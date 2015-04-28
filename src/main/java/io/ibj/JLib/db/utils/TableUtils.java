package io.ibj.JLib.db.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joe 3/1/2015
 */
public class TableUtils {

    public static List<String> getColumnNames(String table, Connection connection) throws SQLException {
        ResultSet set = connection.createStatement().executeQuery(String.format("show columns from `%s`;", table));
        List<String> ret = new ArrayList<>();
        while(set.next()){
            ret.add(set.getString("Field"));
        }
        return ret;
    }

    public static void insertColumnIntoTable(String table, String columnName, String columnType, String positionModifier, Connection connection) throws SQLException {
        connection.createStatement().execute(String.format("alter table `%s` add `%s` %s %s;", table, columnName, columnType, positionModifier));
    }

}
