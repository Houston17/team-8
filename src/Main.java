import static spark.Spark.*;

public class Main {

	public static void main(String[] args) {
		port(666);
		get("/", (req, res) -> "");
	}
}