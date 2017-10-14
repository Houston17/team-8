import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Scanner;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
//import sun.util.calendar.LocalGregorianCalendar.Date;

public class Main {

	public static void main(String[] args) throws SQLException {
		Spark.port(3002);
		Database data = new Database();
		
		//serve the stylesheet via route because the static server ain't doing well
		Spark.get("/stylesheet.css", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				res.header("Content-Type", "text/css");
				return data("res/stylesheet.css");
			}
		});
		
		/* Begin establishing routes */
		
		Spark.get("/", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return data("res/index.html");
			}
		});
		
		//login
		
		Spark.get("/login", new Route() {

			@Override
			public Object handle(Request req, Response res) throws Exception {
				String user = req.cookie("user");
				String pass = req.cookie("pass");
				
				//if cookies already exist of them logging in, go ahead and direct to user
				
				if (data.validate(user, pass) > 0) {
					res.redirect("/user/" + user);
				}
				return data("res/login.html");
			}
			
		});
		Spark.post("/login", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
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
			}
		});
//		System.out.println("Running on port " + Spark.port());
		//API endpoints
		
		Spark.path("/api", () -> {
			Spark.get("/events", new Route() {
				@Override
				public Object handle(Request req, Response res) throws Exception {
					ResultSet rs = data.runQuery("SELECT * FROM events");
					JsonArray array = new JsonArray();
					if (rs.next()) {
						do {
							JsonObject obj = new JsonObject();
							obj.add("event_id", rs.getInt("event_id"));
							obj.add("start_time", rs.getDate("start_time").getTime());
							obj.add("end_time", rs.getDate("end_time").getTime());
							obj.add("numvolunteers", rs.getInt("numvolunteers"));
							obj.add("description", rs.getString("description"));
							obj.add("created_by", rs.getString("created_by"));
							array.add(obj);
						} while (rs.next());
					}
					return array.toString();
				}
			});
			Spark.get("/users", new Route() {
				@Override
				public Object handle(Request req, Response res) throws Exception {
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
				}
			});
		});
	}
	
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
				return build.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else return null;
	}
	
}