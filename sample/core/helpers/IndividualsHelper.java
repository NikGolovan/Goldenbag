package sample.core.helpers;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.core.indviduals.TableModel;

/*
 *   It is a helper class that allows to pass a single object with it's arguments to certain methods.
 *   It avoids to pass 7 parameters in one method, instead an object contains this parameters is passed.
 */

public class IndividualsHelper {
    private TableView<TableModel> table;
    private TableColumn<TableModel, Integer> id;
    private TableColumn<TableModel, String> firstName;
    private TableColumn<TableModel, String> lastName;
    private TableColumn<TableModel, String> emailAddress;
    private TableColumn<TableModel, String> date;
    private TableColumn<TableModel, Double> amount;

    public void setTable(TableView<TableModel> table) {
        this.table = table;
    }

    public void setId(TableColumn<TableModel, Integer> id) {
        this.id = id;
    }

    public void setFirstName(TableColumn<TableModel, String> firstName) {
        this.firstName = firstName;
    }

    public void setLastName(TableColumn<TableModel, String> lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(TableColumn<TableModel, String> emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setDate(TableColumn<TableModel, String> date) {
        this.date = date;
    }

    public void setAmount(TableColumn<TableModel, Double> amount) {
        this.amount = amount;
    }

    public TableView<TableModel> getTable() {
        return table;
    }

    public TableColumn<TableModel, Integer> getId() {
        return id;
    }

    public TableColumn<TableModel, String> getFirstName() {
        return firstName;
    }

    public TableColumn<TableModel, String> getLastName() {
        return lastName;
    }

    public TableColumn<TableModel, String> getEmailAddress() {
        return emailAddress;
    }

    public TableColumn<TableModel, String> getDate() {
        return date;
    }

    public TableColumn<TableModel, Double> getAmount() {
        return amount;
    }
}
