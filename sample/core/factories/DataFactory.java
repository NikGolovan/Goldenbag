package sample.core.factories;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.core.helpers.CompaniesHelper;
import sample.core.helpers.IndividualsHelper;
import sample.core.companies.CompaniesTableModel;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.indviduals.TableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 *   This class populates data from database and displays it in TableView for individuals and companies.
 */

public class DataFactory {
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    public void populateDataInd(IndividualsHelper individual) {
        /* create a thread */
        Platform.runLater(() -> {
            ObservableList<TableModel> listIndviduals = FXCollections.observableArrayList();
            /* establish connection */
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("Select * from content_individuals")) {

                /* get data from database */
                while (resultSet.next()) {
                    individual.getTable().setPlaceholder(new ProgressBar());
                    listIndviduals.add(new TableModel(resultSet.getInt("id"), resultSet.getString("FirstName"),
                            resultSet.getString("LastName"), resultSet.getString("EmailAdress"),
                            resultSet.getString("LicenseValidFrom"), resultSet.getDouble("amount")));
                }

                if (!resultSet.next())
                    individual.getTable().setPlaceholder(new Label("No results found..."));

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not populate data in the table...", e.getMessage());
            }
            /* display retrieved data from database in TableView columns */
            try {
                individual.getId().setCellValueFactory(new PropertyValueFactory<>("id"));
                individual.getFirstName().setCellValueFactory(new PropertyValueFactory<>("FirstName"));
                individual.getLastName().setCellValueFactory(new PropertyValueFactory<>("LastName"));
                individual.getEmailAddress().setCellValueFactory(new PropertyValueFactory<>("EmailAdress"));
                individual.getDate().setCellValueFactory(new PropertyValueFactory<>("dateLicenseValid"));
                individual.getAmount().setCellValueFactory(new PropertyValueFactory<>("amount"));
            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not set cellValueFactory for columns...", e.getMessage());
            }

            individual.getTable().setItems(listIndviduals);

            if (individual.getTable().getItems().size() == 0)
                individual.getTable().setPlaceholder(new Label("Table is empty..."));
        });
    }

    public void populateDataComp(CompaniesHelper company) {
        /* create a thread */
        Platform.runLater(() -> {
            ObservableList<CompaniesTableModel> listCompanies = FXCollections.observableArrayList();
            /* establish connection */
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("Select * from content_companies")) {

                /* get data from database */
                while (resultSet.next()) {
                    company.getTable().setPlaceholder(new ProgressBar());
                    listCompanies.add(new CompaniesTableModel(resultSet.getInt("id"), resultSet.getString("CompanyName"),
                            resultSet.getString("EmailAddress"),
                            resultSet.getString("LicenseValidFrom"), resultSet.getDouble("amount")));
                }

                if (!resultSet.next())
                    company.getTable().setPlaceholder(new Label("No results found..."));

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not populate data in the table...", e.getMessage());
            }

            /* display retrieved data from database in TableView columns */
            try {
                company.getId().setCellValueFactory(new PropertyValueFactory<>("id"));
                company.getCompanyName().setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
                company.getEmailAddress().setCellValueFactory(new PropertyValueFactory<>("EmailAddress"));
                company.getDate().setCellValueFactory(new PropertyValueFactory<>("dateLicenseValid"));
                company.getAmount().setCellValueFactory(new PropertyValueFactory<>("amount"));
            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not set cellValueFactory for columns...", e.getMessage());
            }
            company.getTable().setItems(listCompanies);

            if (company.getTable().getItems().size() == 0)
                company.getTable().setPlaceholder(new Label("Table is empty..."));
        });
    }
}
