import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Main {

	public static void main(String[] args) {
		/*
		Spark.port(3001);
		get("/", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return data("res/index.html");
			}
		});
		//login
		get("/login", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return data("res/login.html");
			}
		});
		Spark.post("/login", new Route() {
			@Override
			public Object handle(Request req, Response res) throws Exception {
				return data("res/login.html");
			}
		});
		*/
//		System.out.println("Running on port " + Spark.port());
		Database data = new Database();
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