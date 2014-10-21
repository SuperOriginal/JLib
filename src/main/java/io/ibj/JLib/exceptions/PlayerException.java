package io.ibj.JLib.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Joe on 6/19/2014.
 */
public class PlayerException extends RuntimeException {
    public PlayerException(String message){
        super(message);
    }

    public void throwToPlayer(CommandSender s){
        s.sendMessage(ChatColor.DARK_RED+"[Error] "+ChatColor.RED+getMessage());
    }
}
