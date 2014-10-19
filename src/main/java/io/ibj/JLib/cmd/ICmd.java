package io.ibj.JLib.cmd;

import io.ibj.JLib.exceptions.PlayerException;
import org.bukkit.command.CommandSender;

/**
 * Created by Joe on 5/25/2014.
 */
public interface ICmd {

    public boolean execute(CommandSender sender, ArgsSet args) throws PlayerException;

}
