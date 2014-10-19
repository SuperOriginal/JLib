package io.ibj.JLib.cmd;

import io.ibj.JLib.cmd.args.Arg;

import java.util.ArrayList;

/**
 * Created by Joe on 5/25/2014.
 */
public class ArgsSet extends ArrayList<Arg> {

    public ArgsSet(String[] args){
        for(String s : args){
            add(new Arg(s));
        }
    }

    public boolean followsCondition(ArgsCondition condition){
        if(condition == null){
            return true;
        }
        return condition.accepted(size());
    }

    public Arg stripArg(){
        return remove(0);
    }

    public String joinArgs(int start, int end){
        StringBuilder b = new StringBuilder();
        for(int i = start; i<=end; i++){
            b.append(get(i).getAsString());
            if(i != end){
                b.append(" ");
            }
        }
        return b.toString();
    }

    public String joinArgs(int start){
        return joinArgs(start,size()-1);
    }

    public String joinArgs(){
        return joinArgs(0);
    }

}
