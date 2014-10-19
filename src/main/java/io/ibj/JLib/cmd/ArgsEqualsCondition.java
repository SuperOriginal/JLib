package io.ibj.JLib.cmd;

/**
 * Created by Joe on 5/25/2014.
 */
public class ArgsEqualsCondition implements ArgsCondition{

    private int a;

    public ArgsEqualsCondition(int count){
        a = count;
    }

    @Override
    public boolean accepted(int argscount) {
        return argscount == a;
    }
}
