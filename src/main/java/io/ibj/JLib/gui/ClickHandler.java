package io.ibj.JLib.gui;

import io.ibj.JLib.exceptions.PlayerException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Created by Joe on 6/30/2014.
 */
public interface ClickHandler {
    public void handleClick(Player player, Page page, PageHolder holder, ClickType clickType) throws PlayerException;
}
