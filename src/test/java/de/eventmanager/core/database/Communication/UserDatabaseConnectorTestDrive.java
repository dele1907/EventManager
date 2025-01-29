package de.eventmanager.core.database.Communication;

import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDatabaseConnectorTestDrive {

    private User testUser;
    private User testUserUpdated;
    private boolean skipSetUp = false;
    private boolean skipCleanUp = false;

    /**
     * Create two users before each test
     * */
    @BeforeEach
    public void setUp() {

        if(skipSetUp) {

            return;
        }

        testUser = new User("testUserID", "Max", "Mustermann", "1980-01-10",
                "max.mustermann@mail.com", "password123", "1234567890", false);
        testUserUpdated = new User("testUserID", "Maximilian", "Mustermann-Meyer", "1980-10-01",
                "max.m@mail.com", "password987", "1357902468", false);
    }

    /**
     * Clean up the database after testing
     * */
    @AfterEach
    public void cleanUp() {

        if(skipCleanUp) {

            return;
        }

        UserDatabaseConnector.deleteUserByID("testUserID");
    }

    /**
     * Test creating, updating and deleting users
     * */
    @Test
    public void testCreateUpdateDeleteUser() {

        skipCleanUp = true;

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);
        assertTrue(userUpdated, "User update failed but should not.");

        boolean userDeleted = UserDatabaseConnector.deleteUserByID(testUser.getUserID());
        assertTrue(userDeleted, "User deletion failed but should not.");
    }

    /**
     * Test that created users are unique
     * */
    @Test
    public void testCreateUserFailed() {

        UserDatabaseConnector.createNewUser(testUser);
        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);

        assertFalse(userCreated, "User creation was successful but should not.");
    }

    /**
     * Test that updating is only possible if there is an entry in the database
     * */
    @Test
    public void testUpdateUserFailed() {

        skipCleanUp = true;

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);

        assertFalse(userUpdated, "User update was successful but should not.");
    }

    /**
     * Test that deleting is only possible if there is an entry in the database
     * */
    @Test
    public void testDeleteUserFailed() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean userDeleted = UserDatabaseConnector.deleteUserByID("invalidID");

        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    /**
     * Test reading a user from the database by ID
     * */
    @Test
    public void testReadUserByID() {

        UserDatabaseConnector.createNewUser(testUser);
        User userFromDatabase = UserDatabaseConnector.readUserByID("testUserID").get();

        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals("1234567890", userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.getRole().equals(Role.ADMIN));
    }

    /**
     * Test reading a user from the database by email address
     * */
    @Test
    public void testReadUserByEMail() {

        UserDatabaseConnector.createNewUser(testUser);
        User userFromDatabase = UserDatabaseConnector.readUserByEMail("max.mustermann@mail.com").get();

        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals("1234567890", userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.getRole().equals(Role.ADMIN));
    }

}