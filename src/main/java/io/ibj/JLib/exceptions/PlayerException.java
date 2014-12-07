package io.ibj.JLib.exceptions;

import io.ibj.JLib.format.Format;
import io.ibj.JLib.format.TagStyle;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Joe on 6/19/2014.
 */
public class PlayerException extends RuntimeException {
    public PlayerException(String message){
        super(message);
    }
    public PlayerException(Format f){

        f.setTag(ChatColor.DARK_RED+"[Error] "+ChatColor.RED);
        f.setTagStyle(TagStyle.FIRST_LINE);
        this.f = f;
    }

    Format f;

    public void throwToPlayer(CommandSender s){
        s.sendMessage(ChatColor.DARK_RED+"[Error] "+ChatColor.RED+getMessage());
    }
}
