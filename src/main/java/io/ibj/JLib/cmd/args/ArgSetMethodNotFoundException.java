package io.ibj.JLib.cmd.args;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * io.ibj.MattShops.cmd.args
 * Created by Joe
 * Project: MattShops
 */
public class ArgSetMethodNotFoundException extends ArgSetNotFoundException {
    public ArgSetMethodNotFoundException(Arg arg, Set<? extends Object> s, Method m, Object... methodArgs) {
        super(arg, s, "No objects of the object matched the argument.");
        this.m = m;
        this.args = methodArgs;
    }

    public Object[] args;
    public Method m;

    public Object[] getMethodArgs(){
        return args;
    }

    public Method getMethod(){
        return m;
    }
}
