package io.ibj.JLib.db;

/**
 * Created by Joe on 10/19/2014.
 */
public interface DatabaseSource<T extends Object> {

    public T getConnection();

    public void close();

}
