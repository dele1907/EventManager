package de.eventmanager.core.database.Communication;

import java.sql.Connection;
import java.util.Optional;

import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;
import helper.LoggerHelper;
import helper.PasswordHelper;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import static org.jooq.generated.tables.User.USER;

public class UserDatabaseConnector {

    //#region Constants
    private static final String USER_ADDED = "User added successfully";
    private static final String USER_NOT_ADDED = "Error adding user: ";
    private static final String USER_NOT_READ = "Error reading user: ";
    private static final String USER_UPDATED = "User updated successfully";
    private static final String USER_NOT_UPDATED = "Error updating user: ";
    private static final String USER_NOT_FOUND = "No user found with the given ID";
    private static final String USER_DELETED = "User deleted successfully";
    private static final String USER_NOT_DELETED = "Error deleting user: ";

    //#endregion Constants

    // Benutzer hinzufügen (CREATE)
    public static boolean createNewUser(User user) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.insertInto(USER,
                            USER.USERID,
                            USER.FIRSTNAME,
                            USER.LASTNAME,
                            USER.BIRTHDATE,
                            USER.EMAIL,
                            USER.PASSWORD,
                            USER.PHONENUMBER,
                            USER.ISADMIN)
                    .values(
                            user.getUserID(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getDateOfBirth(),
                            user.getEMailAddress(),
                            user.getPassword(),
                            user.getPhoneNumber(),
                            user.getRole().equals(Role.ADMIN) ? true : false)
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_ADDED);

                return true;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_ADDED + exception.getMessage());
        }

        return false;
    }

    // Benutzer laden (READ) anhand der ID
    public static Optional<User> readUserByID(String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(USER)
                    .where(USER.USERID.eq(userID))
                    .fetchOne();

            if (record != null) {
                User user = new User(
                        record.get(USER.USERID),
                        record.get(USER.FIRSTNAME),
                        record.get(USER.LASTNAME),
                        record.get(USER.BIRTHDATE),
                        record.get(USER.EMAIL),
                        record.get(USER.PASSWORD),
                        record.get(USER.PHONENUMBER),
                        record.get(USER.ISADMIN)
                );

                return Optional.of(user);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    // Benutzer laden (READ) anhand der E-Mail-Adresse
    public static Optional<User> readUserByEMail(String eMailAddress) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Record record = create.select()
                    .from(USER)
                    .where(USER.EMAIL.eq(eMailAddress))
                    .fetchOne();

            if (record != null) {

                User user = new User(
                        record.get(USER.USERID),
                        record.get(USER.FIRSTNAME),
                        record.get(USER.LASTNAME),
                        record.get(USER.BIRTHDATE),
                        record.get(USER.EMAIL),
                        record.get(USER.PASSWORD),
                        record.get(USER.PHONENUMBER),
                        record.get(USER.ISADMIN)
                );

                return Optional.of(user);
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_READ + exception.getMessage());
        }

        return Optional.empty();
    }

    // Benutzer ändern (UPDATE)
    public static boolean updateUser(User user) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsUpdated = create.update(USER)
                    .set(USER.FIRSTNAME, user.getFirstName())
                    .set(USER.LASTNAME, user.getLastName())
                    .set(USER.BIRTHDATE, user.getDateOfBirth())
                    .set(USER.EMAIL, user.getEMailAddress())
                    .set(USER.PASSWORD, user.getPassword())
                    .set(USER.PHONENUMBER, user.getPhoneNumber())
                    .where(USER.USERID.eq(user.getUserID()))
                    .execute();

            if (rowsUpdated > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_UPDATED);

                return true;
            } else {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_NOT_FOUND);

                return false;
            }

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_UPDATED + exception.getMessage());

            return false;
        }
    }

    // Benutzer löschen (DELETE)
    public static boolean deleteUserByID(String userID) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.deleteFrom(USER)
                    .where(USER.USERID.eq(userID))
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_DELETED);
            } else {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_DELETED + exception.getMessage());

            return false;
        }
    }

    public static boolean deleteUserByEmail(String email) {

        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            int rowsAffected = create.deleteFrom(USER)
                    .where(USER.EMAIL.eq(email))
                    .execute();

            if (rowsAffected > 0) {
                LoggerHelper.logInfoMessage(UserDatabaseConnector.class, USER_DELETED);
            } else {
                LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_FOUND);
            }

            return rowsAffected > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_DELETED + exception.getMessage());

            return false;
        }
    }

    public static boolean getAdminUserIsPresentInDatabase() {
        try (Connection connection = DatabaseConnector.connect()) {

            DSLContext create = DSL.using(connection);

            Result<Record> record = create.select()
                    .from(USER)
                    .where(USER.ISADMIN.eq(true))
                    .fetch();

            return record.size() > 0;

        } catch (Exception exception) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, USER_NOT_READ + exception.getMessage());
        }

        return false;
    }

    //#region Registration & Authentication

    public static boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    /**
     * <h3>Validate Password</h3>
     * <p>
     * {@code isValidPassword()} checks whether a given password contains any restricted characters from a predefined list.
     * If the password contains any of the restricted characters, the method returns {@code false}. If no restricted characters are found, it returns {@code true}.
     * </p>
     */

    private static boolean isValidPassword(String password) {
        char[] restrictedCharacters = {' ', '$', '@', '§', '&', '%', 'ä', 'ö', 'ü', 'ß', 'Ä', 'Ü', 'Ö'};

        for (var restricted : restrictedCharacters) {
            if (password.indexOf(restricted) != -1) {
                return false;
            }
        }

        return true;
    }

    private static boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {

            System.out.println("Wrong password!");

            return false;
        }

        return checkPassword.equals(password);
    }

    private static boolean comparingEmailAddress(String emailAddress) {
        if (readUserByEMail(emailAddress).isEmpty()) {

            LoggerHelper.logInfoMessage(UserDatabaseConnector.class, "Email address not found");
            return false;
        }

        return true;
    }

    /**
     * <h3>User Login Authentication</h3>
     The method {@code authenticationUserLogin()} accepts an email address and password, authenticates the user
     by checking if the email exists, and verifies whether the provided password matches the one stored
     in the DB. If both conditions are met, the user is successfully logged in.
     */

    public static boolean authenticationUserLogin(String email, String plainPassword) {
        Optional<User> userOptional = readUserByEMail(email);

        if (userOptional.isEmpty()) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, "Email address not found");

            return false;
        }

        return PasswordHelper.verifyPassword(plainPassword, userOptional.get().getPassword());
    }
    //#endregion Registration & Authentication

}
