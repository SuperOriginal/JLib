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
        f = new Format(errorFormat,message);
    }
    
    private static final Format errorFormat = new Format("&4[Error] &c");
    
    public PlayerException(Format f){
        f.setTag(errorFormat);
        this.f = f;
    }

    Format f;

    public void throwToPlayer(CommandSender s){
        if(f == null)
            s.sendMessage(ChatColor.DARK_RED+"[Error] "+ChatColor.RED+getMessage());
        else
        {
            f.sendTo(s);
        }
    }
}
