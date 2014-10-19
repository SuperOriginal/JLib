package io.ibj.JLib.safe;

import io.ibj.JLib.JPlug;
import io.ibj.JLib.exceptions.PlayerException;
import org.bukkit.command.CommandSender;

/**
 * Created by Joe on 8/2/2014.
 */
public class SafeRunnablePlayerWrapper implements Runnable{

    public SafeRunnablePlayerWrapper(JPlug host, Runnable inner, CommandSender notifiable){
        this.host = host;
        this.inner = inner;
        this.notifiable = notifiable;
    }

    private final JPlug host;
    private final Runnable inner;
    private final CommandSender notifiable;

    @Override
    public void run() {
        try{
            inner.run();
        }
        catch(PlayerException e){
            e.throwToPlayer(notifiable);
        }
        catch(Exception e){
            host.handleError(e);
        }
    }
}
