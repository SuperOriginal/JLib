package io.ibj.JLib.cmd;

/**
 * Created by Joe on 6/19/2014.
 */
public class NonPlayerExecuteException extends CommandException {
    public NonPlayerExecuteException() {
        super("The sender must be a player.");
    }
}
