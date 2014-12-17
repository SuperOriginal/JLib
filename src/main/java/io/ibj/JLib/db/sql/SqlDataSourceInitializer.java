package io.ibj.JLib.db.sql;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.ibj.JLib.JLib;
import io.ibj.JLib.db.AuthenticatedDatabaseConnectionDetails;
import io.ibj.JLib.db.DataSourceInitializer;
import io.ibj.JLib.db.DatabaseConnectionDetails;
import io.ibj.JLib.db.DatabaseSource;
import lombok.SneakyThrows;

import java.sql.Connection;

/**
 * Created by Joe on 10/19/2014.
 */
public class SqlDataSourceInitializer implements DataSourceInitializer<Connection> {
    @Override
    @SneakyThrows
    public DatabaseSource<Connection> createNewDatasource(DatabaseConnectionDetails<Connection> details) {
        String connectionUrl = "jdbc:mysql://" + details.getHost() + ":" + details.getPort() + "/" + details.getKeyspace();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionUrl);
        JLib.getI().getLogger().info("Creating new Hikari connection pool connected to "+connectionUrl+".");
        if(details instanceof AuthenticatedDatabaseConnectionDetails){
            final AuthenticatedDatabaseConnectionDetails authenticatedDetails = (AuthenticatedDatabaseConnectionDetails) details;
            JLib.getI().getLogger().info("With authentication: Username: "+authenticatedDetails.getUsername()+" Password: "+authenticatedDetails.getPassword());
            config.setUsername(authenticatedDetails.getUsername());
            config.setPassword(authenticatedDetails.getPassword());
        }

        return new SqlDataSource(new HikariDataSource(config));
    }
}
