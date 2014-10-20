package io.ibj.JLib.file.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.ibj.JLib.file.gson.collection.ListSerializer;
import io.ibj.JLib.file.gson.collection.SetSerializer;
import io.ibj.JLib.file.gson.primative.*;
import lombok.Getter;
import org.joda.time.Instant;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a class used to serialize a class and deserialze a class without much developer intervention. Will attempt to serialize as deeply as possible.
 * All classes that can be serialized with a @Gson annotation, will be inserted within a Json Object, with each field being annotated with a @GsonField being
 * serialized. If a class is not a Set, List, Map, primitive, String, or number, the underlying model class will need to also implement @Gson. All serializable objects && subobjects
 * must have an empty constructor. If a subclass that needs serialized does not implement @Gson, or if the @Gson class does not contain an empty constructor, the wrapper will throw an
 * exception.
 */
public class GsonWrapper {

    public GsonWrapper(com.google.gson.Gson gson){
        this.gson = gson;
        rawTypeMap = new HashMap<>();
        classFieldMap = new HashMap<>();
        registerRawAdapter(new BigDecimalSerializer(), BigDecimal.class);
        registerRawAdapter(new BigIntegerSerializer(), BigInteger.class);
        registerRawAdapter(new BooleanSerializer(), Boolean.TYPE, Boolean.class);
        registerRawAdapter(new ByteSerializer(), Byte.TYPE, Byte.class);
        registerRawAdapter(new CharSerializer(), Character.TYPE, Character.class);
        registerRawAdapter(new DoubleSerializer(),Double.TYPE, Double.class);
        registerRawAdapter(new FloatSerializer(),Float.TYPE, Float.class);
        registerRawAdapter(new IntSerializer(), Integer.TYPE, Integer.class);
        registerRawAdapter(new LongSerializer(), Long.TYPE, Long.class);
        registerRawAdapter(new ShortSerializer(), Short.TYPE, Short.class);
        registerRawAdapter(new StringSerializer(), String.class);
        registerRawAdapter(new ListSerializer(), List.class);
        registerRawAdapter(new SetSerializer(), Set.class);
        registerRawAdapter(new InstantSerializer(), Instant.class);
    }

    @Getter
    com.google.gson.Gson gson;

    private Map<Type, GsonWrapperSerializer> rawTypeMap;
    private Map<Class, GsonSerializableFieldMap> classFieldMap;

    public JsonElement serialize(Object object){
        Class<? extends Object> me = object.getClass();   //Retrieve the class of the object we are trying to serialize
        GsonSerializableFieldMap<? extends Object> fieldMap = classFieldMap.get(me);  //Try to find the cached class field map
        if(fieldMap == null){   //If we didn't find a field cache map
            fieldMap = new GsonSerializableFieldMap(me);    //Create a new one
            classFieldMap.put(me,fieldMap);             //And cache it
        }
        JsonObject base = new JsonObject();     //Create a new object to store this object in
        Class currentLevel = me;
        while(currentLevel != null) {
            fieldMap = classFieldMap.get(currentLevel);  //Try to find the cached class field map
            if(fieldMap == null){   //If we didn't find a field cache map
                fieldMap = GsonSerializableFieldMap.create(currentLevel);    //Create a new one
                if(fieldMap != null) {
                    classFieldMap.put(currentLevel, fieldMap);             //And cache it
                }
                else
                {
                    break;
                }
            }
            for (Map.Entry<String, Field> field : fieldMap.getSerializableFields().entrySet()) {  //For each field inside of our object
                if (!field.getValue().isAccessible()) {
                    try {
                        field.getValue().setAccessible(true);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Could not make field accessable!");
                    }
                }
                Type fieldType = field.getValue().getGenericType();     //Retrieve the type
                GsonWrapperSerializer nativeSerializer = rawTypeMap.get(fieldType); //If there is a raw type serializer present for this type
                if (nativeSerializer != null) {
                    base.add(field.getKey(), nativeSerializer.serialize(object, field.getValue(), fieldType,this)); //Serialize and insert into document
                    continue;
                } else {
                    try {
                        Object innerObject = field.getValue().get(object);  //Otherwise, we need to keep serializing deeper
                        base.add(field.getKey(), serialize(innerObject));
                    } catch (IllegalAccessException e) {
                        continue;
                    }
                }
            }
            currentLevel = currentLevel.getSuperclass();
        }
        return base;
    }

    public <T extends Object> T deserialize(JsonElement element, Class<T> t){
        GsonSerializableFieldMap<T> fieldMap = classFieldMap.get(t);
        if(fieldMap == null){   //If we didn't find a field cache map
            fieldMap = new GsonSerializableFieldMap<>(t);    //Create a new one
            classFieldMap.put(t,fieldMap);             //And cache it
        }
        T ret = fieldMap.getNew();
        JsonObject base = element.getAsJsonObject();
        Class currentLevel = t;
        while(currentLevel != null) {
            fieldMap = classFieldMap.get(currentLevel);  //Try to find the cached class field map
            if(fieldMap == null){   //If we didn't find a field cache map
                fieldMap = GsonSerializableFieldMap.create(currentLevel);    //Create a new one
                if(fieldMap != null) {
                    classFieldMap.put(currentLevel, fieldMap);             //And cache it
                }
                else
                {
                    break;
                }
            }
            for (Map.Entry<String, Field> field : fieldMap.getSerializableFields().entrySet()) {
                if (!field.getValue().isAccessible()) {
                    try {
                        field.getValue().setAccessible(true);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Could not make field accessible!");
                    }
                }
                Type fieldType = field.getValue().getGenericType();
                GsonWrapperSerializer nativeSerializer = rawTypeMap.get(fieldType);
                if (nativeSerializer != null) {
                    nativeSerializer.deserialize(ret, field.getValue(), fieldType, base.get(field.getKey()),this);
                    continue;
                } else {
                    try {
                        field.getValue().set(ret, deserialize(base.get(field.getKey()), field.getValue().getType()));
                    } catch (IllegalAccessException e) {

                    }
                }
            }
            currentLevel = currentLevel.getSuperclass();
        }
        return ret;

    }

    public <T extends Object> T deserialize(String element, Class<T> t){
        return deserialize(gson.fromJson(element,JsonElement.class),t);
    }

    public <T extends Object> T deserialize(byte[] element, Class<T> t){
        try {
            return deserialize(new String(element,"UTF-8"),t);
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public String serializeToString(Object object){
        return gson.toJson(serialize(object));
    }

    public byte[] serializeToBytes(Object object){
        try {
            return serializeToString(object).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public void registerRawAdapter(GsonWrapperSerializer serializer, Type... types){
        for(Type t : types){
            rawTypeMap.put(t,serializer);
        }
    }
}
