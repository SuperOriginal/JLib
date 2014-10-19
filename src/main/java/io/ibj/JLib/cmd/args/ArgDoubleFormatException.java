package io.ibj.JLib.cmd.args;

/**
 * Thrown when the argument fails Double parsing
 */
public class ArgDoubleFormatException extends ArgException {
    public ArgDoubleFormatException(Arg arg) {
        super(arg.getAsString()+" is not a double.", arg);
    }
}
