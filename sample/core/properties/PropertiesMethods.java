package sample.core.properties;

import sample.core.handlers.ErrorAndExceptionHandler;

import java.io.FileInputStream;
import java.util.Properties;

/*
 *   This class contains methods to work with .properties file.
 */

public abstract class PropertiesMethods {
    private static final String FILE_NAME = "configuration.properties";

    /* Get a specific property from .properties file
     * @param: propertyName name of the property to be returned
     * @return: property as string
     */
    public String getPropertyFromFile(String propertyName) {
        Properties properties = new Properties();
        String property = null;

        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME)) {
            properties.load(fileInputStream);

            property = properties.getProperty(propertyName);

        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not get property from .properties file...", e.toString());
        }
        return property;
    }
}
