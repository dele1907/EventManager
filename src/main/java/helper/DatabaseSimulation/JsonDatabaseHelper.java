package helper.DatabaseSimulation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import com.google.common.reflect.TypeToken;
import de.eventmanager.core.users.User;

public class JsonDatabaseHelper {
    private static final String DATABASE_PATH = "src/main/resources/databasesimulation.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Optional<JsonArray> readUsers() {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);
            if (database != null && !database.isEmpty()) {
                JsonObject root = database.get(0);
                JsonArray users = root.getAsJsonArray("Users");

                return Optional.of(users);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static boolean addUser(String userID, String firstName, String lastName, String dateOfBirth, String email, int phoneNumber, String password, boolean isAdmin) {
        Optional<JsonArray> users = readUsers();

        if (users.isPresent()) {
            JsonArray userList = users.get();
            JsonObject newUser = new JsonObject();
            newUser.addProperty("userID", userID);
            newUser.addProperty("firstName", firstName);
            newUser.addProperty("lastName", lastName);
            newUser.addProperty("dateOfBirth", dateOfBirth);
            newUser.addProperty("eMailAddress", email);
            newUser.addProperty("password", password);
            newUser.addProperty("phoneNumber", phoneNumber);
            newUser.addProperty("isAdmin", isAdmin);
            userList.add(newUser);

            return writeUsers(userList);
        }

        return false;
    }

    public static boolean writeUsers(JsonArray users) {
        try (FileWriter fileWriter = new FileWriter(DATABASE_PATH)) {
            JsonObject root = new JsonObject();
            root.add("Users", users);
            gson.toJson(List.of(root), fileWriter);

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    public static boolean removeUserByEmail(String email) {
        Optional<JsonArray> users = readUsers();

        if (users.isPresent()) {
            JsonArray userList = users.get();
            boolean removed = userList.asList().removeIf(user -> email.equals(user.getAsJsonObject().get("eMailAddress").getAsString()));

            if (removed) {
                return writeUsers(userList);
            }
        }

        return false;
    }

    public static Optional<User> getUserByEmail(String email) {
        Optional<JsonArray> users = readUsers();
        if (users.isPresent()) {

            for (int i = 0; i < users.get().size(); i++) {
                JsonObject userJson = users.get().get(i).getAsJsonObject();

                if (email.equals(userJson.get("eMailAddress").getAsString())) {
                    User user = new User(
                            userJson.get("userID").getAsString(),
                            userJson.get("firstName").getAsString(),
                            userJson.get("lastName").getAsString(),
                            userJson.get("dateOfBirth").getAsString(),
                            userJson.get("eMailAddress").getAsString(),
                            userJson.get("password").getAsString(),
                            userJson.get("phoneNumber").getAsInt(),
                            userJson.get("isAdmin").getAsBoolean()
                    );

                    return Optional.of(user);
                }
            }
        }

        return Optional.empty();
    }
}