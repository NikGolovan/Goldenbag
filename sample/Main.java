package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.properties.PropertiesMethods;

import java.io.FileInputStream;
import java.util.Optional;
import java.util.Properties;

public class Main extends Application {

    private static final String FILE_NAME = "configuration.properties";

    private boolean askOnClose() {
        String closeWindowOnClose = null;

        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);

            closeWindowOnClose = properties.getProperty("closeDialogWindow");

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not get properties...", e.toString());
        }
        return Boolean.valueOf(closeWindowOnClose);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            Parent root = FXMLLoader.load(getClass().getResource("gui/MainWindow.fxml"));
            Image appIcon = new Image("sample/img/money-bag.png");
            primaryStage.getIcons().add(appIcon);
            primaryStage.setTitle("Goldenbag");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    if (!askOnClose()) {
                        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Close application?", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> answer = deleteAlert.showAndWait();

                        if (answer.isPresent() && answer.get() == ButtonType.YES)
                            System.exit(0);
                        else
                            windowEvent.consume();
                    } else {
                        System.exit(0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
