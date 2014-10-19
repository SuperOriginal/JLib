package io.ibj.JLib.cmd.args;

/**
 * Thrown when an argument cannot be parsed as a float
 */
public class ArgFloatFormatException extends ArgException {
    public ArgFloatFormatException(Arg arg) {
        super(arg.getAsString()+" is not float.", arg);
    }
}
