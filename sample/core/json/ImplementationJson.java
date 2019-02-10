package sample.core.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import sample.core.handlers.ErrorAndExceptionHandler;
import sample.core.helpers.GsonHelper;
import sample.core.properties.PropertiesMethods;

import java.io.*;
import java.sql.*;
import java.util.*;

/*
 *   Contains implementation of methods that allows to work with .json files.
 */

public class ImplementationJson extends PropertiesMethods {
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";
    private static final String FILE_ENCODING = "UTF-8";
    private static final String JSON_DATASET_NAME_PROPERTY = "jsonDatasetName";
    private static final String TABLE_INDIVIDUALS = "content_individuals";
    private static final String TABLE_COMPANIES = "content_companies";

    private static final String EXPORT_JSON_FAIL_MESSAGE = "Could not export data to JSON file...";

    /* Calls either import to Individuals or Companies depending on tab that is opened
    * @param: file a file that is created
    * @param: tabIndex index of a Tab (Individuals or Companies)
    */
    public void importJson(File file, int tabIndex) {
        if (tabIndex == 0) {
            importToIndividuals(file);
        } else {
            importToCompanies(file);
        }
    }

    /* To avoid useless import, this method returns only distinguish values compared with TableView and .jsonFile
     * For example, if .json file contains id's 1, 2, 3, 4 and TableView only 1, 2, 3, so only data set of id 4 is
     * returned.
     * @param: file a file that is created
     * @param: connectionUrl is a url of the database
     * @param: tableName a name of the table in the database
     * @return: result a JsonArray containing data set from .json file to import to TableView
     */
    public JsonArray getDataFromFile(File file, String connectionUrl, String tableName) {
        JsonArray result = null;
        List<Integer> allDbId = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("Select id from " + tableName)) {

            while (resultSet.next()) {
                allDbId.add(resultSet.getInt(1));
            }

            Object object = jsonParser.parse(new FileReader(file));
            JsonObject jsonObject = (JsonObject) object;
            result = (JsonArray) jsonObject.get(getPropertyFromFile(JSON_DATASET_NAME_PROPERTY));

            //TODO: works only if the order of ID is the same as the order of ID in TableView. Fix it.
            for (int i = 0; i < result.size(); i++) {
                for (int j = 0; j < allDbId.size(); j++) {
                    if (Integer.valueOf(String.valueOf(result.get(i).getAsJsonObject().get("id"))) == allDbId.get(j)) {
                        result.remove(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not process data from a .json file.", e.toString());
        }
        return result;
    }
    
    /* Import data from TableView of Individuals to created .json file
     * @param: file a file that is created
     */
    public void importToIndividuals(File file) {
        /* Establish connection */
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
             PreparedStatement preparedStatement = connection.prepareStatement("insert into content_individuals values (?, ?, ?, ?, ?, ?, ?)")) {

            /* Call JsonParser from library */
            JsonParser jsonParser = new JsonParser();
            Object object = jsonParser.parse(new FileReader(file));
            /* Create Json Object */
            JsonObject jsonObject = (JsonObject) object;

            /* Transform object to JsonArray using library */
            JsonArray jsonArray = (JsonArray) jsonObject.get(getPropertyFromFile(JSON_DATASET_NAME_PROPERTY));

            /* Get data from .json file */
            jsonArray = getDataFromFile(file, CONNECTION_URL_IND, TABLE_INDIVIDUALS);

            /* If returned array.size() <= 0, it means all ID's are the same in TableView and in .json file */
            if (jsonArray.size() <= 0) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not import .JSON data because all id's are matching.",
                        "Same IDENTIFICATION NUMBER SET in Table and in .JSON file.");
                return;
            }

            /* Retrieve data from JsonArray */
            for (int i = 0; i < jsonArray.size(); i++) {
                Integer id = Integer.valueOf(String.valueOf(jsonArray.get(i).getAsJsonObject().get("id")));
                String firstName = jsonArray.get(i).getAsJsonObject().get("firstName").getAsString();
                String lastName = jsonArray.get(i).getAsJsonObject().get("lastName").getAsString();
                String emailAdress = jsonArray.get(i).getAsJsonObject().get("emailAdress").getAsString();
                String dateLicenseValid = jsonArray.get(i).getAsJsonObject().get("dateLicenseValid").getAsString();
                Double amount = jsonArray.get(i).getAsJsonObject().get("amount").getAsDouble();

                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, emailAdress);
                preparedStatement.setString(5, dateLicenseValid);
                preparedStatement.setDouble(6, amount);
                /* We do not import or export notes, to avoid an error message, just setString on import to empty */
                preparedStatement.setString(7, "");

                /* Write data */
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not open a JSON  file...", e.getMessage());
            e.printStackTrace();
        }
    }

    /* Import data from TableView of Companies to created .json file
     * @param: file a file that is created
     */
    public void importToCompanies(File file) {
        /* Establish connection */
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
             PreparedStatement preparedStatement = connection.prepareStatement("insert into content_companies values (?, ?, ?, ?, ?, ?)")) {

            /* Call JsonParser from library */
            JsonParser jsonParser = new JsonParser();
            Object object = jsonParser.parse(new FileReader(file));
            /* Create Json Object */
            JsonObject jsonObject = (JsonObject) object;

            /* Transform object to JsonArray using library */
            JsonArray jsonArray = (JsonArray) jsonObject.get(getPropertyFromFile(JSON_DATASET_NAME_PROPERTY));

            /* Get data from .json file */
            jsonArray = getDataFromFile(file, CONNECTION_URL_COMP, TABLE_COMPANIES);

            /* If returned array.size() <= 0, it means all ID's are the same in TableView and in .json file */
            if (jsonArray.size() <= 0) {
                new ErrorAndExceptionHandler().showErrorAlert("Could not import .JSON data because all id's are matching.",
                        "Same IDENTIFICATION NUMBER SET in Table and in .JSON file.");
                return;
            }

            /* Retrieve data from JsonArray */
            for (int i = 0; i < jsonArray.size(); i++) {
                Integer id = Integer.valueOf(String.valueOf(jsonArray.get(i).getAsJsonObject().get("id")));
                String companyName = jsonArray.get(i).getAsJsonObject().get("companyName").getAsString();
                String emailAdress = jsonArray.get(i).getAsJsonObject().get("emailAdress").getAsString();
                String dateLicenseValid = jsonArray.get(i).getAsJsonObject().get("dateLicenseValid").getAsString();
                Double amount = jsonArray.get(i).getAsJsonObject().get("amount").getAsDouble();

                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, companyName);
                preparedStatement.setString(3, emailAdress);
                preparedStatement.setString(4, dateLicenseValid);
                preparedStatement.setDouble(5, amount);

                /* Write data */
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not open a JSON  file...", e.getMessage());
            e.printStackTrace();
        }
    }

    /* Retrieve data from Individuals database and writes it to the list
     * @param: resultSet allows to retrieve data from database
     * @param: items a list of Individuals Records structure
     */
    public void getRecordsIndividuals(ResultSet resultSet, List<RecordsIndividuals> items) {
        try {
            while (resultSet.next()) {
                RecordsIndividuals record = new RecordsIndividuals();

                record.setId(resultSet.getInt(1));
                record.setFirstName(resultSet.getString(2));
                record.setLastName(resultSet.getString(3));
                record.setEmailAdress(resultSet.getString(4));
                record.setDateLicenseValid(resultSet.getString(5));
                record.setAmount(resultSet.getDouble(6));
                items.add(record);
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not read data from the table...", e.getMessage());
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                new ErrorAndExceptionHandler().showErrorAlert("Failed to close resultSet after reading the data...", e.getMessage());
            }
        }
    }

    /* Retrieve data from Companies database and writes it to the list
     * @param: resultSet allows to retrieve data from database
     * @param: items a list of Companies Records structure
     */
    public void getRecordsCompanies(ResultSet resultSet, List<RecordsCompanies> items) {
        try {
            while (resultSet.next()) {
                RecordsCompanies record = new RecordsCompanies();

                record.setId(resultSet.getInt(1));
                record.setCompanyName(resultSet.getString(2));
                record.setEmailAdress(resultSet.getString(3));
                record.setDateLicenseValid(resultSet.getString(4));
                record.setAmount(resultSet.getDouble(5));
                items.add(record);
            }
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not read data from the table...", e.getMessage());
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                new ErrorAndExceptionHandler().showErrorAlert("Failed to close resultSet after reading the data...", e.getMessage());
            }
        }
    }

    /* Export data from database of Individuals TableView
     * @param: file a file that is created with .json extension
     * @param: items a list of id's to export of Individuals TableView
     */
    public void exportIndividuals(File file, List<Integer> items) {
        /* Use Gson class helper nad library */
        GsonHelper metadata = new GsonHelper();
        Gson gson = new Gson();
        List<RecordsIndividuals> list = new ArrayList<>();

        metadata.setRecordID(1);
        metadata.setRecordName("JSON DATA FROM TABLE INDIVIDUALS");

        /* If items.size() <=1 export all data from TableView, otherwise export selected */
        if (items.size() <= 1) {
            /* Establish connection */
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("Select * from content_individuals")) {

                /* Calls method that retrieves data from resultset and writes it to the list */
                getRecordsIndividuals(resultSet, list);

                metadata.setRecords(list);
                String json = gson.toJson(metadata);

                /* Call method that write all retrieved data from the list to actual file as string */
                writeDataToFile(json, file);

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert(EXPORT_JSON_FAIL_MESSAGE, e.getMessage());
            }
        } else {
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
                 Statement statement = connection.createStatement()) {

                for (int i = 0; i < items.size(); i++) {
                    ResultSet resultSet = statement.executeQuery("Select * from content_individuals where id = " + items.get(i));
                    getRecordsIndividuals(resultSet, list);
                }

                metadata.setRecords(list);
                String json = gson.toJson(metadata);
                writeDataToFile(json, file);

            } catch(Exception e){
                new ErrorAndExceptionHandler().showErrorAlert(EXPORT_JSON_FAIL_MESSAGE, e.getMessage());
            }
        }
    }

    /* Export data from database of Companies TableView
     * @param: file a file that is created with .json extension
     * @param: items a list of id's to export of Individuals TableView
     */
    public void exportCompanies(File file, List<Integer> items) {
        /* Use Gson class helper nad library */
        GsonHelper metadata = new GsonHelper();
        Gson gson = new Gson();
        List<RecordsCompanies> list = new ArrayList<>();

        metadata.setRecordID(1);
        metadata.setRecordName("JSON DATA FROM TABLE COMPANIES");

        /* If items.size() <=1 export all data from TableView, otherwise export selected */
        if (items.size() <= 1) {
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("Select * from content_companies")) {

                /* Calls method that retrieves data from resultset and writes it to the list */
                getRecordsCompanies(resultSet, list);

                metadata.setRecords(list);
                String json = gson.toJson(metadata);

                /* Call method that write all retrieved data from the list to actual file as string */
                writeDataToFile(json, file);

            } catch (Exception e) {
                new ErrorAndExceptionHandler().showErrorAlert(EXPORT_JSON_FAIL_MESSAGE, e.getMessage());
            }
        } else {
            try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
                 Statement statement = connection.createStatement()) {

                for (int i = 0; i < items.size(); i++) {
                    ResultSet resultSet = statement.executeQuery("Select * from content_companies where id = " + items.get(i));
                    getRecordsCompanies(resultSet, list);
                }

                metadata.setRecords(list);
                String json = gson.toJson(metadata);
                writeDataToFile(json, file);

            } catch(Exception e){
                new ErrorAndExceptionHandler().showErrorAlert(EXPORT_JSON_FAIL_MESSAGE, e.getMessage());
            }
        }
    }

    /* Method calls either export as Individuals or as Companies depending on Tab selection
    * @param: file created .json file
    * @param: tabIndex index of a Tab (Individuals or Companies)
    * */
    public void exportToJson(File file, List<Integer> items, int tabIndex) {
        if (tabIndex == 0)
            exportIndividuals(file, items);
        else
            exportCompanies(file, items);
    }

    /*
     * Write data to created .json file.
     * @param: json is a string that contains retrieved data from database as string
     * @param: file created .json file
     */
    public void writeDataToFile(String json, File file) {
        try {
            file.createNewFile();
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not create a JSON file...", e.getMessage());
        }

        /* Open writers and buffers to be able to write the information */
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, FILE_ENCODING);
             BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter, true)) {

            /* Write the information */
            printWriter.write(json);

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not write data to created JSON file...", e.getMessage());
        }
         new ErrorAndExceptionHandler().showConfirmationAlert("JSON file has been successfully created.");
    }
}
