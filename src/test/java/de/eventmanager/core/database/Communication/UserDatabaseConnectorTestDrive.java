package de.eventmanager.core.database.Communication;

import de.eventmanager.core.users.User;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserDatabaseConnectorTestDrive {

    private User testUser, testUserUpdated;

    private static final String INVALID_USER_ID = "invalidUserIDForUserDatabaseConnector";
    private static final String INVALID_USER_EMAIL = "invalidEmailForUserDatabaseConnector";

    //#region successful CRUD operations

    /**
     * Test creating and reading a user
     * */
    @Test
    public void testCreateUserAndReadByID() {

        testUser = new User("createTestUserDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.create@testmail.com", "Password123", "1234567890", false);

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID(testUser.getUserID());
        assertTrue(userFromDatabase.isPresent(), "User not found after creation.");
        assertEquals(testUser.getUserID(), userFromDatabase.get().getUserID());
        assertEquals(testUser.getFirstName(), userFromDatabase.get().getFirstName());
        assertEquals(testUser.getLastName(), userFromDatabase.get().getLastName());
        assertEquals(testUser.getDateOfBirth(), userFromDatabase.get().getDateOfBirth());
        assertEquals(testUser.getEMailAddress(), userFromDatabase.get().getEMailAddress());
        assertEquals(testUser.getPassword(), userFromDatabase.get().getPassword());
        assertEquals(testUser.getPhoneNumber(), userFromDatabase.get().getPhoneNumber());

        UserDatabaseConnector.deleteUserByID(testUser.getUserID());
    }

    /**
     * Test updating and reading a user
     * */
    @Test
    public void testUpdateUserAndReadByEmail() {

        testUser = new User("updateTestUserDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.update@testmail.com", "Password123", "1234567890", false);
        testUserUpdated = new User("updateTestUserDatabaseConnector", "Martina", "Musterfrau", "1970-10-01",
                "martina.update@testmail.com", "Password987", "0987654321", false);

        UserDatabaseConnector.createNewUser(testUser);

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);
        assertTrue(userUpdated, "User update failed but should not.");

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail(testUserUpdated.getEMailAddress());
        assertTrue(userFromDatabase.isPresent(), "User not found after update.");
        assertEquals(testUserUpdated.getUserID(), userFromDatabase.get().getUserID());
        assertEquals(testUserUpdated.getFirstName(), userFromDatabase.get().getFirstName());
        assertEquals(testUserUpdated.getLastName(), userFromDatabase.get().getLastName());
        assertEquals(testUserUpdated.getDateOfBirth(), userFromDatabase.get().getDateOfBirth());
        assertEquals(testUserUpdated.getEMailAddress(), userFromDatabase.get().getEMailAddress());
        assertEquals(testUserUpdated.getPassword(), userFromDatabase.get().getPassword());
        assertEquals(testUserUpdated.getPhoneNumber(), userFromDatabase.get().getPhoneNumber());

        UserDatabaseConnector.deleteUserByID(testUser.getUserID());
    }

    /**
     * Test deleting a user by ID
     * */
    @Test
    public void testDeleteUserByID() {

        testUser = new User("deleteByIDTestUserDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.deleteID@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);

        boolean userDeletedByID = UserDatabaseConnector.deleteUserByID(testUser.getUserID());
        assertTrue(userDeletedByID, "User deletion failed but should not.");
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID(testUser.getUserID());
        assertFalse(userFromDatabase.isPresent(), "User was found but should not.");
    }

    /**
     * Test deleting a user by eMail address
     * */
    @Test
    public void testDeleteUserByEmail() {

        testUser = new User("deleteByEmailTestUserDatabaseConnector2", "Max", "Mustermann", "1980-01-10",
                "max.deleteEmail@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);

        boolean userDeletedByEmail = UserDatabaseConnector.deleteUserByEmail(testUser.getEMailAddress());
        assertTrue(userDeletedByEmail, "User deletion failed but should not.");
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail(testUser.getEMailAddress());
        assertFalse(userFromDatabase.isPresent(), "User was found but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that created users are unique
     * */
    @Test
    public void testCreateUserFailed() {

        testUser = new User("createFailTestUserDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.createfail@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID(testUser.getUserID());
        assertTrue(userFromDatabase.isPresent(), "User not found after first creation.");

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertFalse(userCreated, "Second user creation was successful but should not.");

        UserDatabaseConnector.deleteUserByID(testUser.getUserID());
    }

    /**
     * Test reading a user with invalid ID
     * */
    @Test
    public void testReadUserByIDFailed() {

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID(INVALID_USER_ID);
        assertFalse(userFromDatabase.isPresent(), "User was found by ID but should not.");
    }

    /**
     * Test reading a user with invalid eMail address
     * */
    @Test
    public void testReadUserByEMailFailed() {

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail(INVALID_USER_EMAIL);
        assertFalse(userFromDatabase.isPresent(), "User was found by eMail address but should not.");
    }

    /**
     * Test that updating a user is only possible if there is an entry in the database
     * */
    @Test

    public void testUpdateUserFailed() {

        testUserUpdated = new User("updateFailTestUserDatabaseConnector", "Martina", "Musterfrau", "1970-10-01",
                "martina.updatefail@testmail.com", "Password987", "0987654321", false);

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);
        assertFalse(userUpdated, "User update was successful but should not.");
    }

    /**
     * Test deleting a user with invalid ID
     * */
    @Test
    public void testDeleteUserByIDFailed() {

        boolean userDeleted = UserDatabaseConnector.deleteUserByID(INVALID_USER_ID);
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    /**
     * Test deleting a user with invalid eMail address
     * */
    @Test
    public void testDeleteUserByEmailFailed() {

        boolean userDeleted = UserDatabaseConnector.deleteUserByEmail(INVALID_USER_EMAIL);
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}