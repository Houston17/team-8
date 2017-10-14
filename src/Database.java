import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

	private static final String sqlURL = "jdbc:postgres://ec2-23-21-189-181.compute-1.amazonaws.com:5432/dfiapi0mdogp1g";
	private static final String sqlUser = "mvdwiysxpajrkk";
	private static final String sqlPass = "b4c48e991b2834cd698e238618abd4a74dce314b14cf43a1cb80e309dcb3ae84";
	
	private final Connection sql;
	
	public Database() {
		Connection temp = null;
		try {
			temp = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
			System.out.println("Connected to database");
			java.sql.Statement s = temp.createStatement();
			ResultSet rs = s.executeQuery("SELECT name_test FROM USERS");
			System.out.println(rs.getInt("name_test"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sql = temp;
		}
	}
}