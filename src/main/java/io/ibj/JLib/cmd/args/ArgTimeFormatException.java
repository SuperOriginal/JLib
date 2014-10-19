package io.ibj.JLib.cmd.args;

/**
 * Created by Joe on 6/25/2014.
 */
public class ArgTimeFormatException extends ArgException {
    public ArgTimeFormatException(Arg arg) {
        super(arg.getAsString()+" is not a valid time format.", arg);
    }
}
