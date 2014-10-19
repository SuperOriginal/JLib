package io.ibj.JLib.tp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Joe on 8/5/2014.
 */
public class DirectTPProvider implements TPProvider {
    @Override
    public void tp(Player p, Location l) {
        p.teleport(l);
    }
}
