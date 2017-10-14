import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	
	private static final String sqlUrl = "";
	
	private final Connection conn;
	
	public Database() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(sqlUrl);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.conn = conn;
		}
	}
	
	
	
}
