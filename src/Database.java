import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
	
	public final Connection sql;
	
	public Database() {
		Connection temp = null;
		try {
			Properties props = new Properties();
			props.setProperty("user", System.getenv("DATABASE_USER"));
			props.setProperty("password", System.getenv("DATABASE_PASS"));
			//props.setProperty("ssl","true");
			temp = DriverManager.getConnection(System.getenv("DATABASE_URL"), props);
			//temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
			System.out.println("Connected to database");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = temp;
		}
	}
	
	public ResultSet runQuery(String query) throws SQLException {
		Statement st = sql.createStatement();
		return st.executeQuery(query);
	}
	
	public void runPreparedQuery(String query) throws SQLException {
		System.out.println("Running: " + query);
		PreparedStatement st = sql.prepareStatement(query);
		st.executeQuery();
	}
	
	/**
	 * Validates whether or not the user or pass is valid
	 * @param user
	 * @param pass
	 * @return user_id for success, 0 for no auth, -1 for server error
	 */
	private static final String str = "SELECT user_id FROM users WHERE username = ? AND password = ?";
	
	public int validate(String user, String pass) {
		try {
			PreparedStatement st = sql.prepareStatement(str);
			st.setString(1, user);
			st.setString(2, pass);
			ResultSet rs = st.executeQuery();
			if (!rs.next())
				return 0;
			int user_id = rs.getInt("user_id");
			return user_id;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}