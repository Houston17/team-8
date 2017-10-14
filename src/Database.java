import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	private static final String sqlURL = "jdbc:mysql://54.89.229.2:3306";
	private static final String sqlUser = "root";
	private static final String sqlPass = "root";
	
	private final Connection sql;
	
	public Database() {
		Connection temp = null;
		try {
			temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
			System.out.println("Connected to database");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = temp;
		}
	}
}