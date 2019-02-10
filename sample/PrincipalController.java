package sample;

import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.core.companies.CompaniesTableModel;
import sample.core.csv.ImplementationCsv;
import sample.core.factories.DataFactory;
import sample.core.factories.SearchFactory;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.edit.EditAndDeleteMethods;
import sample.core.indviduals.TableModel;
import sample.core.data.InitialiseDatabase;
import sample.core.notes.ImplementationNotes;
import sample.core.preferences.QuestionMarkMessages;
import sample.core.properties.PropertiesInitializer;
import sample.core.json.ImplementationJson;
import sample.core.helpers.CompaniesHelper;
import sample.core.helpers.IndividualsHelper;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/*
 *   This is a principal controller that is responsible for the interaction with code and GUI in the main window.
 */

public class PrincipalController implements Initializable {

    @FXML private JFXTabPane tabbedPane;
    @FXML private JFXTextField txtSearchIndividual;
    @FXML private JFXTextField txtSearchCompany;

    @FXML private TableColumn<CompaniesTableModel, Integer> idCompany;
    @FXML private TableView<CompaniesTableModel> tableCompanies;
    @FXML private TableColumn<CompaniesTableModel, String> companyName;
    @FXML private TableColumn<CompaniesTableModel, String> emailAddressCompany;
    @FXML private TableColumn<CompaniesTableModel, String> dateLicenseValidCompany;
    @FXML private TableColumn<CompaniesTableModel, Double> amountCompanies;

    @FXML private TableView<TableModel> tableIndividuals;
    @FXML private TableColumn<TableModel, Integer> idIndividuals;
    @FXML private TableColumn<TableModel, String> firstName;
    @FXML private TableColumn<TableModel, String> lastName;
    @FXML private TableColumn<TableModel, String> emailAdressIndividuals;
    @FXML private TableColumn<TableModel, String> dateLicenseValidInd;
    @FXML private TableColumn<TableModel, Double> amountIndividuals;

    @FXML private Label lblRefreshTimeInd;
    @FXML private Label lblRefreshTimeComp;

    private EditAndDeleteMethods implementationTable;
    private ImplementationCsv implementationCsv;
    private ErrorAndExceptionHandler exceptionHandler;
    private PropertiesInitializer propertiesInitializer;
    private InitialiseDatabase initializerDatabase;
    private SearchFactory searchFactory;
    private DataFactory dataFactory;
    private ImplementationNotes implementationNotes;
    private IndividualsHelper individual;
    private CompaniesHelper company;

    private static final String FXML_PATH_PREFERENCES = "/sample/gui/Preferences.fxml";
    private static final String PREFERENCES_WINDOW_TITLE = "Goldenbag - Preferences";
    private static final String FXML_PATH_NEW_INDIVIDUAL = "/sample/gui/NewClient.fxml";
    private static final String NEW_INDIVIDUAL_WINDOW_TITLE = "Goldenbag - Add New Client";
    private static final String FXML_PATH_NEW_COMPANY = "/sample/gui/NewCompany.fxml";
    private static final String NEW_COMPANY_WINDOW_TITLE = "Goldenbag - Add New Company";

    private static final  String NO_CELL_SELECTED = "No cell was selected for this action";

    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    /* Open preferences Window */
    public void openPreferences() {
        openNewWidnow(FXML_PATH_PREFERENCES, PREFERENCES_WINDOW_TITLE);
    }

    /* Open new Individual Window */
    public void newClient() {
        openNewWidnow(FXML_PATH_NEW_INDIVIDUAL, NEW_INDIVIDUAL_WINDOW_TITLE);
    }

    /* Open new company Window */
    public void newCompany() {
        openNewWidnow(FXML_PATH_NEW_COMPANY, NEW_COMPANY_WINDOW_TITLE);
    }

    /* Open note */
    public void openNote() {
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        if (tabIndex == 0) {
            if (tableIndividuals.getSelectionModel().getSelectedItem() == null) {
                new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
                return;
            } else {
                int individualsId = tableIndividuals.getSelectionModel().getSelectedItem().getId();
                implementationNotes.openNoteWindow(individualsId, CONNECTION_URL_IND, "content_individuals");
            }
        } else {
            if (tableCompanies.getSelectionModel().getSelectedItem() == null) {
                new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
                return;
            } else {
                int companiesId = tableCompanies.getSelectionModel().getSelectedItem().getId();
                implementationNotes.openNoteWindow(companiesId, CONNECTION_URL_COMP, "content_companies");
            }
        }
    }

    /* Perform an opening of new window
    * @param: fxmlPath a path for the GUI component of the window
    * @param: title is a title to be set on the window
    */
    public void openNewWidnow(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(fxmlPath));
            fxmlLoader.load();

            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage(StageStyle.DECORATED);
            javafx.scene.image.Image appIcon = new Image("sample/img/money-bag.png");
            stage.getIcons().add(appIcon);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.setResizable(false);

            /* Each time the window is closed, TableView is refreshed */
            stage.setOnCloseRequest(windowEvent -> { dataFactory.populateDataInd(individual); dataFactory.populateDataComp(company); });

            stage.show();

        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Could not open new window with the following path " + fxmlPath + "...", e.toString());
        }
    }

    /* Ferfresh TableView to update the data that is displayed */
    public void refreshTable() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar calendar = Calendar.getInstance();

            if (tabbedPane.getSelectionModel().getSelectedIndex() == 0) {
                dataFactory.populateDataInd(individual);
                lblRefreshTimeInd.setText(dateFormat.format(calendar.getTime()));
            } else {
                dataFactory.populateDataComp(company);
                lblRefreshTimeComp.setText(dateFormat.format(calendar.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptionHandler.showErrorAlert("Could not refresh the table...", e.toString());
        }
    }

    /* Search for the client */
    public void searchClient() {
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();
        String searchIndividual = txtSearchIndividual.getText();
        String searchCompany = txtSearchCompany.getText();
        String dataToSearch = tabIndex == 0 ? searchIndividual : searchCompany;

        searchFactory.searchClient(tableIndividuals, tableCompanies, dataToSearch, tabIndex);
    }

    /* If user double clicks on any row in the TaleView, Edit Information Window will be opened with according data */
    public void doubleClickOnRow() {
        tableIndividuals.setOnMouseClicked(t -> {
            int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

            if (t.getClickCount() == 2 && tableIndividuals.getSelectionModel().getSelectedItem() != null) {
                implementationTable.editClient(individual, company, tableIndividuals, tableCompanies, tabIndex);
            }
        });
        tableCompanies.setOnMouseClicked(t -> {
            int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

            if (t.getClickCount() == 2 && tableCompanies.getSelectionModel().getSelectedItem() != null) {
                implementationTable.editClient(individual, company, tableIndividuals, tableCompanies, tabIndex);
            }
        });
    }

    /* Call a function that edit's information about a client */
    public void editCell() {
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        if (rowsAreSelected(tabIndex)) {
            new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
            return;
        }
        implementationTable.editClient(individual, company, tableIndividuals, tableCompanies, tabIndex);
    }

    /* Copy email address to the buffer so the user can paste it in his email instead of typing it each time */
    public void copyEmailAddress() {
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        if (tabIndex == 0) {
            if (tableIndividuals.getSelectionModel().getSelectedItem() != null) {
                String emailAddressInd = tableIndividuals.getSelectionModel().getSelectedItem().getEmailAdress();
                StringSelection stringSelection = new StringSelection(emailAddressInd);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } else {
                new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
            }
        } else {
            if (tableCompanies.getSelectionModel().getSelectedItem() != null) {
                String emailAddressComp = tableCompanies.getSelectionModel().getSelectedItem().getEmailAddress();
                StringSelection stringSelection = new StringSelection(emailAddressComp);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } else {
                new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
            }
        }
    }

    /* Call function that performs export of a TableView data to a .json file */
    public void launchExportToJson() {
        List<Integer> items = new ArrayList<>();
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        if (rowsAreSelected(tabIndex)) {
            new Alert(Alert.AlertType.WARNING, NO_CELL_SELECTED, ButtonType.OK).showAndWait();
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(new Stage());

            items = getSelectedRows(tabIndex);

            if (file == null) {
                return;
            } else {
                file = new File(file.getAbsolutePath() + ".json");
                new ImplementationJson().exportToJson(file, items, tabIndex);
            }
        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Could not process the task...", e.toString());
        }
    }

    /* Call function that performs import of a TableView data to a .json file */
    public void launchImportJson() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Files *.json", "*.json");
            File file = fileChooser.showOpenDialog(new Stage());

            fileChooser.getExtensionFilters().add(extensionFilter);

            if (file == null) {
                return;
            } else {
                new ImplementationJson().importJson(file, tabbedPane.getSelectionModel().getSelectedIndex());
                if (tabbedPane.getSelectionModel().getSelectedIndex() == 0) {
                    searchFactory.refreshData(tableIndividuals);
                    dataFactory.populateDataInd(individual);
                } else {
                    searchFactory.refreshData(tableCompanies);
                    dataFactory.populateDataComp(company);
                }
            }
        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Failed to choose a file...", e.toString());
        }
    }

    /* Get all id's of selected rows of TableView and write it to the list that is then returned
     * @param: tabIndex is an index of a current opened Tab (Individuals or Companies)
     * |@return: items a list of id's of selected rows
     */
    public List<Integer> getSelectedRows(int tabIndex) {
        List<Integer> items = new ArrayList<>();

        if (tabIndex == 0) {
            for (int i = 0; i < tableIndividuals.getSelectionModel().getSelectedItems().size(); i++) {
                items.add(tableIndividuals.getSelectionModel().getSelectedItems().get(i).getId());
            }
        } else {
            for (int i = 0; i < tableCompanies.getSelectionModel().getSelectedItems().size(); i++) {
                items.add(tableCompanies.getSelectionModel().getSelectedItems().get(i).getId());
            }
        }
        return items;
    }

    /* Call function that performs import of a TableView data to a .csv file */
    public void launchExportToCsv() {
        List<Integer> items = new ArrayList<>();
        items = getSelectedRows(tabbedPane.getSelectionModel().getSelectedIndex());
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        if (rowsAreSelected(tabIndex)) {
            new Alert(Alert.AlertType.WARNING, "No cell was selected for this action.", ButtonType.OK).showAndWait();
            return;
        }

        if (tabbedPane.getSelectionModel().getSelectedIndex() == 0)
            implementationCsv.exportIndividuals(tableIndividuals, items);
        else
            implementationCsv.exportCompanies(tableCompanies, items);
    }

    /* Allows multiple row selection in TableView
     * @param: event is a key event that listens if a key (LEFT CTRL) was pressed on keyboard for the selection
     */
    public void multiRowSelection(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            tableIndividuals.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableCompanies.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    /* Verify if any row is selected in the TableView
     * @param: tabIndex is an index of a current opened Tab (Individuals or Companies)
     * */
    public boolean rowsAreSelected(int tabIndex) {
        if (tabIndex == 0)
            return (tableIndividuals.getSelectionModel().getSelectedItem() == null);
         else
            return (tableCompanies.getSelectionModel().getSelectedItem() == null);
    }

    /* Call method that performs suppression of selected or and it's data in TableView */
    public void launchDeleteClient() {
        List<Integer> items = new ArrayList<>();
        int tabIndex = tabbedPane.getSelectionModel().getSelectedIndex();

        items = getSelectedRows(tabIndex);

        if (rowsAreSelected(tabIndex)) {
            new Alert(Alert.AlertType.WARNING, "No cell was selected for this action.", ButtonType.OK).showAndWait();
            return;
        }

        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected item(s) ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            if (tabIndex == 0) {
                implementationTable.deleteClient(items, "content_individuals", CONNECTION_URL_IND);
                searchFactory.refreshData(tableIndividuals);
                dataFactory.populateDataInd(individual);
            } else {
                implementationTable.deleteClient(items, "content_companies", CONNECTION_URL_COMP);
                searchFactory.refreshData(tableCompanies);
                dataFactory.populateDataComp(company);
            }
        } else {
            return;
        }
    }

    /* Ask if user want's to close the program when X pressed in the right window corner */
    public void confirmExit() {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Close application?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> answer = deleteAlert.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.YES)
            System.exit(0);
    }

    /* Open dialog to show about Goldenbag application */
    public void aboutGoldenbag() {
        new QuestionMarkMessages().showAboutGoldenbag();
    }

    /* Adjust column size to screen size */
    public void setTableResizePolicy() {
        tableIndividuals.setColumnResizePolicy(tableIndividuals.CONSTRAINED_RESIZE_POLICY);
        tableCompanies.setColumnResizePolicy(tableIndividuals.CONSTRAINED_RESIZE_POLICY);
    }

    /* Adjust tab size to screen size */
    public void adjustTabs() {
        tabbedPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            tabbedPane.setTabMinWidth(tabbedPane.getWidth() / 2);
            tabbedPane.setTabMaxWidth(tabbedPane.getWidth() / 2);
        });
    }

    /* Initialize individuals in helper class */
    public void initializeIndividuals() {
        individual.setTable(tableIndividuals);
        individual.setId(idIndividuals);
        individual.setFirstName(firstName);
        individual.setLastName(lastName);
        individual.setEmailAddress(emailAdressIndividuals);
        individual.setDate(dateLicenseValidInd);
        individual.setAmount(amountIndividuals);
    }

    /* Initialize companies in helper class */
    public void initializeCompanies() {
        company.setTable(tableCompanies);
        company.setId(idCompany);
        company.setCompanyName(companyName);
        company.setEmailAddress(emailAddressCompany);
        company.setDate(dateLicenseValidCompany);
        company.setAmount(amountCompanies);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            implementationTable = new EditAndDeleteMethods();
            implementationCsv = new ImplementationCsv();
            exceptionHandler = new ErrorAndExceptionHandler();
            propertiesInitializer = new PropertiesInitializer();
            initializerDatabase = new InitialiseDatabase();
            searchFactory = new SearchFactory();
            dataFactory = new DataFactory();
            implementationNotes = new ImplementationNotes();
            individual = new IndividualsHelper();
            company = new CompaniesHelper();
        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Failed to create an instance of a class on loading...", e.toString());
        }

        setTableResizePolicy();
        adjustTabs();
        initializeIndividuals();
        initializeCompanies();

        try {
            propertiesInitializer.createConfigurationFile();
            initializerDatabase.createTableIndividuals();
            dataFactory.populateDataInd(individual);
            initializerDatabase.createTableICompanies();
            dataFactory.populateDataComp(company);
        } catch (Exception e) {
            exceptionHandler.showErrorAlert("Failed to initialize some objects/methods.", e.toString());
        }
    }
}
