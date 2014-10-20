package io.ibj.JLib.file.gson;


import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 10/1/2014.
 */
public class GsonSerializableFieldMap<T extends Object> {

    public GsonSerializableFieldMap(Class<T> clazz){
        if(!clazz.isAnnotationPresent(Gson.class))
            throw new IllegalArgumentException("Class must have the @Gson annotation to be serialized!");
        this.clazz = clazz;
        this.fieldMap = new HashMap<>();
        for(Field f : clazz.getFields()){
            if(f.isAnnotationPresent(GsonField.class)){
                GsonField field = f.getAnnotation(GsonField.class);
                if(field.value() == ""){
                    fieldMap.put(f.getName(),f);
                }
                else
                {
                    fieldMap.put(field.value(),f);
                }
            }
        }
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class must have an empty public constructor");
        }
    }

    private GsonSerializableFieldMap(){}

    public static <T extends Object> GsonSerializableFieldMap<T> create(Class<T> clazz){
        if(!clazz.isAnnotationPresent(Gson.class))
            return null;
        GsonSerializableFieldMap ret = new GsonSerializableFieldMap();
        ret.clazz = clazz;
        ret.fieldMap = new HashMap<>();
        for(Field f : clazz.getFields()){
            if(f.isAnnotationPresent(GsonField.class)){
                GsonField field = f.getAnnotation(GsonField.class);
                if(field.value() == ""){
                    ret.fieldMap.put(f.getName(),f);
                }
                else
                {
                    ret.fieldMap.put(field.value(),f);
                }
            }
        }
        try {
            ret.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        }
        return ret;
    }

    @Getter
    private Class<T> clazz;
    private Map<String, Field> fieldMap;
    private Constructor<T> constructor;

    public T getNew(){
        try {
            return constructor.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    public Map<String, Field> getSerializableFields(){
        return ImmutableMap.copyOf(fieldMap);
    }
}
