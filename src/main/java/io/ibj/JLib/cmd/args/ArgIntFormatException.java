package io.ibj.JLib.cmd.args;

/**
 * Thrown when an argument cannot be parsed as an Integer
 */
public class ArgIntFormatException extends ArgException {
    public ArgIntFormatException(Arg arg) {
        super(arg.getAsString()+" is not an integer.", arg);
    }
}
