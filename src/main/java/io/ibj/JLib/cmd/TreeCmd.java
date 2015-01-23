package io.ibj.JLib.cmd;

import io.ibj.JLib.cmd.args.ArgMapNotFoundException;
import io.ibj.JLib.exceptions.PlayerException;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joe on 5/25/2014.
 */

public abstract class TreeCmd implements ICmd,IHelpable{

    public Map<String, CmdWrapper> cmdMap = new HashMap<String,CmdWrapper>();
    private List<CmdWrapper> helpList = new ArrayList<>();

    protected <T extends ICmd> T registerCmd(Class<T> cmd){
        CmdWrapper<T> wrapper = CmdWrapper.wrap(cmd);
        cmdMap.put(wrapper.getRoot(),wrapper);
        helpList.add(wrapper);
        for(String alias : wrapper.getAliases()){
            cmdMap.put(alias,wrapper);
        }
        return wrapper.getCmd();
    }


    @Override
    public boolean execute(CommandSender sender, ArgsSet args) throws PlayerException{
        if(args.size() == 0){
            return executeIfNoSubFound(sender,args);
        }

        CmdWrapper selected;
        try{
            selected = args.get(0).getFromMap(cmdMap);
        } catch (ArgMapNotFoundException e) {
            return executeIfNoSubFound(sender,args);
        }

        if(!selected.hasPerms(sender)){
            throw new NoPermsException(selected.getPermError());
        }

        boolean satisfiesExecutor = false;
        for(Executor executor : selected.getExecutors()){
            switch (executor){
                case PLAYER:
                    if(sender instanceof Player){
                        satisfiesExecutor = true;
                    }
                    break;
                case CONSOLE:
                    if(sender instanceof ConsoleCommandSender){
                        satisfiesExecutor = true;
                    }
                    break;
                case COMMAND_BLOCK:
                    if(sender instanceof BlockCommandSender){
                        satisfiesExecutor = true;
                    }
                    break;
                case MINECART:
                    if(sender instanceof CommandMinecart){
                        satisfiesExecutor = true;
                    }
                    break;
                case REMOTE_CONSOLE:
                    if(sender instanceof RemoteConsoleCommandSender){
                        satisfiesExecutor = true;
                    }
                    break;
            }
        }
        if(!satisfiesExecutor){
            throw new CommandException("You may not run this command! You are not the correct type of command executor.");
        }

        args.stripArg();

        if(!selected.meetsArgLength(args)){
            selected.help(sender);
            return true;
        }

        if(!selected.execute(sender,args)){
            selected.help(sender);
            return true;
        }
        return true;
    }



    public boolean executeIfNoSubFound(CommandSender sender, ArgsSet args) throws PlayerException{
        return false;
    }

    public void help(CommandSender sender){
        List<CmdWrapper> cmdList = new ArrayList<>();

        for (CmdWrapper cmdWrapper : helpList) {
            if(cmdWrapper.hasPerms(sender)){
                boolean satisfiesExecutor = false;
                for(Executor executor : cmdWrapper.getExecutors()){
                    switch (executor){
                        case PLAYER:
                            if(sender instanceof Player){
                                satisfiesExecutor = true;
                            }
                            break;
                        case CONSOLE:
                            if(sender instanceof ConsoleCommandSender){
                                satisfiesExecutor = true;
                            }
                            break;
                        case COMMAND_BLOCK:
                            if(sender instanceof BlockCommandSender){
                                satisfiesExecutor = true;
                            }
                            break;
                        case MINECART:
                            if(sender instanceof CommandMinecart){
                                satisfiesExecutor = true;
                            }
                            break;
                        case REMOTE_CONSOLE:
                            if(sender instanceof RemoteConsoleCommandSender){
                                satisfiesExecutor = true;
                            }
                            break;
                    }
                }
                if(satisfiesExecutor){
                    cmdList.add(cmdWrapper);
                }
            }
        }
        if(cmdList.isEmpty()){
            new PlayerException("There are no commands contained within this help.").throwToPlayer(sender);
        }
        else
        {
            for(CmdWrapper w : cmdList){
                sender.sendMessage(ChatColor.AQUA+w.getUsage()+" "+ ChatColor.WHITE+"-"+ChatColor.DARK_AQUA+" "+w.getDescription());
            }
        }
    }
}
