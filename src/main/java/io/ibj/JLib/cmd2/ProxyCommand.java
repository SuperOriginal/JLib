package io.ibj.JLib.cmd2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
* @author joe 2/7/2015
        */
public class ProxyCommand extends Command {

    public ProxyCommand(String name, CommandRegistrar commandRegistrar){
        super(name);
        this.commandRegistrar = commandRegistrar;
    }

    CommandRegistrar commandRegistrar;

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        commandRegistrar.handleCommand(commandSender,s,strings);
        return true;
    }
}
