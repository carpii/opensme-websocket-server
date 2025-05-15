package backend.handlers;

import backend.handlers.HandlerInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import backend.util.DatabaseHelper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all table.* WebSocket actions.
 */
public class TableHandler implements HandlerInterface {

    /**
     * Default constructor.
     */
    public TableHandler() {}

    @Override
    public Object handle(String action, JSONObject data) {
        switch (action) {
            case "table.list":
                return listTables(); // Returns a JSONArray of table names
            default:
                throw new IllegalArgumentException("Unknown table action: " + action);
        }
    }

    /**
     * Lists all database tables.
     *
     * @return JSONArray of table names
     */
    private JSONArray listTables() {
        JSONArray tables = new JSONArray();
        try (
            Connection conn = DatabaseHelper.getConnection();
            ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})
        ) {
            while (rs.next()) {
                tables.put(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }
        return tables;
    }
}
