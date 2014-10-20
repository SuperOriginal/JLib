package io.ibj.JLib.file.gson;

import com.google.gson.JsonElement;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by Joe on 10/1/2014.
 */
public interface GsonWrapperSerializer {

    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper);

    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper);

}
