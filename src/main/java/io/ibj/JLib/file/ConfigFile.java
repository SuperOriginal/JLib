package io.ibj.JLib.file;

import io.ibj.JLib.JPlug;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;

import java.io.*;

/**
 * Created by Joe on 6/30/2014.
 */
@Deprecated
public class ConfigFile {

    public ConfigFile(@NonNull File f, String modelResource, @NonNull JPlug plugin){
        this.file = f;
        this.modelResource = modelResource;
        this.plugin = plugin;
    }

    @Getter
    private File file;

    @Getter
    private String modelResource;

    @Getter
    private JPlug plugin;

    public void directoryFill(){
        if(!file.isDirectory()) {
            file.getParentFile().mkdirs();
        }
    }

    public InputStream read(boolean replace) throws IOException {
        if(file.exists()){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                //Will not throw
            }
        }
        else
        {
            if(replace) {
                if (modelResource != null) {
                    directoryFill();
                    file.createNewFile();
                    @Cleanup InputStream in = getModel();
                    @Cleanup OutputStream out = new FileOutputStream(file);
                    byte[] buf = new byte['?'];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                    return new FileInputStream(file);
                }
            }
        }
        return null;
    }
    public OutputStream write() throws IOException {
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return new FileOutputStream(file);
    }

    public InputStream getModel(){
        if(modelResource == null){
            return null;
        }
        return plugin.getResource(modelResource);
    }
}
