package io.ibj.JLib.file.gson.collection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.ibj.JLib.file.gson.GsonWrapper;
import io.ibj.JLib.file.gson.GsonWrapperSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Joe on 10/5/2014.
 */
public class SetSerializer implements GsonWrapperSerializer {


    @Override
    public JsonElement serialize(Object hostObject, Field field, Type type, GsonWrapper wrapper) {
        JsonArray array = new JsonArray();
        Set set = ((Set) hostObject);
        for(Object obj : set){
            array.add(wrapper.serialize(obj));
        }
        return array;
    }

    @Override
    public void deserialize(Object hostObject, Field field, Type type, JsonElement element, GsonWrapper wrapper) {
        JsonArray array = element.getAsJsonArray();
        Set s = new HashSet();
        for(int i = 0; i<array.size(); i++){
            s.add(wrapper.deserialize(array.get(i),field.getType()));
        }
        try {
            field.set(hostObject,s);
        } catch (IllegalAccessException e) {

        }
    }
}
