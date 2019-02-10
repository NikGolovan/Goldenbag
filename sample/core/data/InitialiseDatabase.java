package sample.core.data;

import sample.core.handlers.ErrorAndExceptionHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/*
 *   This class creates tables in SQLite3 database.
 */

public class InitialiseDatabase {
    private static final String CONNECTION_URL_IND = "jdbc:sqlite:clients_individuals.db";
    private static final String CONNECTION_URL_COMP = "jdbc:sqlite:clients_companies.db";

    /* Create table for Individuals */
    public void createTableIndividuals() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_IND);
             Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists content_individuals (id INTEGER PRIMARY KEY, FirstName VARCHAR(60), " +
                    "LastName VARCHAR(60), EmailAdress VARCHAR(80), LicenseValidFrom VARCHAR(60), Amount REAL, Note VARCHAR(200))");
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not create table for database...", e.toString());
        }
    }
    /* Create table for Companies */
    public void createTableICompanies() {
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL_COMP);
             Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists content_companies (id INTEGER PRIMARY KEY, CompanyName VARCHAR(60), " +
                    "EmailAddress VARCHAR(80), LicenseValidFrom VARCHAR(60), Amount REAL, Note VARCHAR(200))");
        } catch (Exception e) {
            new ErrorAndExceptionHandler().showErrorAlert("Could not create table for database...", e.toString());
        }
    }
}
