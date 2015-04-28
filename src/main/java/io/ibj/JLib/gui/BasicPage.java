package io.ibj.JLib.gui;

import org.bukkit.entity.Player;

/**
 * Created by Joe on 6/30/2014.
 */
public class BasicPage extends Page implements Cloneable{
    public BasicPage(String name, Integer size) {
        super(name, size);
        buttons = new Button[size];
    }

    public BasicPage(String name, Integer size, Button... buttons){
        this(name,size);
        addButtons(buttons);
    }

    private Integer nextFreeSpot(){
        for(int i = 0; i<buttons.length; i++){
            if(buttons[i]== null){
                return i;
            }
        }
        return null;
    }

    public void addButton(Button b){
        Integer nextFreeSlot = nextFreeSpot();  //Find the next free slot
        if(nextFreeSlot == null){   //If we couldn't find one
            throw new RuntimeException("No empty spot to insert the passed button!");
        }
        buttons[nextFreeSlot] = b;  //Set the new button inside the empty position
        updateButton(nextFreeSlot); //Make sure to notify the observers that the button changed
    }

    public void setButton(Button b, Integer slot){
        if(slot >= getSize()){
            throw new IllegalArgumentException("The slot was greater than the allocated number of spaces.");
        }
        buttons[slot] = b;
        updateButton(slot);
    }

    public void addButtons(Button... b){
        for(Button button : b){
            addButton(button);
        }
    }

    public Button getButton(Integer slot){
        return buttons[slot];
    }

    public Button[] getButtons(){
        return buttons;
    }

    public void removeButton(Integer slot){
        setButton(null,slot);
    }

    private Button[] buttons;

    @Override
    public Button[] getButtons(Player observer) {
        return buttons;
    }

    @Override
    public Button getButton(Player observer, Integer slot) {
        return buttons[slot];
    }

    @Override
    public void onClose(PageHolder holder, PageCloseCause cause) {
        //We dont care? I dont know.
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Button[] bRet = new Button[buttons.length];
        for(int i = 0; i<bRet.length; i++){
            if(buttons[i] == null){
                bRet[i] = null;
            }
            else {
                bRet[i] = (Button) buttons[i].clone();
            }
        }
        return new BasicPage(getName(),getSize(),bRet);
    }
}
