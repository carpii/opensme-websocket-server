package util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

/**
 * Utility class for executing common database queries.
 */
public class DatabaseHelper {

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
			Connection conn = DriverManager.getConnection("jdbc:h2:./db/sme");
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
}
