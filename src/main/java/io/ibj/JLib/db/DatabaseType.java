package io.ibj.JLib.db;

import io.ibj.JLib.db.sql.SqlDataSourceInitializer;

/**
 * Created by Joe on 10/19/2014.
 */
public enum DatabaseType {

    SQL(new SqlDataSourceInitializer()),
    MONGO(null),
    REDIS(null),
    RABBITMQ(null),
    CASSANDRA(null);

    DatabaseType(DataSourceInitializer dataSourceInitializer){
        this.initializer = dataSourceInitializer;
    }

    DataSourceInitializer initializer;

    public DataSourceInitializer getInitializer(){
        return initializer;
    }

}
