package io.ibj.JLib.file.gson.primative;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.ibj.JLib.file.gson.GsonWrapper;
import io.ibj.JLib.file.gson.GsonWrapperSerializer;
import lombok.SneakyThrows;
import org.joda.time.Instant;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by Joe on 10/7/2014.
 */
public class InstantSerializer implements GsonWrapperSerializer {
    @Override
    @SneakyThrows
    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper) {
        Instant inst = (Instant) field.get(hostObject);
        return new JsonPrimitive(inst.getMillis());
    }

    @Override
    @SneakyThrows
    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper) {
        Long millis = element.getAsLong();
        field.set(hostObject,new Instant(millis));
    }
}
