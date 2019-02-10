package sample.core.preferences;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.core.edit.EditInformationController;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.properties.PropertiesMethods;


import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

/*
 *   This class contains methods that allow to work with preferences and properties.
 */

public class PreferencesController extends PropertiesMethods implements Initializable {
    @FXML private ComboBox<String> dateRepresentation;
    @FXML private ComboBox<String> currencyUsed;
    @FXML private CheckBox askBeforeClosing;
    @FXML private StackPane stackPaneGeneral;
    @FXML private StackPane stackPaneImportExport;
    @FXML private StackPane stackPaneData;
    @FXML private JFXTextField txtJsonDatasetName;
    @FXML private JFXTextField txtPriceIndividuals;
    @FXML private JFXTextField txtPriceCompanies;
    @FXML private CheckBox checkAllowEmptyUploading;

    ErrorAndExceptionHandler exceptionHandler;
    EditInformationController editClientController;
    QuestionMarkMessages questionMarkMessages;

    private static final String FILE_NAME = "configuration.properties";
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    /* Set values to ComboBoxes of date and currency */
    public void initialiseDateBoxes() {
        dateRepresentation.getItems().addAll("dd/MM/yyyy", "dd.MM.yyyy", "dd-MM-yyyy");
        currencyUsed.getItems().addAll("EUR", "USD", "GBP");
    }

    /* Select all text when JFXTextField field JsonDatabaseName is clicked on */
    public void focusAll() {
        txtJsonDatasetName.selectAll();
    }

    /* Shows message when question mark is clicked  */
    public void aboutDate() { questionMarkMessages.showAboutDate(stackPaneGeneral); }

    public void aboutProgram() {
        questionMarkMessages.showAboutProgram(stackPaneGeneral);
    }

    public void aboutUsedCurrency() {
        questionMarkMessages.showAboutUsedCurrency(stackPaneGeneral);
    }

    public void aboutDefaultPrice() { questionMarkMessages.showAboutDefaultPrice(stackPaneGeneral); }

    public void aboutJsonDataset() { questionMarkMessages.showAboutDatasetJson(stackPaneImportExport); }

    public void aboutUploadingForms() {
        questionMarkMessages.showAboutUploadingForms(stackPaneData);
    }

    public void aboutDeleteAllData() {
        questionMarkMessages.showAboutDeleteAllData(stackPaneData);
    }

    /* Closes preference window */
    public void discard() {
        Stage stage = (Stage) dateRepresentation.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

        stage.close();
    }

    /* Set all GUI components to corresponding .properties settings */
    private void applyPropertiesOnLoad() {
        try {
            dateRepresentation.getSelectionModel().select(Integer.valueOf(getPropertyFromFile("datePreferenceIndex")));
            askBeforeClosing.setSelected(Boolean.valueOf(getPropertyFromFile("closeDialogWindow")));
            checkAllowEmptyUploading.setSelected(Boolean.valueOf("allowUploadIncompleteForm"));
            txtJsonDatasetName.setText(getPropertyFromFile("jsonDatasetName"));
            currencyUsed.getSelectionModel().select(Integer.valueOf(getPropertyFromFile("currencyPreferenceIndex")));
            txtPriceIndividuals.setText(getPropertyFromFile("priceForIndividuals"));
            txtPriceCompanies.setText(getPropertyFromFile("priceForCompanies"));
        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Could not apply properties on load...", e.toString());
        }
    }

    /* Validate all changes that has been made in preference window by taking it from GUI components */
    public void validate() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            Properties properties = new Properties();

            properties.setProperty("datePreferenceIndex", String.valueOf(dateRepresentation.getSelectionModel().getSelectedIndex()));
            properties.setProperty("datePreferenceString",dateRepresentation.getSelectionModel().getSelectedItem().toString());
            properties.setProperty("closeDialogWindow",   String.valueOf(askBeforeClosing.isSelected()));
            properties.setProperty("allowUploadIncompleteForm", String.valueOf(checkAllowEmptyUploading.isSelected()));
            properties.setProperty("jsonDatasetName", txtJsonDatasetName.getText());
            properties.setProperty("currencyPreferenceIndex", String.valueOf(currencyUsed.getSelectionModel().getSelectedIndex()));
            properties.setProperty("currencyUsed", currencyUsed.getSelectionModel().getSelectedItem().toString());
            properties.setProperty("priceForIndividuals", txtPriceIndividuals.getText());
            properties.setProperty("priceForCompanies", txtPriceCompanies.getText());
            properties.store(fileOutputStream, null);

            /* Calls another method to update date format in TableView */
            new EditInformationController().updateSqlDateformat();
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not set properties...", e.toString());
        }
        new Alert(Alert.AlertType.INFORMATION, "Changes has been successfully applied.", ButtonType.OK).showAndWait();
    }

    /* Set all preferences by default in .properties file and according to it all GUI components */
    public void setToDefault() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Set all configurations by default?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            dateRepresentation.getSelectionModel().select(0);
            askBeforeClosing.setSelected(false);
            txtJsonDatasetName.setText("records");
            checkAllowEmptyUploading.setSelected(false);
            currencyUsed.getSelectionModel().select(0);

            validate();
        } else {
            return;
        }
    }

    /* Delete all data from Individuals and Companies */
    public void deleteAllData() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "This action is irreversible, delete all Data?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            /* Call method that actually does the suppression */
            performDataDeletion(CONNECTION_URL_IND, "content_individuals");
            performDataDeletion(CONNECTION_URL_COMP, "content_companies");

            new Alert(Alert.AlertType.INFORMATION, "All data has been successfully deleted from the database.", ButtonType.OK).showAndWait();
        } else {
            return;
        }
    }

    /* Allows only numbers in JFXTextField PriceCompanies */
    public void allowOnlyNumbers(JFXTextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    /* Perform the suppression of all data in the database */
    public void performDataDeletion(String connectionUrl, String tableName) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {

            statement.execute("delete from " + tableName);

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not delete all data from " + tableName + " table...", e.toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            exceptionHandler = new ErrorAndExceptionHandler();
            editClientController = new EditInformationController();
            questionMarkMessages = new QuestionMarkMessages();
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not initialize some instances...", e.toString());
        }
        allowOnlyNumbers(txtPriceIndividuals);
        allowOnlyNumbers(txtPriceCompanies);
        initialiseDateBoxes();
        applyPropertiesOnLoad();
    }

}
