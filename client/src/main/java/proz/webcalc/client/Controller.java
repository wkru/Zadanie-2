package proz.webcalc.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML
	private Button zero;

	@FXML
	private Button tripleZero;

	@FXML
	private Button decimalSign;

	@FXML
	private Button equals;

	@FXML
	private Button add;

	@FXML
	private Button subtract;

	@FXML
	private Button multiply;

	@FXML
	private Button divide;

	@FXML
	private Button one;

	@FXML
	private Button two;

	@FXML
	private Button three;

	@FXML
	private Button four;

	@FXML
	private Button five;

	@FXML
	private Button six;

	@FXML
	private Button seven;

	@FXML
	private Button eight;

	@FXML
	private Button nine;

	@FXML
	private Button allCancel;

	@FXML
	private Button backspace;

	@FXML
	private Button invertSign;

	@FXML
	private Button sqrt;

	@FXML
	private Button power;

	@FXML
	private Button naturalLogarithm;

	@FXML
	private Button clearEntry;

	@FXML
	private TextFlow display;

	@FXML
	private Text displayText;

	private String calcInput = "";
	private String pendingOperation = "";
	private boolean finalResultShown = false;
	private boolean nonNumericOutputShown = false;
	private boolean usePower = false;
	private Model model = new Model();

	@FXML
	public void initialize() {
		zero.setOnAction(e -> {
			digitAppend("0");
		});

		tripleZero.setOnAction(e -> {
			digitAppend("000");
		});

		one.setOnAction(e -> {
			digitAppend("1");
		});

		two.setOnAction(e -> {
			digitAppend("2");
		});

		three.setOnAction(e -> {
			digitAppend("3");
		});

		four.setOnAction(e -> {
			digitAppend("4");
		});

		five.setOnAction(e -> {
			digitAppend("5");
		});

		six.setOnAction(e -> {
			digitAppend("6");
		});

		seven.setOnAction(e -> {
			digitAppend("7");
		});

		eight.setOnAction(e -> {
			digitAppend("8");
		});

		nine.setOnAction(e -> {
			digitAppend("9");
		});

		decimalSign.setOnAction(e -> {
			digitAppend(".");
		});

		add.setOnAction(e -> {
			mathOperationClicked("+");
		});

		subtract.setOnAction(e -> {
			mathOperationClicked("-");
		});

		multiply.setOnAction(e -> {
			mathOperationClicked("*");
		});

		divide.setOnAction(e -> {
			mathOperationClicked("/");
		});

		power.setOnAction(e -> {
			mathOperationClicked("^");
		});

		sqrt.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (finalResultShown)
				finalResultShown = false;
			String oldCalcInput = calcInput;
			calcInput = "Math.pow(" + displayText.getText() + ", 0.5)";
			calculate();
			calcInput = oldCalcInput;
		});

		naturalLogarithm.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (finalResultShown)
				finalResultShown = false;
			String oldCalcInput = calcInput;
			calcInput = "Math.log(" + displayText.getText() + ")";
			calculate();
			calcInput = oldCalcInput;
		});

		backspace.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (displayText.getText().equals("0"))
				return;
			if (finalResultShown)
				return;
			if (displayText.getText().length() == 1) {
				displayText.setText("0");
				return;
			}

			displayText.setText(displayText.getText().substring(0, displayText.getText().length() - 1));
		});

		clearEntry.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (displayText.getText().equals("0"))
				return;
			if (finalResultShown)
				return;

			displayText.setText("0");
		});

		allCancel.setOnAction(e -> {
			displayText.setText("0");
			finalResultShown = false;
			pendingOperation = "";
			calcInput = "";
			nonNumericOutputShown = false;
			usePower = false;
		});

		invertSign.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (displayText.getText().equals("0"))
				return;
			if (displayText.getText().startsWith("-"))
				displayText.setText(displayText.getText().substring(1, displayText.getText().length()));
			else
				displayText.setText("-" + displayText.getText());
		});

		equals.setOnAction(e -> {
			if (nonNumericOutputShown)
				return;
			if (!calcInput.isBlank() && calcInput.substring(calcInput.length() - 1, calcInput.length()).equals("/")
					&& displayText.getText().equals("0")) {
				showErrorWindow("Nie można dzielić przez zero", "Popraw swoje obliczenia.");
				return;
			}
			if (usePower) {
				usePower = false;
				calcInput = calcInput + ", " + displayText.getText() + ")";
			} else
				calcInput = calcInput.concat(displayText.getText());
			calculate();
			finalResultShown = true;
			pendingOperation = "";
		});
	}

	private void showErrorWindow(String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Błąd działania programu");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void showWarningWindow(String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Uwaga");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void calculate() {
		String result = model.calculate(calcInput);
		if (!result.isBlank() && result.length() > 2
				&& result.substring(result.length() - 2, result.length()).equals(".0"))
			result = result.substring(0, result.length() - 2);
		displayText.setText(result);
		if (displayText.getText().equals("Błąd."))
			nonNumericOutputShown = true;
		if (displayText.getText().equals("Infinity")) {
			showWarningWindow("Wynikiem obliczeń była nieskończoność", "Skasuj wynik obliczeń klawiszem AC.");
			nonNumericOutputShown = true;
		}
		if (displayText.getText().equals("NaN")) {
			showErrorWindow("Wynikiem obliczeń była nie-liczba", "Skasuj wynik obliczeń klawiszem AC.");
			nonNumericOutputShown = true;
		}
		calcInput = "";
	}

	private void digitAppend(String digit) {
		if (nonNumericOutputShown)
			return;
		if (finalResultShown) {
			displayText.setText("0");
			finalResultShown = false;
		}
		if (!pendingOperation.isBlank()) {
			if (pendingOperation.equals("^")) {
				calcInput = "Math.pow(" + displayText.getText();
				usePower = true;
			} else if (pendingOperation.equals("/")) {
				if (!displayText.getText().contains("."))
					calcInput = displayText.getText() + "." + pendingOperation;
				else
					calcInput = displayText.getText() + pendingOperation;
			} else
				calcInput = displayText.getText() + pendingOperation;
			pendingOperation = "";
			if (digit.equals("."))
				displayText.setText("0.");
			else
				displayText.setText(digit);
			return;
		}
		if (displayText.getText().length() + digit.length() > 16) {
			showWarningWindow("Przekroczona liczba znaków", "Maksymalna dozwolona liczba znaków to 16.");
			return;
		}
		if (digit.equals(".")) {
			if (!displayText.getText().contains(".")) {
				displayText.setText(displayText.getText() + ".");
				return;
			} else
				return;
		}
		if (displayText.getText().equals("0")) {
			if (!(digit.equals("0") || digit.equals("000")))
				displayText.setText(digit);
		} else
			displayText.setText(displayText.getText() + digit);
	}

	private void mathOperationClicked(String operation) {
		if (nonNumericOutputShown)
			return;
		if (finalResultShown)
			finalResultShown = false;
		if (!pendingOperation.isBlank()) {
			pendingOperation = operation;
			return;
		}
		if (!calcInput.isBlank() && calcInput.substring(calcInput.length() - 1, calcInput.length()).equals("/")
				&& displayText.getText().equals("0")) {
			showErrorWindow("Nie można dzielić przez zero", "Popraw swoje obliczenia.");
			return;
		}
		if (usePower) {
			usePower = false;
			calcInput = calcInput + ", " + displayText.getText() + ")";
		} else if (operation.equals("/") && !displayText.getText().contains("."))
			calcInput = calcInput + displayText.getText() + ".";
		else
			calcInput = calcInput.concat(displayText.getText());
		calculate();
		pendingOperation = operation;
	}
}

