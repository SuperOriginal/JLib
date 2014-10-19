package io.ibj.JLib.cmd;

/**
 * Created by Joe on 5/25/2014.
 */
public class ArgsGreaterThanCondition implements ArgsCondition {

    public int min;

    public ArgsGreaterThanCondition(int minimum){
        min = minimum;
    }


    @Override
    public boolean accepted(int argscount) {
        return argscount >= min;
    }
}
