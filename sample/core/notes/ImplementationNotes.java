package sample.core.notes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.core.handlers.ErrorAndExceptionHandler;

import java.util.Optional;

/*
 *   This class contains implementation of methods to work with Notes.
 */

public class ImplementationNotes {

    /* Open note window
    * @param: id the id of company/client that user clicked on
    * @param: connectionUrl that helps to connect to correct database
    * @param: tableName the name of the table to retrieve data from
    * */
    public void openNoteWindow(int id, String connectionUrl, String tableName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/gui/Notes.fxml"));
            fxmlLoader.load();

            NotesController notesController = fxmlLoader.getController();
            notesController.setParameters(id, connectionUrl, tableName);

            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Goldenbag - Note");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);

            /* Verify on close if user had made changes and ask if they want to close the note anyway or either save it */
            stage.setOnCloseRequest(windowEvent -> {
                if (notesController.currentNoteSize != notesController.getLabelNumber()) {
                    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "There are unsaved changes. Close note anyway?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> answer = deleteAlert.showAndWait();

                    if (answer.isPresent() && answer.get() == ButtonType.YES) {
                        stage.close();
                    } else {
                        windowEvent.consume();
                    }
                }
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not open note...", e.toString());
        }
    }

}
