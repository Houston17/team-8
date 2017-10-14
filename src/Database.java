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
	private static final String str = "SELECT user_id FROM users WHERE username = ? AND password = ?";
	
	public Database() {
		Connection temp = null;
		try {
			Properties props = new Properties();
			props.setProperty("user", "bcatdsnhthgflj");
			props.setProperty("password", "5b6fa85be0b90acc5732fce5b84cb281664ca1110e852fa30a86f1bc9c6d4f2c");
			//props.setProperty("ssl","true");
			temp = DriverManager.getConnection("jdbc:postgresql://ec2-23-21-197-231.compute-1.amazonaws.com/dei6bg5krii18t?sslmode=require", props);
			//temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
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