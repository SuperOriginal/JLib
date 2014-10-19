package io.ibj.JLib.safe;

import io.ibj.JLib.JPlug;

/**
 * Created by Joe on 6/29/2014.
 */
public class SafeRunnableWrapper implements Runnable {

    public SafeRunnableWrapper(JPlug host, Runnable inner){
        this.plugin = host;
        this.inner = inner;
    }

    private JPlug plugin;
    private Runnable inner;

    @Override
    public void run() {
        try{
            inner.run();
        }
        catch(Exception e){
            plugin.handleError(e);
        }
    }
}
