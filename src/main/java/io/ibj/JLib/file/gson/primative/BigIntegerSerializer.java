package io.ibj.JLib.file.gson.primative;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.ibj.JLib.file.gson.GsonWrapper;
import io.ibj.JLib.file.gson.GsonWrapperSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by Joe on 10/3/2014.
 */
public class BigIntegerSerializer implements GsonWrapperSerializer {
    @Override
    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper) {
        try {
            return new JsonPrimitive(((Number) field.get(hostObject)));
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper) {
        try {
            field.set(hostObject,element.getAsBigInteger());
        } catch (IllegalAccessException e) {

        }
    }
}
