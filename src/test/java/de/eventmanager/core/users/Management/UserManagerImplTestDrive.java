package de.eventmanager.core.users.Management;

import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserManagerImplTestDrive {

    private User testUser;
    private User testUserUpdated;
    private boolean skipSetUp = false;
    private boolean skipCleanUp = false;

    // Vor jedem Test zwei Test-User erstellen:
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

    // Nach jedem Test die Datenbank bereinigen:
    @AfterEach
    public void cleanUp() {

        if(skipCleanUp) {

            return;
        }

        UserDatabaseConnector.deleteUserByID("testUserID");
    }

    // Testen von Erstellen, Updaten und Löschen eines Users in der Datenbank:
    @Test
    @Order(0)
    public void testCreateUpdateDeleteUser() {

        skipCleanUp = true;

        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);
        assertTrue(userUpdated, "User update failed but should not.");

        boolean userDeleted = UserDatabaseConnector.deleteUserByID(testUser.getUserID());
        assertTrue(userDeleted, "User deletion failed but should not.");
    }

    // Testen, ob ein User nicht mehrmals erstellt werden kann:
    @Test
    @Order(1)
    public void testCreateUserFailed() {

        UserDatabaseConnector.createNewUser(testUser);
        boolean userCreated = UserDatabaseConnector.createNewUser(testUser);

        assertFalse(userCreated, "User creation was successful but should not.");
    }

    // Testen, ob ein Update nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    @Order(2)
    public void testUpdateUserFailed() {

        skipCleanUp = true;

        boolean userUpdated = UserDatabaseConnector.updateUser(testUserUpdated);

        assertFalse(userUpdated, "User update was successful but should not.");
    }

    // Testen, ob Löschen nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    @Order(3)
    public void testDeleteUserFailed() {

        skipSetUp = true;
        skipCleanUp = true;

        boolean userDeleted = UserDatabaseConnector.deleteUserByID("invalidID");

        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    // Testen, ob ein User anhand der ID korrekt ausgelesen werden kann:
    @Test
    @Order(4)
    public void testReadUserByID() {

        UserDatabaseConnector.createNewUser(testUser);
        User userFromDatabase = UserDatabaseConnector.readUserByID("testUserID").get();

        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.getRole().equals(Role.ADMIN));
    }

    // Testen, ob ein User anhand der E-Mail korrekt ausgelesen werden kann:
    @Test
    @Order(5)
    public void testReadUserByEMail() {

        UserDatabaseConnector.createNewUser(testUser);
        User userFromDatabase = UserDatabaseConnector.readUserByEMail("max.mustermann@mail.com").get();

        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.getRole().equals(Role.ADMIN));
    }

    //#region Registration and Authentication Tests
    @Test
    @Order(6)
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        skipSetUp = true;
        skipCleanUp = true;

        String validTestPassword = "eventManager123";
        String inValidTestPassword = "eventManagerÄ";

        assertFalse(UserDatabaseConnector.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(UserDatabaseConnector.isValidRegistrationPassword(validTestPassword, "eventManager123"));
    }

    @Test
    @Order(7)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        skipSetUp = true;
        skipCleanUp = true;

        assertTrue(UserDatabaseConnector.authenticationUserLogin("fiot00001@htwsaar.de", "eventManager123"));
    }
    //#endregion Registration and Authentication Tests

}