package sample.core.properties;

import sample.core.handlers.ErrorAndExceptionHandler;

import java.io.File;
import java.io.FileOutputStream;

/*
 *   This class initializes properties.
 */

public class PropertiesInitializer {
    private static final String FILE_NAME = "configuration.properties";

    /* Create configuration file */
    public void createConfigurationFile() {
        try {
            File file = new File(FILE_NAME);

            /* if file exists do nothing, otherwise set properties */
            if (file.exists())
                return;
            else
                setProperties();

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not create configuration file...", e.toString());
        }
    }

    /* Set's default properties to .properties file */
    private void setProperties() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            java.util.Properties properties = new java.util.Properties();

            properties.setProperty("datePreferenceIndex", "0");
            properties.setProperty("datePreferenceString", "dd/MM/yyyy");
            properties.setProperty("closeDialogWindow", "false");
            properties.setProperty("allowUploadIncompleteForm", "false");
            properties.setProperty("jsonDatasetName", "records");
            properties.setProperty("currencyPreferenceIndex", "0");
            properties.setProperty("currencyUsed", "EUR");
            properties.setProperty("priceForIndividuals", "0.0");
            properties.setProperty("priceForCompanies", "0.0");
            /* write to file */
            properties.store(fileOutputStream, null);

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorAndExceptionHandler().showErrorAlert("Could not set properties...", e.toString());
        }
    }
}
