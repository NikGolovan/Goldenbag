package sample.core.handlers;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/*
 *   A class that handles Alert/Error/Warning messages. Contains it's implementation, structure and text.
 */

public class ErrorAndExceptionHandler {
    private static final String TITLE = "Exception Dialog";
    private static final String HEADER = "Oops, something went wrong.";
    private static final String LABEL = "Helpful information for the developer:";

    public void showErrorAlert(String context, String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(TITLE);
        alert.setHeaderText(HEADER);
        alert.setContentText(context);

        Label label = new Label(LABEL);

        TextArea textArea = new TextArea(e);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    public void showConfirmationAlert(String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Success");
        alert.setContentText(context);

        alert.showAndWait();
    }

    public void showWarningAlert(String context) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("The task was not taken into account");
        alert.setContentText(context);

        alert.showAndWait();
    }
}
