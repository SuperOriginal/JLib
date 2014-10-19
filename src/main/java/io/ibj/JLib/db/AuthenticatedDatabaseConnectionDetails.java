package io.ibj.JLib.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by Joe on 10/19/2014.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedDatabaseConnectionDetails<T extends Object> extends DatabaseConnectionDetails<T> {
    public AuthenticatedDatabaseConnectionDetails(DatabaseType type, Class<T> connectionType, String host, int port, String username, String password) {
        super(type, connectionType, host, port);
        this.username = username;
        this.password = password;
    }
    public AuthenticatedDatabaseConnectionDetails(DatabaseType type, Class<T> connectionType, String host, int port, String keyspace, String username, String password) {
        super(type, connectionType, host, port, keyspace);
        this.username = username;
        this.password = password;
    }

    @Getter
    private String username;
    @Getter
    private String password;
}
