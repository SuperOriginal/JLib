package io.ibj.JLib.cmd;

import org.bukkit.permissions.Permissible;

/**
 * Created by Joe on 5/25/2014.
 */
public interface PermCondition {
    public boolean hasPerm(Permissible permissible);
}
