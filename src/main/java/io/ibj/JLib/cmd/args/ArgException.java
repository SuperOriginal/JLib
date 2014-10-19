package io.ibj.JLib.cmd.args;

import io.ibj.JLib.exceptions.PlayerException;

/**
 * Base Argument Exception
 */
public class ArgException extends PlayerException {
    public ArgException(String message, Arg arg){
        super(message);
        this.a = arg;
    }


    private Arg a;


    public Arg getArg() {
        return a;
    }

}
