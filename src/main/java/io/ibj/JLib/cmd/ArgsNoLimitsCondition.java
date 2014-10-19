package io.ibj.JLib.cmd;

/**
 * Created by Joe on 5/25/2014.
 */
public class ArgsNoLimitsCondition implements ArgsCondition {
    @Override
    public boolean accepted(int argscount) {
        return true;
    }
}
