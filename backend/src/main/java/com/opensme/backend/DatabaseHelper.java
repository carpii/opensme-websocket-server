package com.opensme.backend;

import com.opensme.backend.models.Portfolio;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Utility class for executing common database queries.
 */
public class DatabaseHelper {

    /** The JDBC URL for connecting to the H2 database */
    private static final String DB_URL = "jdbc:h2:./db/sme";
    
    /** The database user name */
    private static final String DB_USER = "";
    
    /** The database password */
    private static final String DB_PASSWORD = "";

    /**
     * Default constructor.
     */
    public DatabaseHelper() {}

    /**
     * Provides a connection to the database.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Returns all rows from the given table as a JSON array of objects.
     *
     * @param tableName name of the table to query
     * @return JSONArray of rows
     * @throws SQLException if the query fails
     */
    public static JSONArray fetchAllRows(String tableName) throws SQLException {
        JSONArray results = new JSONArray();

        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName);
            ResultSet rs = stmt.executeQuery()
        ) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                JSONObject row = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                results.put(row);
            }
        }

        return results;
    }

    /**
     * Executes a query with the given SQL and parameters, returning the results as a JSON array.
     *
     * @param sql the SQL query to execute
     * @param params the parameters to bind to the query
     * @return JSONArray of rows
     * @throws SQLException if the query fails
     */
    public static JSONArray executeQuery(String sql, String... params) throws SQLException {
        JSONArray array = new JSONArray();
        try (
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        String columnName = meta.getColumnName(i).toLowerCase(); // Convert to lowercase
                        obj.put(columnName, rs.getObject(i));
                    }
                    array.put(obj);
                }
            }
        }
        return array;
    }
}
