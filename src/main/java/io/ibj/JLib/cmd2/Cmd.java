package io.ibj.JLib.cmd2;

/**
 * @author joe 2/7/2015
 */
public @interface Cmd 
{
    Route[] routes();
    String usage();
    String desc();
    boolean async() default false;
}
