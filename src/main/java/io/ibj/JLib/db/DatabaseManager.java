package io.ibj.JLib.db;

import java.util.Map;

/**
 * Database manager
 */
public class DatabaseManager {

    private Map<DatabaseConnectionDetails<?>, DatabaseSource<?>> dataStores;

    public <T> T getConnection(DatabaseConnectionDetails<T> details){
        DatabaseSource<T> source = (DatabaseSource<T>) dataStores.get(details);
        if(source != null){
            return source.getConnection();
        }
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
        DatabaseSource source = dataStores.remove(dbSource);
        source.close();
    }

}
