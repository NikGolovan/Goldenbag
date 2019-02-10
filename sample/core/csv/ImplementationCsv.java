package sample.core.csv;

import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.core.companies.CompaniesTableModel;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.indviduals.TableModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

/*
 *   This class contains methods that are used for .csv actions.
 */

public class ImplementationCsv {

    /* This method exports data from TableView of Individuals database to .csv file
    * @param: TableVew of Individuals
    * @param: items a list of id's to export that are selected in TableView of Individuals
    * */
    public void exportIndividuals(TableView<TableModel> table, List<Integer> items) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null)
            return;
        else
            file = new File(file.getAbsolutePath() + ".csv");

        /* Write data to a .csv file */
        try (Writer printWriter = new BufferedWriter(new FileWriter(file))) {
            /* If items.size() <= 1, export everything */
            if (items.size() <= 1) {
                for (int i = 0; i < table.getItems().size(); i++) {
                    String text = table.getItems().get(i).getId() + ";" + table.getItems().get(i).getFirstName()
                            + ";" + table.getItems().get(i).getLastName()  + ";" + table.getItems().get(i).getEmailAdress()
                            + ";" + table.getItems().get(i).getDateLicenseValid().replace('.', ',') + ";" +
                            table.getItems().get(i).getAmount() + "\n";
                    printWriter.write(text);
                }
            } else {
                /* Otherwise, export selected */
                for (int i = 0; i < items.size(); i++) {
                    String text = table.getSelectionModel().getSelectedItems().get(i).getId() + ";" +
                            table.getSelectionModel().getSelectedItems().get(i).getFirstName()
                            + ";" + table.getSelectionModel().getSelectedItems().get(i).getLastName() + ";" +
                            table.getSelectionModel().getSelectedItems().get(i).getEmailAdress()
                            + ";" + table.getSelectionModel().getSelectedItems().get(i).getDateLicenseValid() + ";" +
                            table.getSelectionModel().getSelectedItems().get(i).getAmount() + "\n";
                    printWriter.write(text);
                }
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not process the task...", e.getMessage());
        }
        new ErrorAndExceptionHandler().showConfirmationAlert("File was successfully created.");
    }

    /* This method exports data from TableView of Individuals database to .csv file
     * @param: TableVew of Companies
     * @param: items a list of id's to export that are selected in TableView of Companies
     * */
    public void exportCompanies(TableView<CompaniesTableModel> table, List<Integer> items) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null)
            return;
        else
            file = new File(file.getAbsolutePath() + ".csv");

        /* Write data to a .csv file */
        try (Writer printWriter = new BufferedWriter(new FileWriter(file))) {
            /* If items.size() <= 1, export everything */
            if (items.size() <= 1) {
                for (int i = 0; i < table.getItems().size(); i++) {
                    String text = table.getItems().get(i).getId() + ";" + table.getItems().get(i).getCompanyName() + ";" +
                            table.getItems().get(i).getEmailAddress()
                            + ";" + table.getItems().get(i).getDateLicenseValid().replace('.', ',') + ";" +
                            table.getItems().get(i).getAmount() + "\n";
                    printWriter.write(text);
                }
            } else {
                /* Otherwise, export selected */
                for (int i = 0; i < items.size(); i++) {
                    String text = table.getSelectionModel().getSelectedItems().get(i).getId() + ";" +
                            table.getSelectionModel().getSelectedItems().get(i).getCompanyName() + ";" +
                            table.getItems().get(i).getEmailAddress()
                            + ";" + table.getSelectionModel().getSelectedItems().get(i).getDateLicenseValid() + ";" +
                            table.getSelectionModel().getSelectedItems().get(i).getAmount() + "\n";
                    printWriter.write(text);
                }
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not process the task...", e.getMessage());
        }
        new ErrorAndExceptionHandler().showConfirmationAlert("File was successfully created.");
    }
}
