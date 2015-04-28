package io.ibj.JLib.cmd2;

/**
 * @author joe 2/7/2015
 */
public @interface Route {
    
    String value();
    String perm() default "";
    String permError() default "You do not have permission to run this command.";
    Executor[] executors() default {Executor.COMMAND_BLOCK, Executor.CONSOLE, Executor.PLAYER, Executor.MINECART, Executor.REMOTE_CONSOLE};

}
