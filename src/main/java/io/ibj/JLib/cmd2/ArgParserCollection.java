package io.ibj.JLib.cmd2;

/**
 * @author joe 2/8/2015
 */
public class ArgParserCollection {
    
    public ArgParserCollection(){
        
        
    }
    
    
    
    
    public <T extends Object> ArgParser<T> getByClassName(Class<T> argumentType, String className){
        return null;
    }
    
    public <T extends Object> ArgParser<T> getByTargetObject(Class<T> argumentType){
        return null;
    }
    
}
