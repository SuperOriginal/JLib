package io.ibj.JLib.file.gson.primative;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.ibj.JLib.file.gson.GsonWrapper;
import io.ibj.JLib.file.gson.GsonWrapperSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by Joe on 10/1/2014.
 */
public class LongSerializer implements GsonWrapperSerializer {
    @Override
    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper) {
        try {
            return new JsonPrimitive(field.getLong(hostObject));
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Override
    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper) {
        try {
            field.setLong(hostObject,element.getAsLong());
        } catch (IllegalAccessException e) {

        }
    }
}
