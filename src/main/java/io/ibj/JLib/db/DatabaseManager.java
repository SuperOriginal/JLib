package io.ibj.JLib.db;

import io.ibj.JLib.JLib;

import java.util.HashMap;
import java.util.Map;

/**
 * Database manager
 */
public class DatabaseManager {

    private Map<DatabaseConnectionDetails<?>, DatabaseSource<?>> dataStores = new HashMap<>();

    public <T> T getConnection(DatabaseConnectionDetails<T> details){
        DatabaseSource<T> source = (DatabaseSource<T>) dataStores.get(details);
        if(source != null){
            return source.getConnection();
        }
        JLib.getI().getLogger().info("Initializing new DatabaseConnection...");
        source = details.getDatabaseType().getInitializer().createNewDatasource(details);
        dataStores.put(details,source);
        return source.getConnection();
    }

    public void cleanUpAll(){
        for(DatabaseSource source : dataStores.values()){
            source.close();
        }
        dataStores.clear();
    }

    public void cleanUpDatabaseSource(DatabaseConnectionDetails dbSource){
        DatabaseSource<?> source = dataStores.remove(dbSource);
        source.close();
    }

}
