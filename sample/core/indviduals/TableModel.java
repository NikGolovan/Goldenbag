package sample.core.indviduals;

/*
 *   Contains data structure of Individuals database data that will be represented in TableView.
 */

public class TableModel {
    Integer id;
    Double amount;
    String firstName;
    String lastName;
    String emailAdress;
    String licenseValidFrom;

    public TableModel(Integer id, String firstName, String lastName, String emailAddress, String licenseValidFrom, Double amount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAdress = emailAddress;
        this.licenseValidFrom = licenseValidFrom;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public String getDateLicenseValid() {
        return licenseValidFrom;
    }

    public void setDateLicenseValid(String licenseValidFrom) {
        this.licenseValidFrom = licenseValidFrom;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
