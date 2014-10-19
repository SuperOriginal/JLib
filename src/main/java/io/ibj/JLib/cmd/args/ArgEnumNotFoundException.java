package io.ibj.JLib.cmd.args;

/**
 * io.ibj.MattShops.cmd.args
 * Created by Joe
 * Project: MattShops
 */
public class ArgEnumNotFoundException extends ArgException {
    public ArgEnumNotFoundException(Arg arg,Class<? extends Enum<?>> e) {
        super(arg.getAsString()+" is not a part of the queried enumeration.", arg);
        this.e = e;
    }

    private Class<? extends Enum<?>> e;

    public Class<? extends Enum<?>> getEnumeration(){
        return e;
    }
}
