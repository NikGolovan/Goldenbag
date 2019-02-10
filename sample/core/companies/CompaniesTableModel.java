package sample.core.companies;

/*
 *   Contains data structure of Companies database data that will be represented in TableView.
 */

public class CompaniesTableModel {
    Integer id;
    Double amount;
    String  companyName;
    String emailAddress;
    String licenseValidFrom;

    public CompaniesTableModel(Integer id, String companyName, String emailAddress, String licenseValidFrom, Double amount) {
        this.id = id;
        this.companyName = companyName;
        this.emailAddress = emailAddress;
        this.licenseValidFrom = licenseValidFrom;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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
