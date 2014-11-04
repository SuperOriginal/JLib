package io.ibj.JLib;

import io.ibj.JLib.cmd.ICmd;
import io.ibj.JLib.exceptions.PlayerException;
import io.ibj.JLib.exceptions.PlayerInterruptedException;
import io.ibj.JLib.file.ResourceFile;
import io.ibj.JLib.file.YAMLFile;
import io.ibj.JLib.gui.PageHolder;
import io.ibj.JLib.safe.SafeRunnablePlayerWrapper;
import io.ibj.JLib.safe.SafeRunnableWrapper;
import io.ibj.JLib.safe.SuperEventExecutor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Joe on 6/27/2014.
 */
public abstract class JPlug extends JavaPlugin {

    YAMLFile formatsFile;

    private Set<ResourceFile> resources;
    private Set<Command> registeredCommands;

    public void registerResource(ResourceFile resource){
        resources.add(resource);
    }

    public void reload(){
        for(ResourceFile file : resources){
            file.reloadConfig();
        }
    }
    /////////////////////////////////
    //Events                       //
    /////////////////////////////////

    public void registerEvents(Listener listener){
        if(!this.isEnabled()){
            throw new IllegalPluginAccessException("Dood! U iz not enabuld!");
        }
        for(Map.Entry<Class<? extends Event>,Set<RegisteredListener>> entry : registerListener(listener).entrySet()){
            getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
        }
    }

    private HandlerList getEventListeners(Class<? extends Event> type){
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

    private Map<Class<? extends Event>, Set<RegisteredListener>> registerListener(Listener listener){
        Validate.notNull(listener,"Listener can not be null");

        boolean useTimings = getServer().getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            methods = new HashSet<>(publicMethods.length, 3.4028235E+38F);
            for (Method m : publicMethods) {
                methods.add(m);
            }
            for (Method m : listener.getClass().getDeclaredMethods()) {
                methods.add(m);
            }
        }
        catch(NoClassDefFoundError e){
            handleError(new Exception(e));
            return ret;
        }
        for(Method m : methods){
            EventHandler eh = m.getAnnotation(EventHandler.class);
            if(eh == null) continue;
            if(m.getParameterTypes().length != 1){
                handleError(new Exception("Tried to register a method with not exactly 1 arg.... check this out."));
                continue;
            }
            Class<?> paramClass = m.getParameterTypes()[0];
            if(!Event.class.isAssignableFrom(paramClass)){
                handleError(new Exception("Tried to register a method with a non Event argument."));
                continue;
            }
            Class<? extends Event> castedParamClass = paramClass.asSubclass(Event.class);
            Set<RegisteredListener> eventListeners = ret.get(castedParamClass);
            if(eventListeners == null){
                eventListeners = new HashSet<>();
                ret.put(castedParamClass,eventListeners);
            }
            Class<?> clazz = castedParamClass;
            //for(Class<?> clazz = castedParamClass; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()){
                if(clazz.isAnnotationPresent(Deprecated.class)){
                    handleError(new Exception("Event class is Deprecated! Please choose another."));
                }
                EventExecutor executor = new SuperEventExecutor(clazz.asSubclass(Event.class), m, this);
                if(useTimings){
                    eventListeners.add(new TimedRegisteredListener(listener, executor, eh.priority(), this, eh.ignoreCancelled()));
                }
                else
                {
                    eventListeners.add(new RegisteredListener(listener,executor,eh.priority(),this,eh.ignoreCancelled()));
                }
            //}
        }
        return ret;
    }

    /////////////////////////////////
    //Commands                     //
    /////////////////////////////////

    public void registerCmd(Class<? extends ICmd> cmd)  {
        registeredCommands.add(JLib.getI().register(cmd, this));
    }


    /////////////////////////////////
    //Threads/Delays               //
    /////////////////////////////////

    private BukkitTask runNowPrivate(Runnable r, ThreadLevel t){
        switch(t){
            case SYNC:
                if(Bukkit.isPrimaryThread()){
                    r.run(); //Safe to run
                    return null;
                }
                else {
                    return Bukkit.getScheduler().runTask(this, r);
                }
            case ASYNC:
                if(!Bukkit.isPrimaryThread()){
                    r.run();
                    return null;
                }
                else {
                    return Bukkit.getScheduler().runTaskAsynchronously(this, r);
                }
            case SUPER_ASYNC:
                Thread th = new Thread(r);
                th.start();
                return null;
        }
        return null;
    }

    public BukkitTask runNow(Runnable r, ThreadLevel t){
        return runNowPrivate(new SafeRunnableWrapper(this, r), t);
    }

    public BukkitTask runNow(Runnable r, CommandSender player, ThreadLevel t){
        return runNowPrivate(new SafeRunnablePlayerWrapper(this, r, player), t);
    }

    private BukkitTask runLaterPrivate(final Runnable r, ThreadLevel t, final TimePeriod period){
        switch (t){
            case SYNC:
                return Bukkit.getScheduler().runTaskLater(this, r ,period.getAsTicks());
            case ASYNC:
                return Bukkit.getScheduler().runTaskLaterAsynchronously(this, r ,period.getAsTicks());
            case SUPER_ASYNC:
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(period.getAsMs());
                            r.run();
                        } catch (Exception e) {
                            handleError(e);
                        }
                    }
                });
                th.start();
                return null;
        }
        return null;
    }

    public BukkitTask runLater(Runnable r, ThreadLevel t, TimePeriod period){
        return runLaterPrivate(new SafeRunnableWrapper(this,r),t,period);
    }

    public BukkitTask runLater(Runnable r, ThreadLevel t, TimePeriod period, CommandSender s){
        return runLaterPrivate(new SafeRunnablePlayerWrapper(this,r,s),t,period);
    }

    private BukkitTask runScheduledPrivate(final Runnable r, ThreadLevel t, final TimePeriod delay, final TimePeriod repeated){
        switch(t){
            case SYNC:
                return Bukkit.getScheduler().runTaskTimer(this,r,delay.getAsTicks(),repeated.getAsTicks());
            case ASYNC:
                return Bukkit.getScheduler().runTaskTimerAsynchronously(this, r , delay.getAsTicks(),repeated.getAsTicks());
            case SUPER_ASYNC:
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(delay.getAsMs());
                            while(JPlug.this.isEnabled()){
                                r.run();
                                Thread.sleep(repeated.getAsMs());
                            }
                        } catch (Exception e) {
                            JPlug.this.handleError(e);
                        }
                    }
                });
                th.start();
                return null;
        }
        return null;
    }

    public BukkitTask runScheduled(Runnable r, ThreadLevel t, TimePeriod delay, TimePeriod repeated){
        return runScheduledPrivate(new SafeRunnableWrapper(this,r),t,delay,repeated);
    }

    public BukkitTask runScheduled(Runnable r, ThreadLevel t, TimePeriod delay, TimePeriod repeated, CommandSender s){
        return runScheduledPrivate(new SafeRunnablePlayerWrapper(this,r,s),t,delay,repeated);
    }

    public <T extends Object> T runSync(Callable<T> callable){
        FutureTask<T> task = new FutureTask<>(callable);
        runNow(task,ThreadLevel.SYNC);
        try {
            return task.get();
        } catch (InterruptedException e) {
            throw new PlayerInterruptedException();
        } catch (ExecutionException e) {
            if(e.getCause() instanceof PlayerException){
                throw (PlayerException)e.getCause(); //Pass upstream
            }
            throw new RuntimeException(e.getCause());//Throw it upstream to error catching and such
        }
    }

    public void runSync(Runnable runnable){
        FutureTask task = new FutureTask(runnable,null);
        runNow(task,ThreadLevel.SYNC);
        try {
            task.get();
        } catch (InterruptedException e) {
            throw new PlayerInterruptedException();
        } catch (ExecutionException e) {
            if(e.getCause() instanceof PlayerException){
                throw (PlayerException)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    /////////////////////////////////
    //GUI                          //
    /////////////////////////////////

    public PageHolder makePageHolder(Player p){
        return new PageHolder(p,this);
    }

    /**
     * Should be overridden in order to catch errors coming from the plugin.
     * @param e
     */
    public void handleError(Exception e){
        e.printStackTrace();
        if(!JLib.getI().isDebug())
            JLib.getI().getRaven().sendException(e);
    }

    public void reportEvent(io.ibj.JLib.logging.event.Event e){
        JLib.getI().getRaven().sendEvent(e);
    }

    /* Delegated Methods */
    protected void onModuleEnable() throws Exception{getLogger().warning(getName() + " did not run any code on enable!");}
    protected void onModuleDisable() throws Exception{}
    protected void onFailureToEnable() {}
    protected void onFailureToDisable() {}

    @Override
    public final void onEnable() {
        try {
            saveDefaultConfig();
            this.formatsFile = new YAMLFile(this, "formats.yml");
            this.formatsFile.saveDefaultConfig();
            resources = new HashSet<>();
            registeredCommands = new HashSet<>();
            resources.add(formatsFile);
            onModuleEnable();
        } catch (Exception e) {
            handleError(e);
            onFailureToEnable();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }
    @Override
    public final void onDisable() {
        try {
            onModuleDisable();
            for(Command command : registeredCommands){
                command.unregister(JLib.getI().getCmdMap());
            }
            registeredCommands.clear();
            resources.clear();
        } catch (Exception e) {
            handleError(e);
            onFailureToDisable();
        }
    }

    public final String getFormatRaw(String key, String... formatters) {
        FileConfiguration config = formatsFile.getConfig(); //Get the formats file
        if (!config.contains(key)) return null; //Check if it has this format key, and if not return null
        String unFormattedString = ChatColor.translateAlternateColorCodes('&', config.getString(key)); //Get the un-formatted key
        if (formatters == null) return unFormattedString;
        for(int i = 0; i<formatters.length; i+=2){
            if(formatters[i] == null || formatters[i+1] == null) continue;
            unFormattedString = unFormattedString.replace(formatters[i],formatters[i+1]);
        }
        return unFormattedString; //Return
    }

    public final String getFormatRaw(String key){
        //noinspection NullArgumentToVariableArgMethod
        return getFormatRaw(key,null);
    }
    public final String getFormat(String key, boolean prefix, String... formatters) {
        String formatRaw = getFormatRaw(key, formatters);
        String prefixString = getFormatRaw("prefix");
        return !prefix || prefixString == null ? formatRaw : prefixString + formatRaw;
    }
    public final String getFormat(String key, String... formatters) {
        return getFormat(key, true, formatters);
    }
    public final String getFormat(String key) {
        //noinspection NullArgumentToVariableArgMethod
        return getFormat(key, true, null);
    }
    public final boolean hasFormat(String key) {
        return formatsFile.getConfig().contains(key);
    }


}
