package io.ibj.JLib.file;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import io.ibj.JLib.JPlug;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;

import java.io.*;

/**
 * Created by Joe on 6/30/2014.
 */
@Deprecated
public class GsonFile extends ConfigFile{
    public GsonFile(File file, String modelResource, JPlug plugin, @NonNull Gson gson) {
        super(file, modelResource, plugin);
        this.gson = gson;
    }

    @Getter
    private Gson gson;

    public <T extends Object> T mold(Class<T> mold, boolean replace) throws IOException {
        InputStream stream = this.read(replace);
        if(stream == null){
            return null;
        }
        @Cleanup Reader r = new InputStreamReader(stream);
        return gson.fromJson(r,mold);
    }

    public void save(Object obj) throws IOException {
        @Cleanup FileWriter fileWriter = new FileWriter(this.getFile());
        @Cleanup JsonWriter jsonWriter = new JsonWriter(fileWriter);
        gson.toJson(obj,obj.getClass(),jsonWriter);
    }

    protected <T extends Object> T moldResource(Class<T> mold) throws IOException {
        InputStream s = getModel();
        if(s == null){
            return null;
        }
        @Cleanup InputStream model = s;
        @Cleanup Reader r = new InputStreamReader(model);
        return gson.fromJson(r,mold);
    }
}
