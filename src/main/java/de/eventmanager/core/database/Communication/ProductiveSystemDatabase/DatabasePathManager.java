package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;

import java.io.*;

public class DatabasePathManager {
    private static final String PATH_FILE = "database_path.txt";

    public static String loadDatabasePath(boolean isProductiveSystem) {
        if (!isProductiveSystem) {
            return "src/main/resources/eventmanager.sqlite";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE))) {
            String path = reader.readLine();
            return (path != null) ? path : "";
        } catch (IOException e) {
            return "";
        }
    }

    public static void saveDatabasePath(String path, boolean isProductiveSystem) {
        if (!isProductiveSystem) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE))) {
            writer.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@TODO: remove flush before release
    public static void flushDatabasePath(boolean flush) {
        if (!flush) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidPath(String path, boolean isProductiveSystem) {
        if (!isProductiveSystem) {
            return true;
        }

        File file = new File(path);
        return file.exists() || file.getParentFile().canWrite();
    }
}

