package io.ibj.JLib.cmd2;

import lombok.Getter;

/**
 * @author joe 2/19/2015
 */
public class MethodInvokerRoute {
    
    @Getter
    private CommandMethodInvoker invoker;
    @Getter
    private Route selectedRoute;
    
    public MethodInvokerRoute(CommandMethodInvoker invoker, Route selectedRoute){
        this.invoker = invoker;
        this.selectedRoute = selectedRoute;
    }
    
}
