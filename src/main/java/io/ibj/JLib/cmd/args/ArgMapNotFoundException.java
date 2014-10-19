package io.ibj.JLib.cmd.args;

import java.util.Map;

/**
 * Thrown when the argument is not present within the passed map
 */
public class ArgMapNotFoundException extends ArgException {
    public ArgMapNotFoundException(Arg arg, Map<String, ? extends Object> passed) {
        super(arg.getAsString()+" is not part of the passed map.", arg);
        this.passed = passed;
    }

    private Map<String,? extends Object> passed;

    public Map<String, ? extends Object> getMap(){
        return passed;
    }
}
