package sample.core.preferences;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


/*
 *   This class contains methods that show text of question marks from preferences window whenever user clicks on it.
 */

public class QuestionMarkMessages {
    public void showAboutDate(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Date Preference"));
        content.setBody(new Text("Defines how the date will be shown in the table.\nPlease note, after changing date preference" +
                "all dates in existing data\nwill be automatically adjusted to the selected one."));
        stackPane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    public void showAboutDatasetJson(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("JSON Data Set"));
        content.setBody(new Text("The name under which your dataset is stored and imported to the program\nPlease note, if DataSet name is different" +
                " the import could not be performed.\n"));
        stackPane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    public void showAboutProgram(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Do Not Ask Before Closing Program"));
        content.setBody(new Text("The dialog window before closing application will not be\nshown anymore."));
        stackPane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }


    public void showAboutUploadingForms(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Allow Uploading Of Incomplete Forms"));
        content.setBody(new Text("By default the program does not allow to leave empty fields in the form\n" +
                "however, this checkbox allows to upload empty fields to the database."));
        stackPane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    public void showAboutUsedCurrency(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Currently Used Currency"));
        content.setBody(new Text("You can change the currency that is used for the \"amount\" field.\nIt only has visual effect " +
                "and does not influence any processing with currency."));
        stackPane.setVisible(true);

        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    public void showAboutDefaultPrice(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Set Default Price"));
        content.setBody(new Text("Each time you will add a new client or new company, the price\n" +
                "that is set here will be suggested on the field \"amount\"."));
        stackPane.setVisible(true);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    public void showAboutDeleteAllData(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Delete All Data"));
        content.setBody(new Text("This action will perform the suppression of all data that " +
                "currently exists in the database.\nAfter this, it will not be possible to restore any data."));
        stackPane.setVisible(true);
        
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton buttonOk = new JFXButton("OK");

        buttonOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
                stackPane.setVisible(false);
            }
        });

        /* Handles the case when user close dialog window by not clicking on "OK" button */
        handleDialog(dialog, stackPane);

        content.setActions(buttonOk);
        dialog.show();
    }

    /* Prevents the bug of StackPane that is still visible and blocks GUI components when user closes dialog by
    *  clicking outside of dialog box instead of clicking on "OK" button.
    * @param: dialog the actual dialog window
    * @param: stackPane a stackPane of current Tab
    * */
    public void handleDialog(JFXDialog dialog, StackPane stackPane) {
        dialog.setOnMouseExited(event -> {
            dialog.close();
            stackPane.setVisible(false);
        });
    }

    public void showAboutGoldenbag() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT GOLDENBAG");
        alert.setHeaderText("ABOUT");
        alert.setContentText("Goldenbag is an application that helps to track payments of your product.");

        alert.showAndWait();
    }
}
