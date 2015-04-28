package io.ibj.JLib.cmd2;

import io.ibj.JLib.exceptions.PlayerException;

import java.util.List;

/**
 * @author joe 2/7/2015
 * Parses an argument and turns it into a Java object. 
 */
public interface ArgParser<T extends Object> {
    T parse(String argument) throws PlayerException;
}
