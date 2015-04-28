package io.ibj.JLib.cmd2;

import org.bukkit.command.CommandSender;

/**
 * @author joe 2/8/2015
 */
public interface SenderCaster<T extends Object> {
    
    public boolean isApplicable(CommandSender sender);
    
    public T cast(CommandSender sender);
    
}
