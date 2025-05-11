package handlers.table;

import handlers.HandlerInterface;
import org.json.JSONObject;
import org.json.JSONArray;

import java.sql.*;

/**
 * Handles all table.* WebSocket actions.
 */
public class TableHandler implements HandlerInterface {

	/**
	 * Default constructor.
	 */
	public TableHandler() {}

	@Override
	public String handle(String action, JSONObject data) {
		switch (action) {
			case "table.get":
				return getTableList();
			default:
				throw new IllegalArgumentException("Unknown table action: " + action);
		}
	}

	/**
	 * Queries the database for a list of table names.
	 *
	 * @return JSON string containing an array of table names
	 */
	private String getTableList() {
		JSONArray tables = new JSONArray();
		try (
			Connection conn = DriverManager.getConnection("jdbc:h2:./db/sme");
			ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})
		) {
			while (rs.next()) {
				tables.put(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			JSONObject error = new JSONObject();
			error.put("error", "DB error: " + e.getMessage());
			return error.toString();
		}

		JSONObject result = new JSONObject();
		result.put("tables", tables);
		return result.toString();
	}
}
