package proz.webcalc.client;

import com.google.gson.Gson;

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
	public String calculate (String expression) {
		Gson gson = new Gson();
		Expression exp = new Expression();
		exp.setExpression(expression);
		String url = "";
		
		String expressionString = gson.toJson(exp);
		
		
		
		return "aaa";
	}
}
