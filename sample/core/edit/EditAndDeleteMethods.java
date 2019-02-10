package sample.core.edit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.core.companies.CompaniesTableModel;
import sample.core.factories.DataFactory;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.helpers.CompaniesHelper;
import sample.core.helpers.EditInformationHelper;
import sample.core.helpers.IndividualsHelper;
import sample.core.indviduals.TableModel;

import java.sql.*;
import java.util.List;

/*
 *  Class containing suppression method and some edit client methods.
 */
public class EditAndDeleteMethods {

    /* Delete either client either an Individual or a Company
    * @param: items a list of id's to be deleted
    * @param: tableName a table to delete from
    * @param: connectionUrl is a URL of the database
    * */
    public void deleteClient(List<Integer> items, String tableName, String connectionUrl) {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {

            for (int i = 0; i < items.size(); i++) {
                statement.execute("delete from " + tableName + " where id like '%" + items.get(i) + "%'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Set corresponding data before editing and pass it to oepnEditWindow method
     * @param: individual an object that contain information about individuals data set
     * @param: company an object that contain information about companies data set
     * @param: tableInd a TableView of Individuals
     * @param: tableComp a TableView of Companies
     * @param: tabIndex index of an opened Tab (Individuals or Companies)
     */
    public void editClient(IndividualsHelper individual, CompaniesHelper company, TableView<TableModel> tableInd,
                           TableView<CompaniesTableModel> tableComp, int tabIndex) {
        if (tabIndex == 0) {
            EditInformationHelper information = new EditInformationHelper();
            information.setId(tableInd.getSelectionModel().getSelectedItem().getId());
            information.setFirstName(tableInd.getSelectionModel().getSelectedItem().getFirstName());
            information.setLastName(tableInd.getSelectionModel().getSelectedItem().getLastName());
            information.setCompanyName("N/A");
            information.setEmailAddress(tableInd.getSelectionModel().getSelectedItem().getEmailAdress());
            information.setDate(tableInd.getSelectionModel().getSelectedItem().getDateLicenseValid());
            information.setAmount(tableInd.getSelectionModel().getSelectedItem().getAmount());

            openEditWindow(individual, company, information, tabIndex);
        } else {
            EditInformationHelper information = new EditInformationHelper();
            information.setId(tableComp.getSelectionModel().getSelectedItem().getId());
            information.setFirstName("N/A");
            information.setLastName("N/A");
            information.setCompanyName(tableComp.getSelectionModel().getSelectedItem().getCompanyName());
            information.setEmailAddress(tableComp.getSelectionModel().getSelectedItem().getEmailAddress());
            information.setDate(tableComp.getSelectionModel().getSelectedItem().getDateLicenseValid());
            information.setAmount(tableComp.getSelectionModel().getSelectedItem().getAmount());

            openEditWindow(individual, company, information, tabIndex);
        }
    }

    /* Open Edit Information window
     * @param: individual an object that contain information about individuals data set
     * @param: company an object that contain information about companies data set
     * @param: information is an object that contains necessarily information
     * @param: tabIndex an index of an opened Tab
     */
    public void openEditWindow(IndividualsHelper individual, CompaniesHelper company, EditInformationHelper information, int tabIndex) {
        try {
            DataFactory dataFactory = new DataFactory();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/gui/EditInformation.fxml"));
            fxmlLoader.load();

            EditInformationController editClientController = fxmlLoader.getController();
            editClientController.setFields(information, tabIndex);

            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Goldenbag - Edit Information");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);

            /* Refresh data in TableView of Individuals and Companies after commiting changes in data */
            stage.setOnCloseRequest(windowEvent -> { dataFactory.populateDataInd(individual); dataFactory.populateDataComp(company);});

            stage.show();

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not edit information...", e.toString());
            e.printStackTrace();
        }
    }
}
