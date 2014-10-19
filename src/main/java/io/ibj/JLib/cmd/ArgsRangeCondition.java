package io.ibj.JLib.cmd;

/**
 * Created by Joe on 5/25/2014.
 */
public class ArgsRangeCondition implements ArgsCondition{

    private int min;
    public int max;

    public ArgsRangeCondition(int min, int max){
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean accepted(int argscount) {
        return min <=argscount && max >= argscount;
    }
}
