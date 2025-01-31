package de.eventmanager.core.database.Communication;

import de.eventmanager.core.users.User;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.jooq.generated.tables.User.USER;
import static org.junit.jupiter.api.Assertions.*;

public class UserDatabaseConnectorTestDrive {

    User testUser;
    User testUserUpdated;

    /**
     * Clean up the database after testing
     * */
    /*@AfterAll
    static void cleanUp() throws SQLException {

        try (Connection cleanupConnection = DatabaseConnector.connect()) {
           DSLContext cleanupDsl = DSL.using(cleanupConnection, SQLDialect.SQLITE);
            cleanupDsl.deleteFrom(USER).execute();
        }
    }*/

    //#region successful CRUD operations

    /**
     * Test creating and reading a user
     * */
    @Test
    public void testCreateUserAndReadByID() {

        User testUser = new User("createTestUserDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.create@testmail.com", "Password123", "1234567890", false);

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID("createTestUserDatabaseConnector");
        assertTrue(userFromDatabase.isPresent(), "User not found after creation.");
        assertEquals("createTestUserDatabaseConnector", userFromDatabase.get().getUserID());
        assertEquals("Max", userFromDatabase.get().getFirstName());
        assertEquals("Mustermann", userFromDatabase.get().getLastName());
        assertEquals("1980-01-10", userFromDatabase.get().getDateOfBirth());
        assertEquals("max.create@testmail.com", userFromDatabase.get().getEMailAddress());
        assertEquals("Password123", userFromDatabase.get().getPassword());
        assertEquals("1234567890", userFromDatabase.get().getPhoneNumber());

        UserDatabaseConnector.deleteUserByID("createTestUserDatabaseConnector");
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

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail("martina.update@testmail.com");
        assertTrue(userFromDatabase.isPresent(), "User not found after update.");
        assertEquals("updateTestUserDatabaseConnector", userFromDatabase.get().getUserID());
        assertEquals("Martina", userFromDatabase.get().getFirstName());
        assertEquals("Musterfrau", userFromDatabase.get().getLastName());
        assertEquals("1970-10-01", userFromDatabase.get().getDateOfBirth());
        assertEquals("martina.update@testmail.com", userFromDatabase.get().getEMailAddress());
        assertEquals("Password987", userFromDatabase.get().getPassword());
        assertEquals("0987654321", userFromDatabase.get().getPhoneNumber());

        UserDatabaseConnector.deleteUserByID("updateTestUserDatabaseConnector");
    }

    /**
     * Test deleting a user by ID
     * */
    @Test
    public void testDeleteUserByID() {

        testUser = new User("deleteTestUserDatabaseConnector1", "Max", "Mustermann", "1980-01-10",
                "max.delete1@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);

        boolean userDeletedByID = UserDatabaseConnector.deleteUserByID("deleteTestUserDatabaseConnector1");
        assertTrue(userDeletedByID, "User deletion failed but should not.");
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID("deleteTestUserDatabaseConnector1");
        assertFalse(userFromDatabase.isPresent(), "User was found but should not.");
    }

    /**
     * Test deleting a user by eMail address
     * */
    @Test
    public void testDeleteUserByEmail() {

        testUser = new User("deleteTestUserDatabaseConnector2", "Max", "Mustermann", "1980-01-10",
                "max.delete2@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);

        boolean userDeletedByEmail = UserDatabaseConnector.deleteUserByEmail("max.delete2@testmail.com");
        assertTrue(userDeletedByEmail, "User deletion failed but should not.");
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail("max.delete2@testmail.com");
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
        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID("createFailTestUserDatabaseConnector");
        assertTrue(userFromDatabase.isPresent(), "User not found after first creation.");

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertFalse(userCreated, "Second user creation was successful but should not.");

        UserDatabaseConnector.deleteUserByID("createFailTestUserDatabaseConnector");
    }

    /**
     * Test reading a user with invalid ID
     * */
    @Test
    public void testReadUserByIDFailed() {

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByID("invalidIDToRead");
        assertFalse(userFromDatabase.isPresent(), "User was found by ID address but should not.");
    }

    /**
     * Test reading a user with invalid eMail address
     * */
    @Test
    public void testReadUserByEMailFailed() {

        Optional<User> userFromDatabase = UserDatabaseConnector.readUserByEMail("invalidEmailToRead@testmail.com");
        assertFalse(userFromDatabase.isPresent(), "User was found by eMail address but should not.");
    }

    /**
     * Test that updating is only possible if there is an entry in the database
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

        boolean userDeleted = UserDatabaseConnector.deleteUserByID("invalidIDToDelete");
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    /**
     * Test deleting a user with invalid eMail address
     * */
    @Test
    public void testDeleteUserByEmailFailed() {

        boolean userDeleted = UserDatabaseConnector.deleteUserByEmail("invalidEmailToDelete@testmail.com");
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    //#endregion failed CRUD operations

}