package io.ibj.JLib.cmd2;

import io.ibj.JLib.exceptions.PlayerException;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author joe 2/7/2015
 */
public class CommandRegistrar {
    
    TreeNode<String, Set<MethodInvokerRoute>> commandTreeMap;
    Set<ProxyCommand> proxyCommands;
    
    public void registerCommands(Object object){
        for(Method m : object.getClass().getMethods()){
            if(m.isAnnotationPresent(Cmd.class)){
                registerInvoker(new CommandMethodInvoker(m,object,this));
            }
        }
    }
    
    private void registerInvoker(CommandMethodInvoker commandMethodInvoker){
        for(Route r : commandMethodInvoker.cmd.routes()){
            String[] subRoutes = r.value().split(" ");
            TreeNode<String, Set<MethodInvokerRoute>> node = commandTreeMap;
            for(String s : subRoutes){
                TreeNode<String, Set<MethodInvokerRoute>> subNode = node.getChildByKey(s);
                if(subNode == null){
                    subNode = node.createChild(s,new HashSet<MethodInvokerRoute>());
                }
                node = subNode;
            }
            if(node == commandTreeMap){
                throw new RuntimeException("There were no subwords within the route string, therefore the command could not be registered.");
            }
            node.get().add(new MethodInvokerRoute(commandMethodInvoker,r));
        }
    }
    
    public void handleCommand(CommandSender commandSender, String label, String[] arguments){
        try{
            TreeNode<String, Set<MethodInvokerRoute>> methodTree = commandTreeMap.getChildByKey(label);
            if(methodTree == null){
                throw new RuntimeException("There is no tree node that is registered to the passed label.");
            }
            TreeNode<String, Set<MethodInvokerRoute>> workingNode = methodTree; //Represents the workingNode of the last string passed
            String route = label;
            for(String s : arguments){                                          //For each of our arguments, try to resolve the subtree, if possible.
                TreeNode<String, Set<MethodInvokerRoute>> nextLevelNode = workingNode.getChildByKey(s);
                if(nextLevelNode == null){                                      //If its not possible, we need to resolve which subroute to actually handle the command request
                    MethodInvokerRoute permBackup = null;                       //Used to pass the permission error, if there are no accepted command scopes.
                    Set<MethodInvokerRoute> acceptedCommandScope = new HashSet<>(); //Represents all commands that satisfy the sender type and permission requirements
                            //This represents the command "scope", where help can be generated. The perm backup will be used in the event this isn't filled, the backup will be thrown.
                    //If the backup is empty, this means that nothing is within the sender scope, so then we should handle it as noted below.
                    for(MethodInvokerRoute invoker : workingNode.get()){            //For all invokers within 
                        if(invoker.getInvoker().isAcceptedSender(commandSender)){
                            if(!Objects.equals(invoker.getSelectedRoute().perm(), "")){
                                if(commandSender.hasPermission(invoker.getSelectedRoute().perm())){
                                     acceptedCommandScope.add(invoker);
                                }
                                else
                                {
                                    if(permBackup == null){
                                        permBackup = invoker;
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    workingNode = nextLevelNode;
                    route += " "+s;
                }
            }
        }
        catch (Exception e){

        }
        
    }
    
    public Set<SenderCaster> getSenderCasters(){
        
        return null;
    }
    
    public SenderCaster getCaster(Class<?> senderClass){
        return null;
        
    }
    
    public ArgParser getArgParser(String name){
        return null;
        
    }
    
    public ArgParser getArgParser(Class<?> clazz){
        return null;
        
    }
    
}
