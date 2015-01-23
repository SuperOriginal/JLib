package io.ibj.JLib.cmd.annotations;

import io.ibj.JLib.cmd.Executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joe 1/22/2015
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
    String name();
    String[] aliases() default {};
    String description();
    String usage();
    String perm() default "";
    String permError() default "You do not have permission to run this command.";
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    boolean aggressive() default false;
    Executor[] executors() default {Executor.COMMAND_BLOCK, Executor.CONSOLE, Executor.PLAYER};
}
