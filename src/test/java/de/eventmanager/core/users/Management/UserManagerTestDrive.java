package de.eventmanager.core.users.Management;

import de.eventmanager.core.users.User;

import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTestDrive {

    private User testUser;
    private User testUserUpdated;

    // Vor jedem Test eine Instanz von UserManager und zwei Test-User erstellen:
    @BeforeEach
    public void setUp() {

        testUser = new User("testUserID", "Max", "Mustermann", "1980-01-10",
                "max.mustermann@mail.com", "password123", 1234567890, false);
        testUserUpdated = new User("testUserID", "Maximilian", "Mustermann-Meyer", "1980-10-01",
                "max.m@mail.com", "password987", 1357902468, false);
    }

    // Testen von Erstellen, Updaten und Löschen eines Users in der Datenbank:
    @Test
    public void testCreateUpdateDeleteUser() {

        boolean userCreated = UserManager.createNewUser(testUser);
        assertTrue(userCreated, "User creation failed but should not.");

        boolean userUpdated = UserManager.updateUser(testUserUpdated);
        assertTrue(userUpdated, "User update failed but should not.");

        boolean userDeleted = UserManager.deleteUserByID(testUser.getUserID());
        assertTrue(userDeleted, "User deletion failed but should not.");
    }

    // Testen, ob ein User nicht mehrmals erstellt werden kann:
    @Test
    public void testCreateUserFailed() {

        UserManager.createNewUser(testUser);
        boolean userCreated = UserManager.createNewUser(testUser);
        assertFalse(userCreated, "User creation was successful but should not.");
    }

    // Testen, ob ein Update nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    public void testUpdateUserFailed() {

        boolean userUpdated = UserManager.updateUser(testUserUpdated);
        assertFalse(userUpdated, "User update was successful but should not.");
    }

    // Testen, ob Löschen nur möglich ist, wenn der entsprechende Datensatz vorliegt:
    @Test
    public void testDeleteUserFailed() {

        boolean userDeleted = UserManager.deleteUserByID("invalidID");
        assertFalse(userDeleted, "User deletion was successful but should not.");
    }

    // Testen, ob ein User anhand der ID korrekt ausgelesen werden kann:
    @Test
    public void testReadUserByID() {

        UserManager.createNewUser(testUser);
        User userFromDatabase = UserManager.readUserByID("testUserID").get();
        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.isAdmin());

    }

    // Testen, ob ein User anhand der E-Mail korrekt ausgelesen werden kann:
    @Test
    public void testReadUserByEMail() {

        UserManager.createNewUser(testUser);
        User userFromDatabase = UserManager.readUserByEMail("max.mustermann@mail.com").get();

        assertEquals("testUserID", userFromDatabase.getUserID());
        assertEquals("Max", userFromDatabase.getFirstName());
        assertEquals("Mustermann", userFromDatabase.getLastName());
        assertEquals("1980-01-10", userFromDatabase.getDateOfBirth());
        assertEquals("max.mustermann@mail.com", userFromDatabase.getEMailAddress());
        assertEquals("password123", userFromDatabase.getPassword());
        assertEquals(1234567890, userFromDatabase.getPhoneNumber());
        assertEquals(false, userFromDatabase.isAdmin());


    }

    // Nach jedem Test die Datenbank bereinigen:
    @AfterEach
    public void cleanUp() {

        UserManager.deleteUserByID("testUserID");
    }

    //#region Registration and Authentication Tests

    @Test
    @Order(3)
    @DisplayName("Password-Registration Test")
    void isValidAndIsNotValidRegistrationPasswordTest() {

        String validTestPassword = "eventManager123";
        String inValidTestPassword = "eventManagerÄ";

        assertFalse(UserManager.isValidRegistrationPassword(inValidTestPassword, "eventManagerÄ"));

        assertTrue(UserManager.isValidRegistrationPassword(validTestPassword, "eventManager123"));

    }

    @Test
    @Order(4)
    @DisplayName("Login-System Test")
    void authenticateUserLoginTest() {

        assertTrue(UserManager.authenticationUserLogin("fiot00001@htwsaar.de", "eventManager123"));

    }

    //#endregion Registration and Authentication Tests

}