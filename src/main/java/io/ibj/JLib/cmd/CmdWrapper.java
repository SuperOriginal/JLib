package io.ibj.JLib.cmd;

import io.ibj.JLib.cmd.annotations.*;
import io.ibj.JLib.exceptions.PlayerException;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 5/25/2014.
 */
public class  CmdWrapper<T extends ICmd> {


    @Getter
    private T cmd;

    @Getter @Setter
    private ArgsCondition argsCondition;

    @Getter @Setter
    private PermCondition permCondition;

    @Getter @Setter
    private String permError;

    @Getter @Setter
    private boolean forcePlayer;

    @Getter
    private String root;

    @Getter
    private String[] aliases;

    @Getter
    private String description;

    @Getter
    private String usage;

    @Getter
    private boolean isAggresive;

    protected CmdWrapper(T cmd, ArgsCondition condition, PermCondition perms, String permError, boolean forcePlayer, String root, String[] aliases, String description, String usage, boolean aggresive){
        this.cmd = cmd;
        this.argsCondition = condition;
        this.permCondition = perms;
        this.permError = permError;
        this.forcePlayer = forcePlayer;
        this.root = root;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.isAggresive = aggresive;
    }

    public static <A extends ICmd> CmdWrapper<A> wrap(Class<A> cmd){

        ArgsCondition condition;
        if(cmd.isAnnotationPresent(ArgsEquals.class)){
            condition = new ArgsEqualsCondition(cmd.getAnnotation(ArgsEquals.class).value());
        }
        else if(cmd.isAnnotationPresent(ArgsGreaterThan.class)){
            condition = new ArgsGreaterThanCondition(cmd.getAnnotation(ArgsGreaterThan.class).value());
        }
        else if(cmd.isAnnotationPresent(ArgsRange.class)){
            condition = new ArgsRangeCondition(cmd.getAnnotation(ArgsRange.class).minimum(),cmd.getAnnotation(ArgsRange.class).maximum());
        }
        else
        {
            condition = new ArgsNoLimitsCondition();
        }

        PermCondition perm;
        if(cmd.isAnnotationPresent(Perm.class)) {
            perm = new SimplePermCondition(cmd.getAnnotation(Perm.class).value());
        }
        else
        {
            perm = new NoPermCondition();
        }

        String permError = "You do not have permission to run this command.";

        if(cmd.isAnnotationPresent(PermError.class)){
            permError = cmd.getAnnotation(PermError.class).value();
        }

        boolean forcePlayer = cmd.isAnnotationPresent(ForcePlayer.class);

        String[] aliases;
        if(cmd.isAnnotationPresent(Aliases.class)){
            aliases = cmd.getAnnotation(Aliases.class).value();
        }
        else
        {
            aliases = new String[0];
        }

        String root;
        if(cmd.isAnnotationPresent(RootName.class)){
            root = cmd.getAnnotation(RootName.class).value();
        }
        else
        {
            if(aliases.length == 0){
                throw new IllegalArgumentException("There are no root or alias command names for the passed ICmd class!");
            }
            else
            {
                root = aliases[0];
            }
        }

        Constructor<? extends ICmd> constructor;

        try {
             constructor = cmd.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The passed ICmd does not have an empty constructor to generate a new ICmd instance from!",e);
        }

        ICmd cmdInst;
        try {
            cmdInst = constructor.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Passed ICmd's constructor is not public.");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }

        String description = "No description";
        if(cmd.isAnnotationPresent(Desc.class)){
            description = cmd.getAnnotation(Desc.class).value();
        }

        String usage = "No usage";
        if(cmd.isAnnotationPresent(Usage.class)){
            usage = cmd.getAnnotation(Usage.class).value();
        }

        boolean aggressive = cmd.isAnnotationPresent(Aggressive.class);

        return new CmdWrapper(cmdInst,condition,perm,permError,forcePlayer,root,aliases, description,usage,aggressive);
    }

    public boolean hasPerms(Permissible test){
        return permCondition.hasPerm(test);
    }

    public boolean meetsArgLength(ArgsSet set){
        return set.followsCondition(argsCondition);
    }

    public boolean execute(CommandSender sender, ArgsSet set) throws PlayerException {
        return cmd.execute(sender,set);
    }

    public void help(CommandSender sender){
        if (cmd instanceof IHelpable) {
            IHelpable iHelpable = (IHelpable) cmd;
            iHelpable.help(sender);
        }
        else {
            sender.sendMessage(ChatColor.DARK_PURPLE+"["+ChatColor.LIGHT_PURPLE+"Help"+ChatColor.DARK_PURPLE+"]"+ChatColor.LIGHT_PURPLE+ " "+getUsage());
        }
    }

    List<String> getRootCmdFriendlyNames(){
        List<String> s = new ArrayList<String>();
        for(String st : aliases){
            s.add(st);
        }
        return s;
    }


}
