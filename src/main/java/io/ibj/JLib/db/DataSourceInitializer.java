package io.ibj.JLib.db;

/**
 * Created by Joe on 10/19/2014.
 */
public interface DataSourceInitializer<T> {
    DatabaseSource<T> createNewDatasource(DatabaseConnectionDetails<T> details);
}
