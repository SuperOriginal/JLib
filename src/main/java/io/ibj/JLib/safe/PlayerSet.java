package io.ibj.JLib.safe;

import io.ibj.JLib.JLib;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Joe on 7/5/2014.
 */
public class PlayerSet extends HashSet<Player> implements Observer{

    public PlayerSet(){
        super();
        register();
    }

    public PlayerSet(int initialCapacity){
        super(initialCapacity);
        register();
    }

    public PlayerSet(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
        register();
    }

    public PlayerSet(Set<? extends Player> set){
        super(set);
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
