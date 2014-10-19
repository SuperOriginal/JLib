package io.ibj.JLib.cmd;

import io.ibj.JLib.JPlug;
import io.ibj.JLib.exceptions.PlayerException;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Joe on 5/25/2014.
 */
public class RootCmdWrapper extends Command{
    public RootCmdWrapper(CmdWrapper wrapper) {
        super(wrapper.getRoot(),wrapper.getDescription(),wrapper.getUsage(),wrapper.getRootCmdFriendlyNames());
        this.wrapper = wrapper;
        this.host = null;
    }

    public RootCmdWrapper(CmdWrapper wrapper, JPlug host){
        this(wrapper);
        this.host = host;
    }

    @Getter
    CmdWrapper wrapper;

    JPlug host;

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        ArgsSet args = new ArgsSet(strings);

        try {

            if (!wrapper.hasPerms(sender)) {
                throw new NoPermsException(wrapper.getPermError());
            }

            if (!wrapper.meetsArgLength(args)) {
                wrapper.help(sender);
                return true;
            }

            if (!(sender instanceof Player) && wrapper.isForcePlayer()){
                throw new NonPlayerExecuteException();
            }

            if (!wrapper.execute(sender, args)) {
                wrapper.help(sender);
                return true;
            }
            return true;
        }
        catch(PlayerException e){
            e.throwToPlayer(sender);
        }
        catch(Exception e){
            if(host != null){
                host.handleError(e);
                new PlayerException("There was a fatal error when processing your command! The error has been sent to be fixed! Sorry about that!").throwToPlayer(sender);
            }
            else
            {
                throw e;//Just throw up the stream, we can't catch it.
            }
        }
        return true;
    }
}
