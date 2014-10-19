package io.ibj.JLib.exceptions;

/**
 * Created by Joe on 8/5/2014.
 */
public class PlayerInterruptedException extends PlayerException {
    public PlayerInterruptedException() {
        super("A process triggered by your actions have been interrupted.");
    }
}
