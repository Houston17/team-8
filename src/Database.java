import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
	
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
			System.out.println("Connected to database");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = temp;
		}
	}
}