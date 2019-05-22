module proz.webcalc.client {
	requires javafx.controls;
	requires javafx.fxml;
	requires jdk.jshell;
	requires com.google.gson;
	
	requires transitive javafx.graphics;
	
	opens proz.webcalc.client to javafx.fxml;
	exports proz.webcalc.client;
}