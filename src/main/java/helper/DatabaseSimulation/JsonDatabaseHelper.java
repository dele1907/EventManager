package helper.DatabaseSimulation;

import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.reflect.TypeToken;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;

public class JsonDatabaseHelper {
    private static final String DATABASE_PATH = "src/main/resources/databasesimulation.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Optional<JsonArray> readUsersFromJson() {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);
            if (database != null && !database.isEmpty()) {
                JsonObject root = database.get(0);
                JsonArray users = root.getAsJsonArray("users");

                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success reading users from json."
                );

                return Optional.of(users);
            }
        } catch (Exception e) {
            e.printStackTrace();

            LoggerHelper.logErrorMessage(
                    JsonDatabaseHelper.class,
                    "Error reading users from json."
            );
        }

        return Optional.empty();
    }

    public static Optional<JsonArray> readPermissionsFromJson() {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);

            if (database != null && !database.isEmpty()) {
                JsonObject root = database.get(0);
                JsonArray permissions = root.getAsJsonArray("permissions");

                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success reading permissions from json."
                );

                return Optional.of(permissions);
            }
        } catch (Exception e) {
            e.printStackTrace();

            LoggerHelper.logErrorMessage(
                    JsonDatabaseHelper.class,
                    "Error reading permissions from json."
            );
        }

        return Optional.empty();
    }

    public static Optional<JsonArray> readUserHasPermissionFromJson() {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);

            if (database != null && !database.isEmpty()) {
                JsonObject root = database.get(0);
                JsonArray permissions = root.getAsJsonArray("has");

                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success reading has relation from json."
                );

                return Optional.of(permissions);
            }
        } catch (Exception e) {
            e.printStackTrace();

            LoggerHelper.logErrorMessage(
                    JsonDatabaseHelper.class,
                    "Error reading has relation from json."
            );

        }

        return Optional.empty();
    }

    public static boolean addUserToJson(String userID, String firstName, String lastName, String dateOfBirth, String email, int phoneNumber, String password, boolean isAdmin) {
        Optional<JsonArray> users = readUsersFromJson();

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

            LoggerHelper.logInfoMessage(
                    JsonDatabaseHelper.class,
                    "Success adding user to json."
            );

            return writeToJson(userList, readPermissionsFromJson().get());
        }
        LoggerHelper.logErrorMessage(
                JsonDatabaseHelper.class,
                "Error adding user to json."
        );

        return false;
    }

    public static boolean addPermissionToJson(String permissionID, String permissionName, boolean isAdminPermission) {
        Optional<JsonArray> permissions = readPermissionsFromJson();

        if (permissions.isPresent()) {
            JsonArray permissionList = permissions.get();
            JsonObject newPermission = new JsonObject();
            newPermission.addProperty("permissionID", permissionID);
            newPermission.addProperty("permissionName", permissionName);
            newPermission.addProperty("isAdminPermission", isAdminPermission);
            permissionList.add(newPermission);

            LoggerHelper.logInfoMessage(
                    JsonDatabaseHelper.class,
                    "Success adding permission to json."
            );

            return writePermissionsToJson(permissionList);
        }
        LoggerHelper.logErrorMessage(
                JsonDatabaseHelper.class,
                "Error adding permission to json."
        );

        return false;
    }

    public static boolean writeToJson(JsonArray users, JsonArray permissions) {
        return writeUsersToJson(users) && writePermissionsToJson(permissions);
    }

    public static boolean writeUsersToJson(JsonArray users) {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);
            JsonObject root = database.get(0);
            root.add("users", users);

            try (FileWriter fileWriter = new FileWriter(DATABASE_PATH)) {
                gson.toJson(database, fileWriter);

                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success users data to json."
                );

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LoggerHelper.logErrorMessage(
                    JsonDatabaseHelper.class,
                    "Error writing users to json."
            );

            return false;
        }
    }

    public static boolean writePermissionsToJson(JsonArray permissions) {
        try (FileReader fileReader = new FileReader(DATABASE_PATH)) {
            Type databaseType = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> database = gson.fromJson(fileReader, databaseType);
            JsonObject root = database.get(0);
            root.add("permissions", permissions);

            try (FileWriter fileWriter = new FileWriter(DATABASE_PATH)) {
                gson.toJson(database, fileWriter);

                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success writing permissions to json."
                );

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LoggerHelper.logErrorMessage(
                    JsonDatabaseHelper.class,
                    "Error writing permissions to json."
            );

            return false;
        }
    }

    public static boolean removeUserByEmailFromJson(String email) {
        Optional<JsonArray> users = readUsersFromJson();

        if (users.isPresent()) {
            JsonArray userList = users.get();
            boolean removed = userList.asList().removeIf(user -> email.equals(user.getAsJsonObject().get("eMailAddress").getAsString()));

            if (removed) {
                LoggerHelper.logInfoMessage(
                        JsonDatabaseHelper.class,
                        "Success removing user with eMail: " + email + " from users in json."
                );

                return writeToJson(userList, readPermissionsFromJson().get());
            }
        }
        LoggerHelper.logErrorMessage(
                JsonDatabaseHelper.class,
                "Error removing user from json."
        );

        return false;
    }

    public static Optional<User> getUserByEmailFromJson(String email) {
        Optional<JsonArray> users = readUsersFromJson();
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
                    LoggerHelper.logInfoMessage(
                            JsonDatabaseHelper.class,
                            "Success getting user with eMail: " + email + " from users in json."
                    );

                    return Optional.of(user);
                }
            }
        }
        LoggerHelper.logErrorMessage(
                JsonDatabaseHelper.class,
                "Error getting user with eMail: " + email + " from users in json."
        );

        return Optional.empty();
    }

    public static Optional<List<Permission>> getPermissionsForUserFromJson(User user) {
        Optional<JsonArray> userHasPermissions = readUserHasPermissionFromJson();
        Optional<JsonArray> permissions = readPermissionsFromJson();

        if (userHasPermissions.isPresent() && permissions.isPresent()) {
            JsonArray userHasPermissionsArray = userHasPermissions.get();
            JsonArray permissionsArray = permissions.get();
            List<Permission> userPermissions = new ArrayList<>();

            for (JsonElement element : userHasPermissionsArray) {
                JsonObject userHasPermission = element.getAsJsonObject();
                if (user.getUserID().equals(userHasPermission.get("userID").getAsString())) {
                    String permissionID = userHasPermission.get("permissionID").getAsString();
                    for (JsonElement permElement : permissionsArray) {
                        JsonObject permission = permElement.getAsJsonObject();
                        if (permissionID.equals(permission.get("permissionID").getAsString())) {
                            Permission userPermission = new Permission(
                                    permission.get("permissionID").getAsString(),
                                    permission.get("permissionName").getAsString(),
                                    permission.get("isAdminPermission").getAsBoolean()
                            );

                            userPermissions.add(userPermission);
                        }
                    }
                }
            }
            LoggerHelper.logInfoMessage(
                    JsonDatabaseHelper.class,
                    "Success getting permissions for user " + user + " from json."
            );

            return Optional.of(userPermissions);
        }
        LoggerHelper.logErrorMessage(
                JsonDatabaseHelper.class,
                "Error getting permissions for user " + user + " from json."
        );

        return Optional.empty();
    }

    public static boolean findAndUpdateUser(User user) {
        Optional<JsonArray> usersOpt = readUsersFromJson();

        if (usersOpt.isPresent()) {
            JsonArray users = usersOpt.get();

            for (JsonElement element : users) {
                JsonObject userJson = element.getAsJsonObject();

                if (user.getUserID().equals(userJson.get("userID").getAsString())) {
                    userJson.addProperty("firstName", user.getFirstName());
                    userJson.addProperty("lastName", user.getLastName());
                    userJson.addProperty("dateOfBirth", user.getDateOfBirth());
                    userJson.addProperty("eMailAddress", user.getEMailAddress());
                    userJson.addProperty("password", user.getPassword());
                    userJson.addProperty("phoneNumber", user.getPhoneNumber());
                    userJson.addProperty("isAdmin", user.isAdmin());

                    return writeUsersToJson(users);
                }
            }
            LoggerHelper.logInfoMessage(JsonDatabaseHelper.class, "No user found with the given ID");

        } else {
            LoggerHelper.logErrorMessage(JsonDatabaseHelper.class, "Error reading users from json.");
        }

        return false;
    }

    public static boolean getUserHasSpecificPermission(User user, String permissionID) {
        Optional<List<Permission>> permissions = getPermissionsForUserFromJson(user);

        if (permissions.isPresent()) {
            for (Permission permission : permissions.get()) {

                if (permission.getPermissionID().equals(permissionID)) {
                    return true;
                }
            }
        }

        return false;
    }
}