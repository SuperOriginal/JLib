package io.ibj.JLib.cmd;

import org.bukkit.permissions.Permissible;

/**
 * Created by Joe on 5/25/2014.
 */
public class SimplePermCondition implements PermCondition {

    public SimplePermCondition(String perm){
        this.perm = perm;
    }

    private String perm;

    @Override
    public boolean hasPerm(Permissible permissible) {
        return permissible.hasPermission(perm);
    }
}
