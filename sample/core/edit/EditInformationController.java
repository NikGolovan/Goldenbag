package sample.core.edit;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.core.factories.DateFactory;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.helpers.EditInformationHelper;
import sample.core.properties.PropertiesMethods;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

/*
 *   This class contains methods that are required to perform an editing of individual's or company's information.
 */

public class EditInformationController extends PropertiesMethods implements Initializable, DateFactory {
    @FXML public JFXTextField txtId;
    @FXML public JFXTextField txtFirstname;
    @FXML public JFXTextField txtLastname;
    @FXML public JFXTextField txtCompanyName;
    @FXML public JFXTextField txtEmailAddress;
    @FXML public JFXDatePicker dateField;
    @FXML public JFXTextField txtAmountEdit;
    @FXML public Label lblCurrency;
    @FXML public Button btnDiscard;
    @FXML public Button btnConfirm;

    private int tabIndex = 0;
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    private static final String DATE_PREFERENCE_STRING_PROPERTY = "datePreferenceString";

    /* Initialize GUI components depending on the object data that is passed to the parameter
     * @param: information is an object that contains the information about a row that has been opened for the edit
     * @param: tabIndex is an index of a Tab
     */
    public void setFields(EditInformationHelper information, int tabIndex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getPropertyFromFile(DATE_PREFERENCE_STRING_PROPERTY));

        this.txtId.setText(String.valueOf(information.getId()));
        this.txtFirstname.setText(information.getFirstName());
        this.txtLastname.setText(information.getLastName());
        this.txtCompanyName.setText(information.getCompanyName());
        this.txtEmailAddress.setText(information.getEmailAddress());
        this.dateField.setValue(LocalDate.parse(information.getDate(), formatter));
        this.tabIndex = tabIndex;
        this.txtAmountEdit.setText(String.valueOf(information.getAmount()));
        lblCurrency.setText(getPropertyFromFile("currencyUsed"));
        disableSomeFields();
    }

    /* Disable GUI JFXTextFields depended on which client was opened for editing, Individual or Company */
    public void disableSomeFields() {
        if (tabIndex == 0) {
            txtId.setDisable(true);
            txtCompanyName.setDisable(true);
        } else {
            txtId.setDisable(true);
            txtFirstname.setDisable(true);
            txtLastname.setDisable(true);
        }
    }

    /* Closes the window */
    public void discard() {
        Stage stage = (Stage) btnDiscard.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        stage.close();
    }

    /* Returns a list of ancient dates that should be changed to new date format
    * @param: connectionUrl a URL of database
    * @param: tableName a table from where information should be retrieved from
     */
    @Override
    public List<String> getOldDateFormat(String connectionUrl, String tableName) {
        List<String> dates = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select LicenseValidFrom from " + tableName)) {

            while (resultSet.next()) {
                dates.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not get dates from a table to change the format...", e.getMessage());
        }
        return dates;
    }

    /* A date parser that converts date type to the one that was set in preferences and retrieves it from .properties file */
    @Override
    public List<String> dateParser(List<String> dates) {
        List<String> results = new ArrayList<>();
        StringBuilder newDate = null;
        char regex = 0;

        switch (getPropertyFromFile(DATE_PREFERENCE_STRING_PROPERTY)) {
            case "dd/MM/yyyy":
                regex = '/';
                break;
            case "dd.MM.yyyy":
                regex = '.';
                break;
            case "dd-MM-yyyy":
                regex = '-';
                break;
            default:
                break;
        }

        for (int i = 0; i < dates.size(); i++) {
            newDate = new StringBuilder(dates.get(i));
            newDate.setCharAt(2, regex);
            newDate.setCharAt(5, regex);
            results.add(newDate.toString());
        }
        return results;
    }

    /* prevents to convert date format to yyyy-MM-dd when changes has been applied in preferences on open request of edit window */
    @Override
    public String getCorrectDateFormat(DatePicker datePicker) {
        DateTimeFormatter oldFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(getPropertyFromFile("datePreferenceString"));
        String newString = null;

        String oldString = datePicker.getValue().toString();
        LocalDate date = LocalDate.parse(oldString, oldFormatter);
        newString = date.format(newFormatter);

        return newString;
    }

    /* Set the date of all fields in database to one that was selected in preferences*/
    public void updateSqlDateformat() {
        List<String> datesIndividuals = getOldDateFormat(CONNECTION_URL_IND, "content_individuals");
        List<String> datesCompanies = getOldDateFormat(CONNECTION_URL_COMP, "content_companies");
        List<String> parsedDatesInd = new ArrayList<>();
        List<String> parsedDateComp = new ArrayList<>();
        parsedDatesInd = dateParser(datesIndividuals);
        parsedDateComp = dateParser(datesCompanies);

        performUpdate(parsedDatesInd, CONNECTION_URL_IND, "content_individuals");
        performUpdate(parsedDateComp, CONNECTION_URL_COMP, "content_companies");
    }

    /* Perform the actual update in the database of date fields */
    public void performUpdate(List<String> dates, String connectionUrl, String tableName) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement preparedStatement = connection.prepareStatement("update "+ tableName+ " set LicenseValidFrom = ?")) {

            for (int i = 0; i < dates.size(); i++) {
                preparedStatement.setString(1, dates.get(i));
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not set new SQL date format for table " + tableName + "...", e.getMessage());
        }
    }

    /* Methods decides which save method should be called depending on tabIndex (index of opened Tab) */
    public void saveChanges() {
        if (tabIndex != 0)
            saveChangesIndividual();
        else
            saveChangesCompany();
    }

    /* Saves changes that has been made in the Individuals database */
    public void saveChangesIndividual() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
             PreparedStatement preparedStatement = connection.prepareStatement("update content_companies set companyName = ?," +
                     " emailAddress = ?, LicenseValidFrom = ?, amount = ? where id = " + txtId.getText())) {

            preparedStatement.setString(1, txtCompanyName.getText());
            preparedStatement.setString(2, txtEmailAddress.getText());
            preparedStatement.setString(3, getCorrectDateFormat(dateField));
            preparedStatement.setDouble(4, Double.valueOf(txtAmountEdit.getText()));

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not save information...", e.getMessage());
        }
        new Alert(Alert.AlertType.INFORMATION, "The information was successfully updated.", ButtonType.OK).showAndWait();
    }
    
    /* Saves changes that has been made in the Companies database */
    public void saveChangesCompany() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
             PreparedStatement preparedStatement = connection.prepareStatement("update content_individuals set firstName = ?," +
                     " lastName = ?, emailAdress = ?, LicenseValidFrom = ?, amount = ? where id = " + txtId.getText())) {

            preparedStatement.setString(1, txtFirstname.getText());
            preparedStatement.setString(2, txtLastname.getText());
            preparedStatement.setString(3, txtEmailAddress.getText());
            preparedStatement.setString(4, getCorrectDateFormat(dateField));
            preparedStatement.setDouble(5, Double.valueOf(txtAmountEdit.getText()));

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not save information...", e.getMessage());
        }
        new Alert(Alert.AlertType.INFORMATION, "The information was successfully updated.", ButtonType.OK).showAndWait();
    }

    /* Set current data to JFXDatePicker */
    public void setCurrentDate() {
        try {
            dateField.setValue(LocalDate.now());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Oops, an error has occurred. Details:\n" + e.getMessage(), ButtonType.OK);
        }
    }

    /* Select all text in Amont JFXTextfield when clicked on */
    public void focusAll() {
        txtAmountEdit.selectAll();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) { }
}
