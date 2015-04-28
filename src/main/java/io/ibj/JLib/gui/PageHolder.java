package io.ibj.JLib.gui;

import io.ibj.JLib.*;
import io.ibj.JLib.exceptions.PlayerException;
import io.ibj.JLib.utils.ReflectionUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

/**
 * This reincarnation is focusing on 2 basic items: 1. MUCH cleaner code, that is actually understandable, and 2. Condensing of code, taking out unnecessary classes, and just making everything much more streamlined.
 * Essentially, here is a lot of words about how this API will operate. There are 2 structures that control at least this section of the GUI. First, being the {@link Page},
 * provides the name of a page, the size, and the buttons for the GUI. Inside of an {@link Page} is a collection of {@link io.ibj.JLib.gui.Button}s, that extend the {@link java.util.Observable}
 * class. Whenever a PageHolder loads a button, the PageHolder will "observe" that button for changes. Whenever the button changes its displayable data, the button sends an update to all of its observers, for them to
 * later update that button for that user. For each player using a GUI, they should have their own PageHolder object. This PageHolder will listen for when the inventory is opened and closed, even if it is invoked through
 * its own API. These opening and closing events will then trigger when a PageHolder observes a button. If the GUI is not open, the PageHolder will not listen for updates. This ensures that for all players watching a GUI,
 * when the API changes a button, all users will see the button change. In addition to listening to Buttons, the PageHolder will also listen to the IPage itself. Whenever the IPage needs to update one of its buttons,
 * the IPage should notify the PageHolder to update the button reference. The PageHolder will unregister from the old button, and register to the new button.
 *
 * Now, for a more programmable approach. When giving a player a new GUI, the programmer should follow the following steps:
 *  1. Create a new PageHolder object for the player you are opening the Menu for
 *  2. Load the GUI
 *  3. Open the GUI
 *
 *  And, well, that's really it. You can throw away the PageHolder object. No registering or anything. When the events are called, the PageHolder is attached to the Inventory.
 */
public class PageHolder implements InventoryHolder, Observer {


    public PageHolder(Player observer, JPlug plugin){
        this.observer = observer;
        this.hostPlugin = plugin;
    }

    @Getter
    private Player observer;

    private Inventory inventory;

    @Getter
    private Page page;

    private Button[] buttons;

    @Getter
    private JPlug hostPlugin;

    public void load(Page newPage){
        /*
        If we are working with the same page that is loaded into the PageHolder, then we really don't care. If the GUI is open, all button changes SHOULD have been changed through Observable calls.
        If the gui was not opened, then we REALLY don't care, since the page will get re-cached on opening.
        If the page is new, then lets check to see if the GUI is open. If it is closed, we can simply swap out the page reference, as it will be re-cached.
        If the page is open, we can next check to see if a page relaunch is necessary. If the name and size are the same, we can simply write a new buttons array, then update the inventory, then
        .updateInventory() the inventory to force all items in the inventory to propagate to the user, since doing manual checks to see if the stacks are the same are too time consuming and stupid.
        If either the name or size are not the same, we are forced to close and reopen a new inventory.

        REMEMBER TO UPDATE THOSE OBSERVERS!
         */
        if(!newPage.equals(this.page)){
            if(isOpen) {
                if (page.getSize().equals(newPage.getSize()) && page.getName().equals(newPage.getName())) {
                    Page lastPage = page;
                    page = newPage;
                    page.addObserver(this);//Register to our new page
                    lastPage.deleteObserver(this);  //And deregister to our last page
                    deleteButtonObservers();
                    buttons = page.getButtons(observer);    //Float the new buttons into our button cache
                    for (int i = 0; i < page.getSize(); i++) {
                        Button nextButton = buttons[i];
                        if (nextButton != null) {             //Load the new buttons in, one by one, and register to them, since we are open
                            nextButton.addObserver(this);
                            inventory.setItem(i, nextButton.getIcon());
                        }
                        else
                        {
                            inventory.setItem(i,null);//If we found a null button, place air inside the inventory
                        }
                    }
                    observer.updateInventory();//Force mass update of inventory
                }
                else
                {
                    close(PageCloseCause.OVERWRITTEN);
                    page = newPage;
                    open();
                }
            }
            else
            {
                page = newPage; //We aren't open, so we can just replace this.
            }
        }
    }

    public void open(){
        if(isOpen){
            return; //We are already open
        }
        inventory = Bukkit.createInventory(this,page.getSize(),page.getName()); //Create a new inventory with the given size and name
        Button[] pageButtons = page.getButtons(observer);   //Get the buttons personalized for this viewer
        for(int i = 0; i< inventory.getSize(); i++){
            Button b = pageButtons[i];                      //Get the next button to insert
            if(b != null){                                  //Make sure that the button isn't null... (Buttons CAN be null!)
                inventory.setItem(i,b.getIcon());                 //Set the inv to the icon
            }
            else
            {
                inventory.setItem(i,null);                        //If the button is null, just object the icon to air, because we have nothing better to object it to.
            }
        }
        buttons = pageButtons; //Lets keep a direct reference of this. Therefore, if a change happens upstream, the EventHandler is already passed downstream
        observer.openInventory(inventory);  //Lets finally open this thing up to the user
        isOpen = true;
    }

    public void close(){
        PageCloseCause cause = PageCloseCause.APICLOSE;
        close(cause);
    }

    void close(PageCloseCause cause) {
        if(!isOpen){
            return; //We already closed, or never opened
        }
        /*
        Just close the inventory, then deregister all buttons and gui pages
         */
        observer.closeInventory();
        page.deleteObserver(this);
        page.onClose(this,cause);
        deleteButtonObservers();
        isOpen = false;
    }

    protected void updateSlot(Integer i){
        sendPacket(observer,1,i,buttons[i].getIcon());
    }


    @Getter
    private boolean isOpen;

    void onEventClose(){
        if(isOpen){
            isOpen = false;
            deleteButtonObservers();
            page.deleteObserver(this);
            page.onClose(this,PageCloseCause.EVENTCLOSE);
        }
    }

    void onClick(Integer slot, ClickType clickType){
        if(slot >= buttons.length || slot < 0){
            return;
        }
        Button clickedButton = buttons[slot];
        if(clickedButton != null){
            try {
                clickedButton.handle(observer, page, this, clickType);
            }
            catch(PlayerException e){
                e.throwToPlayer(observer);
            }
        }
    }

    private void deleteButtonObservers() {
        for(Button b : buttons){
            if(b != null){
                b.deleteObserver(this);
            }
        }
    }


    @Override
    public Inventory getInventory() {
        return inventory;
    }

    //Called when an underlying button changes
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Button){    //If we are a button
            for(int i = 0; i<getInventory().getSize(); i++){//Lets search our button array to try to find it
                if(buttons[i].equals(o)) {   //If we are the same button
                    inventory.setItem(i,((Button) o).getIcon());
                    updateSlot(i);           //Force the client to update that slot
                    return;                 //That's all we really need to do...
                }
            }
            return;//Couldn't find the button
        }
        else if(o instanceof Page){
            if(!o.equals(page)){//If we are no longer the current page, we could access a bad button out of index, so lets just ignore this happened.
                return;
            }
            Integer slot = (Integer) arg; //The argument is the slot that needs updating
            Button currentButton = buttons[slot];
            if(currentButton != null){
                currentButton.deleteObserver(this);//Lets not observe this button anymore.
            }
            final Button button = page.getButton(observer, slot);   //Get the new button to update
            if(button != null){ //Make sure we are not null
                button.addObserver(this);   //Lets watch this button now, since it is in our GUI
                inventory.setItem(slot,button.getIcon());   //Update us serverside
            }
            else
            {
                inventory.setItem(slot,null);           //Just object the slot null
            }
            buttons[slot] = button;  //Lets write the new button to that slot
            updateSlot(slot);   //Force update of the slot
        }
    }



    /////////////////////////////////
    //Raw Packets/ Reflection      //
    /////////////////////////////////

    private static Class slotUpdateRaw;

    private static Constructor constructor;

    private static Method getNMSCopy;

    private static boolean rawEnabled = false;



    /*
     * This will return NASTY errors if not all the packages are found! Like, really bad. Trust me.
     */

    public static void setUpReflection(){
        try{
            slotUpdateRaw = ReflectionUtils.getNMSClass("PacketPlayOutSetSlot");
            constructor = slotUpdateRaw.getDeclaredConstructor(int.class, int.class, ReflectionUtils.getNMSClass("ItemStack"));
            getNMSCopy = ReflectionUtils.getCBClass("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class);
            rawEnabled = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Object getPacket(int window, int slot, Object is){
        try {
            return constructor.newInstance(window,slot,is);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getNmsCopy(ItemStack stack){
        try {
            return getNMSCopy.invoke(null,stack);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }



    protected static void sendPacket(Player p, int window, int slot, ItemStack toReplaceWith){
        ReflectionUtils.sendPacket(p, getPacket(window, slot, getNmsCopy(toReplaceWith)));
    }
}
