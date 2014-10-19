package io.ibj.JLib.cmd;

import org.bukkit.permissions.Permissible;

/**
 * Created by Joe on 5/25/2014.
 */
public class NoPermCondition implements PermCondition {
    @Override
    public boolean hasPerm(Permissible permissible) {
        return true;
    }
}
