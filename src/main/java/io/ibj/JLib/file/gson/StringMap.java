package io.ibj.JLib.file.gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe on 10/15/2014.
 */
public class StringMap<T extends Object> extends HashMap<String,T> {

    public StringMap(){
        super();
    }

    public StringMap(int size){
        super(size);
    }

    public StringMap(int size, float loadFactor){
        super(size, loadFactor);
    }

    public StringMap(Map<? extends String, ? extends T> initialMap){
        super(initialMap);
    }
}
