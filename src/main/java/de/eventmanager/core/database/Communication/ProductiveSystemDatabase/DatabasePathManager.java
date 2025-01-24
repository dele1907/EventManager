package de.eventmanager.core.database.Communication.ProductiveSystemDatabase;

import java.io.*;

public class DatabasePathManager {
    private static final String PATH_FILE = "database_path.txt";

    public static String loadDatabasePath() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_FILE))) {
            String path = reader.readLine();
            return (path != null) ? path : "";
        } catch (IOException e) {
            return "";
        }
    }

    public static void saveDatabasePath(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_FILE))) {
            writer.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidPath(String path) {
        File file = new File(path);
        return file.exists() || file.getParentFile().canWrite();
    }
}

