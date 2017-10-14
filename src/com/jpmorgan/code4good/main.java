package com..code4good;

import static spark.Spark.*;

public class main {

	public static void main(String[] args) {
		port(666);
		get("/", (req, res) -> "hey nerds");
	}
}