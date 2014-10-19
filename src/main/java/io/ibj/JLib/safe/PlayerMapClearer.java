package io.ibj.JLib.safe;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Observable;

/**
 * Created by Joe on 7/5/2014.
 */
public class PlayerMapClearer extends Observable implements Listener{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        notifyObservers(event.getPlayer());
    }
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        notifyObservers(event.getPlayer());
    }
}
