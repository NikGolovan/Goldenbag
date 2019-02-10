package sample.core.companies;

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
import java.util.*;

/*
 *   This class contains methods of New Company Window.
 */

public class NewCompanyController extends PropertiesMethods implements Initializable {
    @FXML private JFXTextField txtCompanyName;
    @FXML private JFXTextField txtEmailAddress;
    @FXML private JFXDatePicker datePickerNewCompany;
    @FXML private JFXTextField txtAmountNewCompany;
    @FXML private Label lblValueSignCompany;

    private static final String CONNECTION_URL = "jdbc:sqlite:clients_companies.db";
    private static final String CURRENCY_PROPERTY = "currencyUsed";
    private static final String PRICE_COMPANIES_PROPERTY = "priceForCompanies";
    private static final String UPLOAD_INCOMPLETE_FORMS_PROPERTY = "allowUploadIncompleteForm";

    /* Sets current date in JFXDatePicker */
    public void setCurrentDate() {
        try {
            datePickerNewCompany.setValue(LocalDate.now());
            lblValueSignCompany.setText(getPropertyFromFile(CURRENCY_PROPERTY));
            txtAmountNewCompany.setText(getPropertyFromFile(PRICE_COMPANIES_PROPERTY));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Oops, an error has occurred. Details:\n" + e.getMessage(), ButtonType.OK);
        }
    }

    /* Closes the window */
    public void discard() {
        Stage stage = (Stage) datePickerNewCompany.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        stage.close();
    }

    /* Verifies if fields are empty
    * @return: boolean true or false
    * */
    public boolean fieldsAreEmpty(List<String> fields) {
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /* Save information, e.g add a new company to the database */
    public void saveInformation() {
        String companyName = txtCompanyName.getText();
        String emailAddress = txtEmailAddress.getText();
        String dateLicenseValid = datePickerNewCompany.getValue().format(DateTimeFormatter.ofPattern(getPropertyFromFile("datePreferenceString")));

        List<String> fields = new ArrayList<>();

        fields.add(emailAddress);
        fields.add(dateLicenseValid);

        if (Boolean.valueOf(getPropertyFromFile(UPLOAD_INCOMPLETE_FORMS_PROPERTY))) {
            uploadToSqlite(companyName, emailAddress, dateLicenseValid);
        } else {
            if (fieldsAreEmpty(fields)) {
                new ErrorAndExceptionHandler().showWarningAlert("Form is incomplete.");
                return;
            }
            /* Perform upload to the database */
            uploadToSqlite(companyName, emailAddress, dateLicenseValid);
        }
        txtCompanyName.setText("");
        txtEmailAddress.setText("");
    }

    /* Uploads data to database
    * @param: companyName the name of the company
    * @param: emailAddress company's email address
    * @param: dataPayment date of payment
    * */
    public void uploadToSqlite(String companyName, String emailAddress, String datePayment) {
        /* create a thread */
        Platform.runLater(() -> {
            /* Establish connection */
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                 PreparedStatement preparedStatement = connection.prepareStatement("insert into content_companies values (?, ?, ?, ?, ?, ?)")) {

                preparedStatement.setString(2, companyName);
                preparedStatement.setString(3, emailAddress);
                preparedStatement.setString(4, datePayment);
                preparedStatement.setDouble(5, Double.valueOf(txtAmountNewCompany.getText()));
                /* parameterIndex 6 is a Note column which we want to keep empty since note can be only added in note Window */
                preparedStatement.setString(6, "");

                /* Add new data to the database */
                preparedStatement.executeUpdate();

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not upload information to the table...", e.toString());
            }
        });
        new Alert(Alert.AlertType.INFORMATION, "New company has been successfully added to the database.", ButtonType.OK).showAndWait();
    }

    /* Select all text in JFXTextField when clicked on */
    public void focusAll() {
        txtAmountNewCompany.selectAll();
    }

    /* Allows only numbers in JFXTextField Amount */
    public void allowOnlyNumbers() {
        txtAmountNewCompany.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtAmountNewCompany.setText(oldValue);
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
