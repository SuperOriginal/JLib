package io.ibj.JLib.cmd.args;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Base argument class
 */
public class Arg {
    public Arg(String input) {
        this.i = input;
    }

    private String i;

    /**
     * Returns the argument as a string
     * @return
     */
    public String getAsString() {
        return i;
    }

    /**
     * Returns the argument as an integer
     * @return Integer
     * @throws ArgIntFormatException
     */
    public Integer getAsInteger() throws ArgIntFormatException {
        try {
            return Integer.parseInt(i);
        } catch (NumberFormatException e) {
            throw new ArgIntFormatException(this);
        }
    }

    /**
     * Returns the argument as a float
     * @return
     * @throws ArgFloatFormatException
     */
    public Float getAsFloat() throws ArgFloatFormatException {
        try {
            return Float.parseFloat(i);
        } catch (NumberFormatException e) {
            throw new ArgFloatFormatException(this);
        }
    }

    /**
     * Returns the argument as a double
     * @return
     * @throws ArgDoubleFormatException
     */
    public Double getAsDouble() throws ArgDoubleFormatException {
        try{
            return Double.parseDouble(i);
        }
        catch(NumberFormatException e){
            throw new ArgDoubleFormatException(this);
        }
    }

    /**
     * Returns an enumeration value from the passed enumeration
     * @param tEnum Enumeration base class
     * @param <E> Enum Type
     * @return Enumeration value
     * @throws ArgEnumNotFoundException
     */
    public <E extends Enum<E>> Enum<E> getFromEnumSet(Class<E> tEnum) throws ArgEnumNotFoundException {
        for (Enum<E> e : tEnum.getEnumConstants()) {
            if (e.toString().equalsIgnoreCase(i)) {
                return e;
            }
        }
        throw new ArgEnumNotFoundException(this, tEnum);
    }

    /**
     * Soft fail of getFromEnumerationSet
     * @param tEnum
     * @param <E>
     * @return
     */
    public <E extends Enum<E>> Enum<E> getFromEnumSetSoftFail(Class<E> tEnum) {
        try {
            return getFromEnumSet(tEnum);
        } catch (ArgEnumNotFoundException e) {
            return null;
        }
    }

    /**
     * Attempts to match the argument with the key vaule of the map
     * @param expected
     * @param <T>
     * @return
     * @throws ArgMapNotFoundException
     */
    public <T extends Object> T getFromMap(Map<String, T> expected) throws ArgMapNotFoundException {
        T ret = expected.get(i);
        if (ret == null) {
            throw new ArgMapNotFoundException(this, expected);
        }
        return ret;
    }

    /**
     * Returns null if not found (Soft)
     * @param expected
     * @param <T>
     * @return
     */
    public <T extends Object> T getFromMapSoft(Map<String, T> expected) {
        try {
            return getFromMap(expected);
        } catch (ArgMapNotFoundException e) {
            return null;
        }
    }

    /**
     * Returns the object thats field meets the argument. The field must be type string.
     * @param set
     * @param field
     * @param <T>
     * @return
     * @throws ArgSetFieldNotFoundException
     */
    public <T extends Object> T getFromSet(Set<T> set, Field field) throws ArgSetFieldNotFoundException{
        try {
            for (T t : set) {
                if (((String) field.get(t)).equalsIgnoreCase(i)) {
                    return t;
                }
            }
            throw new ArgSetFieldNotFoundException(this,set,field);
        } catch (IllegalAccessException e1) {
            return null;
        }
    }

    /**
     * Soft fail of getFromSet
     * @param set
     * @param f
     * @param <T>
     * @return
     */
    public <T extends Object> T getFromSetSoft(Set<T> set, Field f){
        try{
            return getFromSet(set,f);
        } catch (ArgSetFieldNotFoundException e) {
            return null;
        }
    }

    /**
     * Returns the object that's method return matches the argument. Must return string.
     * @param set
     * @param method
     * @param methodArgs
     * @param <T>
     * @return
     * @throws ArgSetMethodNotFoundException
     */
    public <T extends Object> T getFromSet(Set<T> set, Method method, Object... methodArgs) throws ArgSetMethodNotFoundException{
        try{
            for(T t : set){
                if(((String) method.invoke(t,methodArgs)).equalsIgnoreCase(i)){
                    return t;
                }
            }
            throw new ArgSetMethodNotFoundException(this,set,method,methodArgs);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Soft fail of getFromSet
     * @param set
     * @param method
     * @param methodArgs
     * @param <T>
     * @return
     */
    public <T extends Object> T getFromSetSoft(Set<T> set, Method method, Object... methodArgs){
        try{
            return getFromSet(set,method,methodArgs);
        } catch (ArgSetMethodNotFoundException e) {
            return null;
        }
    }

    private static final Map<String, Integer> TIMEMAP = new HashMap<>();

    static{
        TIMEMAP.put("s",1000);
        TIMEMAP.put("m",60000);
        TIMEMAP.put("h",3600000);
        TIMEMAP.put("d",24*60*60*1000);
        TIMEMAP.put("w",7*24*60*60*1000);
        TIMEMAP.put("y",365*24*60*60*1000);
    }

    /**
     * Retrieves the arg as a time duration, in the format #u, where # is a number, and u is a unit, either s, m, h, d, w, or y.
     * @return
     */
    public Long getAsDuration() throws ArgException {
        if(i.length() < 2){
            //Throw error
        }
        String unit = i.substring(i.length()-1); //Last letter
        Integer multiplier = TIMEMAP.get(unit.toLowerCase());
        if(multiplier == null){
            //Throw error
        }
        Double time = null;
        try {
             time = Double.parseDouble(i.substring(0, i.length() - 2));
        }
        catch(Exception e){
            throw new ArgException("Welp, thats not a date!",this);
        }
        return new Long((long) (time*multiplier));
    }
}
