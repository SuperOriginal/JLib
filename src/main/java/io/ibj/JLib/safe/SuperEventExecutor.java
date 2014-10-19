package io.ibj.JLib.safe;

import io.ibj.JLib.JPlug;
import lombok.AllArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Joe on 6/27/2014.
 */
@AllArgsConstructor
public class SuperEventExecutor implements EventExecutor {

    private Class<? extends Event> eventClass;
    private Method eventMethod;
    private JPlug fallbackError;


    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try{
            if(!eventClass.isAssignableFrom(event.getClass())){
                return;
            }
            eventMethod.invoke(listener,event);
        } catch (InvocationTargetException e) {
            fallbackError.handleError(e);
        } catch (IllegalAccessException e) {
            fallbackError.handleError(e);
        } catch (Exception e){
            fallbackError.handleError(e);
        }
    }
}
