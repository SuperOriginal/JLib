package io.ibj.JLib.tp;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Trade;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by Joe on 8/5/2014.
 */
public class EssentialsTPProvider implements TPProvider {

    private Essentials ess;

    public EssentialsTPProvider(Plugin ess){
        this.ess = (Essentials) ess;
    }

    @Override
    public void tp(Player p, Location l) {
        try {
            ess.getUser(p).getTeleport().teleport(l,new Trade(0,ess), PlayerTeleportEvent.TeleportCause.PLUGIN);
        } catch (Exception e) {
            p.sendMessage(e.getMessage());
        }
    }
}
