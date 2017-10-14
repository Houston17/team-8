make:
	mvn clean
	mvn install
	mvn compile
	mvn exec:java -Dexec.mainClass="Main"