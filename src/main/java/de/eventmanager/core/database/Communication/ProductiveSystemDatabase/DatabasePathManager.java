package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;

import helper.LoggerHelper;

import java.io.*;

public class DatabasePathManager {
    private static final String USERS_HOME_DIRECTORY = System.getProperty("user.home");
    private static File eventManagerDirectory = new File(USERS_HOME_DIRECTORY, "EventManagerFiles");
    private static File dataBaseFile = new File(eventManagerDirectory.getAbsolutePath(), "eventmanagerdata.sqlite");

    public static String loadDatabasePath(boolean isProductiveSystem) {
        if (!isProductiveSystem) {
            return "src/main/resources/eventmanager.sqlite";
        }

        if (!eventManagerDirectory.exists()) {
            createDatabaseFileDirectory();
        }

        if (!dataBaseFile.exists()) {
            createDatabaseFile();
        }

        return dataBaseFile.getAbsolutePath();
    }

    private static void createDatabaseFile() {
        try {
            dataBaseFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LoggerHelper.logErrorMessage(
                    DatabasePathManager.class,
                    "Error creating database file: " +
                            dataBaseFile.getAbsolutePath()
            );
        }
    }

    private static void createDatabaseFileDirectory() {
        try {
            eventManagerDirectory.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerHelper.logErrorMessage(
                    DatabasePathManager.class,
                    "Error creating database file directory: " +
                            eventManagerDirectory.getAbsolutePath()
            );
        }
    }
}

