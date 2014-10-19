package io.ibj.JLib.cmd;

import io.ibj.JLib.exceptions.PlayerException;

/**
 * io.ibj.CmdApi
 * Created by Joe
 * Project: CmdApi
 */
public class CommandException extends PlayerException {
    public CommandException(String message){
        super(message);
    }
}
