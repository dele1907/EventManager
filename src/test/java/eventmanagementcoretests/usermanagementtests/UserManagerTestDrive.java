package eventmanagementcoretests.usermanagementtests;

import eventmanagementcore.usermanagement.UserManager;
import eventmanagementcore.usermanagement.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        User userFromDatabase = UserManager.readUserByID("testUserID");
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
        User userFromDatabase = UserManager.readUserByEMail("max.mustermann@mail.com");
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

}