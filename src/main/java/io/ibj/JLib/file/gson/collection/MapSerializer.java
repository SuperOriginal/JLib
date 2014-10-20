package io.ibj.JLib.file.gson.collection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.ibj.JLib.file.gson.GenericDeserializeAs;
import io.ibj.JLib.file.gson.GsonWrapper;
import io.ibj.JLib.file.gson.GsonWrapperSerializer;
import io.ibj.JLib.file.gson.StringMap;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Joe on 10/15/2014.
 */
public class MapSerializer implements GsonWrapperSerializer {
    @Override
    @SneakyThrows
    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper) {
        StringMap stringMap = (StringMap) field.get(hostObject);
        JsonObject ret = new JsonObject();
        for(Object entry : stringMap.entrySet()){
            Map.Entry<String, Object> entry1 = ((Map.Entry<String, Object>) entry);
            ret.add(entry1.getKey(), wrapper.serialize(entry1.getValue()));
        }
        return ret;
    }

    @Override
    @SneakyThrows
    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper) {
        if(!field.isAnnotationPresent(GenericDeserializeAs.class)){
            throw new IllegalStateException("This StringMap must be annotated with a GenericDeserializeAs.");
        }
        Class<? extends Object> clazz = field.getAnnotation(GenericDeserializeAs.class).value();
        JsonObject object = element.getAsJsonObject();
        StringMap stringMap = new StringMap<>();
        for(Map.Entry<String, JsonElement> entry : object.entrySet()){
            stringMap.put(entry.getKey(), wrapper.deserialize(entry.getValue(), clazz));
        }

        field.set(hostObject,stringMap);

    }
}
