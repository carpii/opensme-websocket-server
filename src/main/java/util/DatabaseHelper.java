import java.sql.*;

public class DatabaseHelper {

	private static final String DB_PATH = "jdbc:h2:./db/sme";

	public static String getTableList() {
		StringBuilder result = new StringBuilder();
		try (Connection conn = DriverManager.getConnection(DB_PATH);
			ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
			while (rs.next()) {
				result.append(rs.getString("TABLE_NAME")).append("\n");
			}
		} catch (SQLException e) {
			return "DB error: " + e.getMessage();
		}
		return result.length() > 0 ? result.toString().trim() : "(no tables)";
	}
}
