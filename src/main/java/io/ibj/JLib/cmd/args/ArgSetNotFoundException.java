package io.ibj.JLib.cmd.args;

import java.util.Set;

/**
 * io.ibj.MattShops.cmd.args
 * Created by Joe
 * Project: MattShops
 */
public class ArgSetNotFoundException extends ArgException {
    public ArgSetNotFoundException(Arg arg, Set<? extends Object> s,String message) {
        super(message, arg);
        this.s = s;
    }

    protected Set<? extends Object> s;

    public Set<? extends Object> getSet(){
        return s;
    }
}
