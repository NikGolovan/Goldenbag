package sample.core.indviduals;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.properties.PropertiesMethods;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewClientController extends PropertiesMethods implements Initializable {

    @FXML private JFXTextField txtFirstname;
    @FXML private JFXTextField txtLastname;
    @FXML private JFXTextField txtEmailAddressClient;
    @FXML private JFXDatePicker datePickerNewClient;
    @FXML private JFXTextField txtAmountNewClient;
    @FXML private Label lblValueSignClient;

    private static final String CONNECTION_URL = "jdbc:sqlite:clients_individuals.db";
    private static final String INCOMPLETE_UPLOAD_PROPERTY = "allowUploadIncompleteForm";

    public void setCurrentDate() {
        try {
            datePickerNewClient.setValue(LocalDate.now());
            lblValueSignClient.setText(getPropertyFromFile("currencyUsed"));
            txtAmountNewClient.setText(getPropertyFromFile("priceForIndividuals"));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Oops, an error has occurred. Details:\n" + e.getMessage(), ButtonType.OK);
        }
    }

    public void discard() {
        Stage stage = (Stage) datePickerNewClient.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        stage.close();
    }

    public boolean fieldsAreEmpty(List<String> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public String getStartDate(String startDate)
    {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern(getPropertyFromFile("datePreferenceString"));

        return LocalDate.parse(startDate, inputFormat).format(outputFormat);
    }

    public void saveInformation() {
        String firstName = txtFirstname.getText();
        String lastName = txtLastname.getText();
        String emailAddress = txtEmailAddressClient.getText();
        String dateLicenseValid = datePickerNewClient.getValue().toString();
        String amount = txtAmountNewClient.getText();

        List<String> fields = new ArrayList<>();

        fields.add(firstName);
        fields.add(lastName);
        fields.add(emailAddress);
        fields.add(dateLicenseValid);
        fields.add(amount);

        if (Boolean.valueOf(getPropertyFromFile(INCOMPLETE_UPLOAD_PROPERTY))) {
            uploadToSqlite(firstName, lastName, emailAddress, getStartDate(dateLicenseValid), amount);
        } else {
            if (fieldsAreEmpty(fields)) {
                new ErrorAndExceptionHandler().showWarningAlert("Form is incomplete.");
                return;
            }
            uploadToSqlite(firstName, lastName, emailAddress, getStartDate(dateLicenseValid), amount);
        }
        txtFirstname.setText("");
        txtLastname.setText("");
        txtEmailAddressClient.setText("");
    }

    public void uploadToSqlite(String firstName, String lastName, String emailAdress, String datePayment, String amount) {
        Platform.runLater(() -> {
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement("insert into content_individuals values (?, ?, ?, ?, ?, ?, ?)")) {

                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, emailAdress);
                preparedStatement.setString(5, datePayment);
                preparedStatement.setDouble(6, Double.valueOf(amount));
                preparedStatement.setString(7, "");

                preparedStatement.executeUpdate();

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not upload information to the table...", e.toString());
            }
        });
        new Alert(Alert.AlertType.INFORMATION, "New client has been successfully added to the database.", ButtonType.OK).showAndWait();
    }

    public void focusAll() {
        txtAmountNewClient.selectAll();
    }

    /* Allows only numbers in JFXTextField Amount */
    public void allowOnlyNumbers() {
        txtAmountNewClient.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtAmountNewClient.setText(oldValue);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allowOnlyNumbers();
        setCurrentDate();
    }
}
