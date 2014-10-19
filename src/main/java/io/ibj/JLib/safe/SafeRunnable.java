package io.ibj.JLib.safe;

import io.ibj.JLib.JPlug;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Joe on 6/29/2014.
 */
@AllArgsConstructor
public abstract class SafeRunnable implements Runnable{

    @Getter
    private JPlug host;

    public abstract void safeRun();

    @Override
    public void run(){
        try{
            safeRun();
        }
        catch(Exception e){
            host.handleError(e);
        }
    }


}
