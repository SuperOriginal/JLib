package io.ibj.JLib.db;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Joe on 10/19/2014.
 */
@ToString
@EqualsAndHashCode
public class DatabaseConnectionDetails<T extends Object> {

    public DatabaseConnectionDetails(DatabaseType type, Class<T> connectionType, String host, int port){
        this(type, connectionType,host,port, "");
    }

    public DatabaseConnectionDetails(DatabaseType type, Class<T> connectionType, String host, int port, String keyspace){
        this.connectionType = connectionType;
        this.host = host;
        this.keyspace = keyspace;
    }

    DatabaseType type;
    Class<T> connectionType;
    String host;
    String keyspace;
    int port;

    public DatabaseType getDatabaseType(){
        return type;
    }

    public Class<T> getConnectionType(){
        return connectionType;
    }

    public String getHost(){
        return host;
    }

    public String getKeyspace(){
        return keyspace;
    }
    public int getPort(){
        return port;
    }
}
