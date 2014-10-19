package io.ibj.JLib.utils.exceptions;

/**
 * io.ibj.utils.exceptions
 * Created by Joe
 * Project: Utils
 */
public class WorldNotFoundException extends Exception{

    public WorldNotFoundException(String worldTried){
        super("The world "+worldTried+" was not found");
    }
}
