package sample.core.helpers;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.core.companies.CompaniesTableModel;

/*
 *   It is a helper class that allows to pass a single object with it's arguments to certain methods.
 *   It avoids to pass 6 parameters in one method, instead an object contains this parameters is passed.
 */

public class CompaniesHelper {
    private TableView<CompaniesTableModel> table;
    private TableColumn<CompaniesTableModel, Integer> id;
    private TableColumn<CompaniesTableModel, String> companyName;
    private TableColumn<CompaniesTableModel, String> emailAddress;
    private TableColumn<CompaniesTableModel, String> date;
    private TableColumn<CompaniesTableModel, Double> amount;

    public void setTable(TableView<CompaniesTableModel> table) {
        this.table = table;
    }

    public void setId(TableColumn<CompaniesTableModel, Integer> id) {
        this.id = id;
    }

    public void setCompanyName(TableColumn<CompaniesTableModel, String> companyName) {
        this.companyName = companyName;
    }

    public void setEmailAddress(TableColumn<CompaniesTableModel, String> emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDate(TableColumn<CompaniesTableModel, String> date) {
        this.date = date;
    }

    public void setAmount(TableColumn<CompaniesTableModel, Double> amount) {
        this.amount = amount;
    }

    public TableView<CompaniesTableModel> getTable() {
        return table;
    }

    public TableColumn<CompaniesTableModel, Integer> getId() {
        return id;
    }

    public TableColumn<CompaniesTableModel, String> getCompanyName() {
        return companyName;
    }

    public TableColumn<CompaniesTableModel, String> getEmailAddress() {
        return emailAddress;
    }

    public TableColumn<CompaniesTableModel, String> getDate() {
        return date;
    }

    public TableColumn<CompaniesTableModel, Double> getAmount() {
        return amount;
    }
}
