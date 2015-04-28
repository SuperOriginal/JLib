package io.ibj.JLib.cmd.args;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * io.ibj.MattShops.cmd.args
 * Created by Joe
 * Project: MattShops
 */
public class ArgSetFieldNotFoundException extends ArgSetNotFoundException {
    public ArgSetFieldNotFoundException(Arg arg, Set<? extends Object> s, Field query) {
        super(arg, s, "No objects of the object matched the argument.");
        this.query = query;
    }

    private Field query;

    public Field getField(){
        return query;
    }
}
