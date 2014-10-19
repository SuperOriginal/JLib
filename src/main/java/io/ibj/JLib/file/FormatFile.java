package io.ibj.JLib.file;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.ibj.JLib.JPlug;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 6/30/2014.
 */
public class FormatFile extends JsonFile {
    public FormatFile(@NonNull File file, String modelResource, @NonNull JPlug plugin, @NonNull Gson gson) {
        super(file, modelResource, plugin, gson);
    }

    HashMap<String, String> formatsMap = new HashMap<>();

    public void loadFormats() throws IOException {
        JsonObject obj = this.mold(JsonObject.class,true);
        for(Map.Entry<String, JsonElement> elements : obj.entrySet()){
            formatsMap.put(elements.getKey(),elements.getValue().getAsString());
        }
        JsonObject model = this.moldResource(JsonObject.class);
        boolean mustOverwrite = false;
        for(Map.Entry<String, JsonElement> elements : model.entrySet()){
            if(!formatsMap.containsKey(elements.getKey())){
                formatsMap.put(elements.getKey(),elements.getValue().getAsString());
                mustOverwrite = true;
            }
        }
        if(mustOverwrite){
            JsonObject obj1 = new JsonObject();
            for(Map.Entry<String,String> s : formatsMap.entrySet()){
                obj1.addProperty(s.getKey(),s.getValue());
            }
            save(obj1);
        }
    }

    public String getFormat(String key){
        return formatsMap.get(key);
    }


}
