package sample.core.factories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import sample.core.companies.CompaniesTableModel;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.indviduals.TableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 *   A class contains method to perform a search for a specific individual or a company in TableView.
 */

public class SearchFactory {
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    /* Clear items in TableView
    * @param: table is a table either of Individuals or Companies
    * */
    public void refreshData(TableView<?> table) { table.getItems().clear(); }

    /* Perform the actual search in the TableView
     * @param: tableInd TableView containing Individuals data from database
     * @param: tableComp TableView containing Companies data from database
     * @param: dataToSearch is a string that is took from JFXTextField as a data to search
     * @param: tabIndex is the index of opened Tab (Individuals or Companies)
     * */
    public void searchClient(TableView<TableModel> tableInd, TableView<CompaniesTableModel> tableComp, String dataToSearch, int tabIndex) {
        /* List containing Individuals data structure of TableView */
        ObservableList<TableModel> individualsList = FXCollections.observableArrayList();
        /* List containing Companies data structure of TableView */
        ObservableList<CompaniesTableModel> companiesList = FXCollections.observableArrayList();

        /* If tabIndex == 0 then Individuals TableView is opened, so perform search in there */
        if (tabIndex == 0) {
            /* Clear data before */
            refreshData(tableInd);
            /* Establish connection */
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select * from content_individuals where firstName like '%" + dataToSearch + "%'")) {

                /* Retrieve data if found and add it to the list*/
                while (resultSet.next()) {
                    individualsList.add(new TableModel(resultSet.getInt("id"), resultSet.getString("FirstName"),
                            resultSet.getString("LastName"), resultSet.getString("EmailAdress"),
                            resultSet.getString("LicenseValidFrom"), resultSet.getDouble("Amount")));
                }
            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not execute research correctly...", e.getMessage());
            }
            /* Set data of the list to the table */
            tableInd.setItems(individualsList);
        } else {
            refreshData(tableComp);
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select * from content_companies where companyName like '%" + dataToSearch + "%'")) {

                while (resultSet.next()) {
                    companiesList.add(new CompaniesTableModel(resultSet.getInt("id"), resultSet.getString("CompanyName"),
                            resultSet.getString("EmailAddress"),
                            resultSet.getString("LicenseValidFrom"), resultSet.getDouble("Amount")));
                }
            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not execute research correctly...", e.getMessage());
            }
            tableComp.setItems(companiesList);
        }
    }
}
