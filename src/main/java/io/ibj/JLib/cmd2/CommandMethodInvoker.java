package io.ibj.JLib.cmd2;

import com.google.common.collect.ImmutableList;
import io.ibj.JLib.exceptions.PlayerException;
import io.ibj.JLib.utils.StringUtils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author joe 2/8/2015
 * Invokes a command reflectivly 
 */
public class CommandMethodInvoker {
    
    public CommandMethodInvoker(Method method, Object object, CommandRegistrar commandRegistrar){
        this.method = method;
        this.object = object;
        this.commandRegistrar = commandRegistrar;
        if(!method.isAnnotationPresent(Cmd.class)){
            throw new IllegalArgumentException("Method does not contain a @Cmd annotation.");
        }
        this.cmd = method.getAnnotation(Cmd.class);
        propogateCaches();
    }
    
    Cmd cmd;
    Method method;
    Object object;
    Map<Class<?>, SenderCaster<?>> senderCacheMap;
    List<ArgParser<?>> argParserCache;
    List<String> argumentNames;
    CommandRegistrar commandRegistrar;
    int expectedArguments;
    boolean expandable;
    
    public boolean isAcceptableArgLength(int args){
        return expandable ? args >= expectedArguments : expectedArguments == args;
    }
    
    private void propogateCaches(){
        senderCacheMap = new HashMap<>();
        argParserCache = new LinkedList<>();
        argumentNames = new LinkedList<>();
        for(Parameter p : method.getParameters()){
            if(p.isAnnotationPresent(Sender.class)){
                SenderCaster senderCaster = commandRegistrar.getCaster(p.getClass());
                if(senderCaster == null){
                    throw new RuntimeException("There is no registered sender caster for the class "+p.getClass().getName());
                }
                senderCacheMap.put(p.getClass(),senderCaster);
                continue;
            }
            
            ArgParser<?> argParser;
            if(p.isAnnotationPresent(Parse.class)){
                argParser = commandRegistrar.getArgParser(p.getAnnotation(Parse.class).value());
                if(argParser == null){
                    throw new RuntimeException("There is no registered arg parser by the name of "+p.getAnnotation(Parse.class).value());
                }
                if(argParser.getClass().getGenericInterfaces().length == 1){
                    if(!argParser.getClass().getGenericInterfaces()[0].getClass().isAssignableFrom(p.getClass())){
                        throw new RuntimeException("The generic defined by the passed argument parser does not match the expected parameter type.");
                    }
                }
            }
            else{
                argParser = commandRegistrar.getArgParser(p.getType());
                if(argParser == null){
                    throw new RuntimeException("There is no registered argument parser for this type.");
                }
            }
            argParserCache.add(argParser);
            argumentNames.add(p.getName());
        }
        boolean multiArg = false; //Perform a multiple args check. The multiple arg parser may only be the last argument
        expectedArguments = 0;
        for(ArgParser parser : argParserCache){
            if(multiArg) { //if we have multiple args and
                throw new RuntimeException("There was a multiple argument parser that was not the last argument.");
            }
            if(parser.getClass().isAnnotationPresent(MultiArgParser.class)){
                multiArg = true;
            }
            expectedArguments++;
        }
        expandable = multiArg;
    }    
    
    /**
     * Returns all of the required sender classes that will need to be passed by the registrar.
     * @return All required sender class types
     */
    public Set<Class<?>> getRequiredSenders(){
        return senderCacheMap.keySet();
    }

    public boolean isAcceptedSender(CommandSender sender){
        for(SenderCaster caster : senderCacheMap.values()){
            if(!caster.isApplicable(sender)){
                return false;
            }
        }
        return true;
    }
    
    public List<String> getArgumentNames(){
        return ImmutableList.copyOf(argumentNames);
    }
    
    public void invoke(CommandSender commandSender, String prefixLabel, List<String> arguments) throws InvocationTargetException, IllegalAccessException {
        Object[] parameterReturns = new Object[method.getParameterCount()];
        Iterator<ArgParser<?>> argParsers = argParserCache.iterator();
        for(int i = 0; i<method.getParameterCount(); i++){
            Parameter parameter = method.getParameters()[i];
            if(parameter.isAnnotationPresent(Sender.class)){
                SenderCaster caster = senderCacheMap.get(parameter.getClass());
                parameterReturns[i] = caster.cast(commandSender);
                continue;
            }
            
            if(!argParsers.hasNext()){
                throw new RuntimeException("Ran out of argument parsers! Has the method changed at runtime?");
            }
            ArgParser argumentParser = argParsers.next();
            String arg;
            if(argumentParser.getClass().isAnnotationPresent(MultiArgParser.class)){
                arg = StringUtils.joinList(arguments.toArray(new String[arguments.size()]));
                arguments.clear();
            }
            else {
                arg = arguments.remove(0);
            }
            parameterReturns[i] = argumentParser.parse(arg);
        }
        try {
            method.invoke(object, parameterReturns);
        }
        catch(HelpException e){
            //TODO: Display help
        }
        catch(PlayerException e){
            e.throwToPlayer(commandSender);
        }
    }
    
    
}
