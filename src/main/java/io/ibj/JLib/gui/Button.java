package io.ibj.JLib.gui;

import io.ibj.JLib.exceptions.PlayerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Observable;

/**
 * Created by Joe on 6/30/2014.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper =  false)
public final class Button extends Observable{

    private ItemStack icon;
    private ClickHandler handler;

    public void handle(Player p, Page page, PageHolder holder, ClickType clickType) throws PlayerException{
        if(handler != null){
            handler.handleClick(p, page, holder, clickType);
        }
    }

    public void setItemStack(ItemStack stack){
        icon = stack;
        update();
    }

    public void update(){
        notifyObservers();
    }

}
