import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import com.eclipsesource.json.JsonArray;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Main {

	public static void main(String[] args) {
		Spark.port(3002);
		Database data = new Database();
		Spark.get("/", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return data("res/index.html");
			}
		});
		//login
		Spark.post("/login", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				Map<String, String> params = req.params();
				if (params.containsKey("user") && params.containsKey("pass")) {
					if (data.validate(params.get("user"), params.get("pass")) > 0) {
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
					ResultSet rs = data.runQuery("SELECT description FROM events");
					JsonArray array = new JsonArray();
					if (rs.isFirst()) {
						do {
							array.add(rs.getString(""));
						} while (rs.next());
					}
					return array.toString();
				}
			});
		});
		/*
		Scanner in = new Scanner(System.in);
		StringBuilder build = new StringBuilder();
		String readIn;
		while (in.hasNextLine()) {
			readIn = in.nextLine();
			if (readIn.equals("")) {
				break;
			}
			build.append(readIn);
		}
		try {
			System.out.println(build.toString());
			data.runQuery(build.toString());
			build.delete(0, build.length());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
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
					build.append(read);
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