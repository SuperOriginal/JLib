package io.ibj.JLib.gui;

/**
 * Created by Joe on 6/30/2014.
 */
public enum PageCloseCause {
    OVERWRITTEN,    //Called when another page is loaded in the place of the current page
    APICLOSE,       //Called when a PageHolder is .close()d, not when being overwritten
    EVENTCLOSE      //Called when a PageHolder's inventory is closed through an external close, notified by an event
}
