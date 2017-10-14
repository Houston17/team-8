import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import spark.Request;
import spark.Response;
import spark.Spark;
//import sun.util.calendar.LocalGregorianCalendar.Date;

import spark.Spark;

public class Main {

	public static void main(String[] args) throws SQLException {
		Spark.port(3003);
		Database data = new Database();
		
		//serve the stylesheet via route because the static server ain't doing well
		Spark.get("/stylesheet.css", (Request req, Response res) -> {
			res.header("Content-Type", "text/css");
			return data("res/stylesheet.css");
		});
		
		/* Begin establishing routes */
		
		/* Landing page route */
		Spark.get("/", (Request req, Response res) -> {
				return data("res/index.html");
		});
		
		/* GET request for login. Called for users accessing the login page. 
		 * Will automatically redirect them to the management page if their cookies is aight */
		Spark.get("/login", (Request req, Response res) -> {
			String user = req.cookie("user");
			String pass = req.cookie("pass");
			// if cookies already exist of them logging in, go ahead and direct to user
			if (data.validate(user, pass) > 0) {
				res.redirect("/user/" + user);
			}
			// otherwise just serve them the login page
			return data("res/login.html");
		});
		/* POST request for login. When user submits, checks for validity of credentials. */
		Spark.post("/login", (Request req, Response res) -> {
			Map<String, String> params = req.params();
			if (params.containsKey("user") && params.containsKey("pass")) {
				String user = params.get("user");
				String pass = params.get("pass");
				if (data.validate(params.get("user"), params.get("pass")) > 0) {
					res.cookie("user", user);
					res.cookie("pass", pass);
					res.redirect("/user/" + params.get("user"));
				}
			}
			return data("res/login.html");
		});

		//API endpoints, accessed by /api/..
		
		Spark.path("/api", () -> {
			/* Get a list of the events. Returns a JSON array of event objects. */
			Spark.get("/events", (Request req, Response res) -> {
				ResultSet rs = data.runQuery("SELECT * FROM events");
				JsonArray array = new JsonArray();
				if (rs.next()) {
					do {
						JsonObject obj = new JsonObject();
						obj.add("event_id", rs.getInt("event_id"));
						obj.add("month_dat_year", rs.getString("month_day_year"));
						obj.add("start_end_time", rs.getString("start_end_time"));
						obj.add("numvolunteers", rs.getInt("numvolunteers"));
						obj.add("description", rs.getString("description"));
						obj.add("created_by", rs.getString("created_by"));
						array.add(obj);
					} while (rs.next());
				}
				return array.toString();
			});
			/* Get a list of the users. Returns a JSON array of event objects. */
			Spark.get("/users", (Request req, Response res) -> {
				ResultSet rs = data.runQuery("SELECT user_id, name, username, phone, numhours, numevents, is_admin FROM users");
				JsonArray array = new JsonArray();
				if (rs.next()) {
					do {
						JsonObject obj = new JsonObject();
						obj.add("user_id", rs.getInt("user_id"));
						obj.add("name", rs.getDate("name").getTime());
						obj.add("username", rs.getDate("username").getTime());
						obj.add("numvolunteers", rs.getInt("numvolunteers"));
						obj.add("description", rs.getString("description"));
						obj.add("created_by", rs.getString("created_by"));
						array.add(obj);
					} while (rs.next());
				}
				return array.toString();
			});
			Spark.post("/events", (Request req, Response res) -> {
				try {
					String adminEmailAddress = req.queryParams("adminEmailAddress");
					String adminPassword = req.queryParams("adminPassword");
					if (!data.validateAdmin(adminEmailAddress, adminPassword)) {
						System.out.println("Guess that isn't an admin");
						res.redirect("/");
						return null;
					}
					String month_day_year = req.queryParams("month_day_year");
					String start_end_time = req.queryParams("start_end_time");
					String location = req.queryParams("location");
					int numvolunteers = Integer.parseInt(req.queryParams("numvolunteers"));
					int numhours = Integer.parseInt(req.queryParams("numhours"));
					String description = req.queryParams("description");
					String created_by = req.queryParams("created_by");
					PreparedStatement ps = data.sql.prepareStatement("INSERT INTO events (month_day_year , start_end_time , location, numvolunteers, numhours, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)");
					ps.setString(1, month_day_year);
					ps.setString(2, start_end_time);
					ps.setString(3, location);
					ps.setInt(4, numvolunteers);
					ps.setInt(5, numhours);
					ps.setString(6, description);
					ps.setString(7, created_by);
					ps.executeUpdate(); //should catch this
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				res.redirect("/");
				return null;
			});
		});
		/* Creating new events */
		
	}
	
	/**
	 * Read in text from a file in a relative path
	 * @param path
	 * @return the data read in
	 */
	public static String data(String path) {
		File f = new File(path);
		if (f.exists()) {
			try {
				FileReader fread = new FileReader(f);
				BufferedReader buff = new BufferedReader(fread);
				StringBuilder build = new StringBuilder();
				String read;
				while ((read = buff.readLine()) != null) {
					build.append(read + "\n");
				}
				buff.close();
				return build.toString();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
	public static void main(String[] args) {
		get("/", (req, res) -> "hey nerds");
		System.out.println("Running on port " + Spark.port());
		Database data = new Database();
	}
	
}
