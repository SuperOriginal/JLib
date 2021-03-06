package io.ibj.JLib.db.sql;

import com.zaxxer.hikari.HikariDataSource;
import io.ibj.JLib.db.DatabaseSource;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by Joe on 10/19/2014.
 */
public class SqlDataSource implements DatabaseSource<Connection> {

    public SqlDataSource(DataSource source){
        this.source = source;
    }

    private DataSource source;

    @Override
    @SneakyThrows
    public Connection getConnection() {
        return source.getConnection();
    }

    @Override
    @SneakyThrows
    public void close() {
        if(source instanceof HikariDataSource){
            ((HikariDataSource) source).close();
        }
    }
}
