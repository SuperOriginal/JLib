package io.ibj.JLib.db.sql;

import com.mchange.v2.c3p0.DataSources;
import io.ibj.JLib.JLib;
import io.ibj.JLib.db.AuthenticatedDatabaseConnectionDetails;
import io.ibj.JLib.db.DataSourceInitializer;
import io.ibj.JLib.db.DatabaseConnectionDetails;
import io.ibj.JLib.db.DatabaseSource;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by Joe on 10/19/2014.
 */
public class SqlDataSourceInitializer implements DataSourceInitializer<Connection> {
    @Override
    @SneakyThrows
    public DatabaseSource<Connection> createNewDatasource(DatabaseConnectionDetails<Connection> details) {
        DataSource unpooled_ds;
        String connectionUrl = "jdbc:mysql://" + details.getHost() + ":" + details.getPort() + "/" + details.getKeyspace();
        JLib.getI().getLogger().info("Creating new C3P0 connection pool connected to "+connectionUrl+".");
        if(details instanceof AuthenticatedDatabaseConnectionDetails){
            final AuthenticatedDatabaseConnectionDetails authenticatedDetails = (AuthenticatedDatabaseConnectionDetails) details;
            unpooled_ds = DataSources.unpooledDataSource(connectionUrl, authenticatedDetails.getUsername(), authenticatedDetails.getPassword());
            JLib.getI().getLogger().info("With authentication: Username: "+authenticatedDetails.getUsername()+" Password: "+authenticatedDetails.getPassword());
        }
        else
        {
            unpooled_ds = DataSources.unpooledDataSource(connectionUrl);
        }

        return new SqlDataSource(DataSources.pooledDataSource(unpooled_ds));
    }
}
