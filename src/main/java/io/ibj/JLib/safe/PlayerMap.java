package io.ibj.JLib.safe;

import io.ibj.JLib.JLib;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joe on 7/5/2014.
 */
public class PlayerMap<T extends Object> extends HashMap<Player, T> implements Observer{
    public PlayerMap(){
        super();
        register();
    }

    public PlayerMap(int initialCapacity){
        super(initialCapacity);
        register();
    }

    public PlayerMap(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
        register();
    }

    public PlayerMap(Map<? extends Player, ? extends T> map){
        super(map);
        register();
    }

    private void register(){
        JLib.getI().getClearer().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        remove(arg);
    }
}
