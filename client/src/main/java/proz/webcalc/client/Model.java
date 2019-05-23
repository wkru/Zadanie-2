package proz.webcalc.client;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

class Expression {
	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}
}

class Result {
	private String result;
	private boolean error;

	public String getResult() {
		return result;
	}

	public boolean isError() {
		return error;
	}
}

public class Model {
	public String calculate(String expression) {
		Gson gson = new Gson();
		Expression exp = new Expression();
		Result result;
		exp.setExpression(expression);
		String json = gson.toJson(exp);

		URI uri = URI.create("http://docker-proz123.apps.us-west-1.online-starter.openshift.com/"); //link valid until July 15, 2019
		Client client = ClientBuilder.newClient();

		WebTarget webTarget = client.target(uri);
		webTarget = webTarget.path("rest").path("calculate");

		try {
			Response response = webTarget.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(json, MediaType.APPLICATION_JSON), Response.class);

			if (response.getStatus() != 200) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Błąd działania programu");
				alert.setHeaderText("Błąd komunikacji z serwerem");
				alert.setContentText(
						"Sprawdź swoje połączenie z Internetem.\nWyczyść pamięć kalkulatora przyciskiem AC.");
				alert.showAndWait();
				return "Błąd.";
			}

			result = gson.fromJson(response.readEntity(String.class), Result.class);

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Błąd działania programu");
			alert.setHeaderText("Błąd komunikacji z serwerem");
			alert.setContentText("Sprawdź swoje połączenie z Internetem.\nWyczyść pamięć kalkulatora przyciskiem AC.");
			alert.showAndWait();
			return "Błąd.";
		}

		if (result.isError()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Błąd działania programu");
			alert.setHeaderText("W trakcie obliczeń napotkano błąd");
			alert.setContentText("Wyczyść pamięć kalkulatora przyciskiem AC.");
			alert.showAndWait();
			return "Błąd.";
		}
		return result.getResult();
	}
}
