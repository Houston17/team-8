import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
	
	//should be private
	public final Connection sql;
	private static final String str = "SELECT user_id, is_admin FROM users WHERE username = ? AND password = ?";
import java.sql.SQLException;

public class Database {

	private static final String sqlURL = "jdbc:mysql://54.89.229.2:3306";
	private static final String sqlUser = "root";
	private static final String sqlPass = "root";
	
	private final Connection sql;
	
	public Database() {
		Connection temp = null;
		try {
			Properties props = new Properties();
			props.setProperty("user", System.getenv("DATABASE_USER"));
			props.setProperty("password", System.getenv("DATABASE_PASS"));
			//props.setProperty("ssl","true");
			temp = DriverManager.getConnection(System.getenv("DATABASE_URL"), props);
			//temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
			temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
			System.out.println("Connected to database");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = temp;
		}
	}
	
	/**
	 * Run a SQL query and return the results.
	 * @param query
	 * @return ResultSet of the results.
	 * @throws SQLException
	 */
	public ResultSet runQuery(String query) throws SQLException {
		Statement st = sql.createStatement();
		return st.executeQuery(query);
	}
	
	/**
	 * Run SQL commands that does not return data
	 * @param query
	 * @return int representing the successfulness - 0 if failed.
	 * @throws SQLException
	 */
	public int runUpdateQuery(String query) throws SQLException {
		Statement st = sql.createStatement();
		return st.executeUpdate(query);
	}
	
	/**
	 * Validates whether or not the user or pass is valid
	 * @param user
	 * @param pass
	 * @return user_id for success, 0 for no auth, -1 for server error
	 */
	
	public int validate(String user, String pass) {
		try {
			System.out.println(user + " " + pass);
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
	
	public boolean validateAdmin(String user, String pass) {
		try {
			System.out.println(user + " " + pass);
			PreparedStatement st = sql.prepareStatement(str);
			st.setString(1, user);
			st.setString(2, pass);
			ResultSet rs = st.executeQuery();
			System.out.println("Ran");
			if (!rs.next())
				return false;
			String isAdmin = rs.getString("is_admin");
			System.out.println(isAdmin);
			return "Y".equals(isAdmin);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
