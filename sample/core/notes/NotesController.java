package sample.core.notes;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.core.handlers.ErrorAndExceptionHandler;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

/*
 *   Controller that contains methods that interact with GUI in a Note window.
 */

public class NotesController implements Initializable {
    public int id;
    public String connectionUrl;
    public String tableName;
    public int currentNoteSize;

    @FXML private JFXTextArea txtFieldNote;
    @FXML private Label lblCounterChars;

    /* Set parameters on window load
    * @param: id is the id of an individual or a company that user clicked on
    * @param: connectionUrl is URL of database
    * @param: tableName the name of the table to retrieve data from
    * */
    public void setParameters(int id, String connectionUrl, String tableName) {
        this.id = id;
        this.connectionUrl = connectionUrl;
        this.tableName = tableName;
        txtFieldNote.appendText(loadNote());
        lblCounterChars.setText(String.valueOf(txtFieldNote.getText().length()));
        currentNoteSize = Integer.valueOf(lblCounterChars.getText());
    }

    /* Get number from dynamic label to know how much chars was used */
    public int getLabelNumber() {
        return Integer.valueOf(lblCounterChars.getText());
    }

    /* Updates the note */
    public void saveNote() {
        /* If chars > 200, decline the data integration */
        if (Integer.valueOf(lblCounterChars.getText()) > 200) {
            new ErrorAndExceptionHandler().showWarningAlert("The note is too long.");
            return;
        } else {
            /* Update currentNoteSize so we avoid to ask user to save information twice on close request of the window */
            currentNoteSize = Integer.valueOf(lblCounterChars.getText());
            /* Establish connection */
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement("update "+ tableName + " set Note = ? where id = " + id)) {

                preparedStatement.setString(1, txtFieldNote.getText());
                /* save note */
                preparedStatement.executeUpdate();

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not save note...", e.getMessage());
            }
            new Alert(Alert.AlertType.INFORMATION, "The note was successfully updated.", ButtonType.OK).showAndWait();
        }
    }

    /* Close Note window */
    public void discard() {
        Stage stage = (Stage) txtFieldNote.getScene().getWindow();

        /* Verify if changes were made in the note on close request of the Note window */
        if (currentNoteSize != Integer.valueOf(lblCounterChars.getText())) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "There are unsaved changes. Close note anyway?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> answer = deleteAlert.showAndWait();

            if (answer.isPresent() && answer.get() == ButtonType.YES) {
                stage.close();
            } else {
                return;
            }
        } else {
            stage.close();
        }
    }

    /* Delete note */
    public void deleteNote() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Delete current note?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            /* Establish connection */
            try (Connection connection = DriverManager.getConnection(connectionUrl);
                 PreparedStatement preparedStatement = connection.prepareStatement("update " + tableName + " set Note = ? where id = " + id)) {

                /* Instead of delete the note each time, we just simulate it and set text to empty */
                preparedStatement.setString(1, "");
                /* Update note */
                preparedStatement.executeUpdate();

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not delete note...", e.getMessage());
            }
            /* Update GUI component */
            txtFieldNote.setText("");
            new Alert(Alert.AlertType.INFORMATION, "The note was successfully deleted.", ButtonType.OK).showAndWait();
        } else {
            return;
        }
    }

    /* Counts how many chars was typed in JFTextArea dynamically and shows it to the user */
    public void countCharacters() {
        lblCounterChars.textProperty().bind(txtFieldNote.textProperty()
                .length()
                .asString("%d"));
    }

    /* Load note on window open
    * @return: note is a string that contains text of note that is set afterwards to JFTextArea
    * */
    public String loadNote() {
        String note = null;
        /* Establish connection */
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("Select Note from " + tableName + " where id = " + id)) {

            note = resultSet.getString("Note");
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not load note...", e.getMessage());
        }
        return note;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
