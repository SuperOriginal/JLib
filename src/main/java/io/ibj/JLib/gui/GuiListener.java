package io.ibj.JLib.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by Joe on 6/30/2014.
 */
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof PageHolder) {
            PageHolder pageHolder = (PageHolder) holder;
            try {
                pageHolder.onEventClose();
            }
            catch(Exception ex){
                pageHolder.getHostPlugin().handleError(ex);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof PageHolder) {
            if(e.getClick() == ClickType.DROP || e.getClick() == ClickType.CONTROL_DROP){
                e.setCancelled(true);
                return;
            }
            PageHolder pageHolder = (PageHolder) holder;
            try{
                pageHolder.onClick(e.getSlot(),e.getClick());
            }
            catch(Exception ex){
                pageHolder.getHostPlugin().handleError(ex);
            }
            e.setCancelled(true);

        }
    }

}
