package io.ibj.JLib.gui;

import org.bukkit.entity.Player;

import java.util.Observable;

/**
 * Created by Joe on 6/30/2014.
 */
public abstract class Page extends Observable{

    protected Page(String name, Integer size){
        this.name = name;
        if(size % 9 != 0) throw new IllegalArgumentException("The size of the inventory must be a multiple of 9.");
        this.size = size;
    }

    private final String name;
    private final Integer size;

    public String getName()
    {
        return name;
    }
    public Integer getSize()
    {
        return size;
    }

    protected void updateButton(Integer slot){
        this.notifyObservers(slot);
    }

    public abstract Button[] getButtons(Player observer);
    public abstract Button getButton(Player observer, Integer slot);

    public abstract void onClose(PageHolder holder, PageCloseCause cause);
}
