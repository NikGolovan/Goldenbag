package sample.core.json;

/*
 *   Contains data structure for .json file of Individuals for import/export actions.
 */

public class RecordsIndividuals {
    private Integer id;
    private String firstName;
    private String lastName;
    private String emailAdress;
    private String dateLicenseValid;
    private Double amount;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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