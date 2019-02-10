package sample.core.json;

/*
 *   Contains data structure for .json file of Companies for import/export actions.
 */

public class RecordsCompanies {
    private Integer id;
    private String companyName;
    private String emailAdress;
    private String dateLicenseValid;
    private Double amount;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setDateLicenseValid(String dateLicenseValid) {
        this.dateLicenseValid = dateLicenseValid;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
