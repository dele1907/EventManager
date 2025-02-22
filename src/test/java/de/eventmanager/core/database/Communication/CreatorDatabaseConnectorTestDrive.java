package de.eventmanager.core.database.Communication;

import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CreatorDatabaseConnectorTestDrive {

    private static final User TEST_USER = new User("testUserForCreatorDatabaseConnector", "Max", "Mustermann", "1980-01-10",
            "max.eventcreator@testmail.com", "Password123", "1234567890", false);
    private static final PublicEvent TEST_EVENT = new PublicEvent("testEventForCreatorDatabaseConnector", "Ostermarkt", "2025-04-04 12:00", "2025-04-06 12:00", 0, null,
            "Markt", false, "66119", "Saarbrücken", "St. Johanner Markt", "Marktplatz", "Ostermarkt für tolle Menschen", 2000, 0);

    @BeforeAll
    static void globalSetUp() {
        UserDatabaseConnector.createNewUser(TEST_USER);
        EventDatabaseConnector.createNewEvent(TEST_EVENT, TEST_USER.getUserID());
    }

    @AfterAll
    static void globalCleanUp() {
        EventDatabaseConnector.deleteEventByID(TEST_EVENT.getEventID(), TEST_USER.getUserID());
        UserDatabaseConnector.deleteUserByID(TEST_USER.getUserID());
    }

    //#region successful CRUD operations

    /**
     * Test checking that a user is the event creator
     * */
    @Test
    public void testCheckIfUserIsCreator() {

        boolean creatorChecked = CreatorDatabaseConnector.checkIfUserIsEventCreator(TEST_EVENT.getEventID(), TEST_USER.getUserID());
        assertTrue(creatorChecked, "User was not found as creator but should.");
    }

    /**
     * Test getting the event creator
     * */
    @Test
    public void testGetEventCreator() {

        Optional<User> creatorFromDatabase = CreatorDatabaseConnector.getEventCreator(TEST_EVENT.getEventID());
        assertTrue(creatorFromDatabase.isPresent(), "User is not present but should.");

        assertEquals(TEST_USER.getUserID(), creatorFromDatabase.get().getUserID());
        assertEquals(TEST_USER.getFirstName(), creatorFromDatabase.get().getFirstName());
        assertEquals(TEST_USER.getLastName(), creatorFromDatabase.get().getLastName());
        assertEquals(TEST_USER.getDateOfBirth(), creatorFromDatabase.get().getDateOfBirth());
        assertEquals(TEST_USER.getEMailAddress(), creatorFromDatabase.get().getEMailAddress());
        assertEquals(TEST_USER.getPassword(), creatorFromDatabase.get().getPassword());
        assertEquals(TEST_USER.getPhoneNumber(), creatorFromDatabase.get().getPhoneNumber());
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test checking that creator and event aren't available
     * */
    @Test
    public void testCheckIfUserIsCreatorFailed() {

        boolean creatorChecked = CreatorDatabaseConnector.checkIfUserIsEventCreator("invalidEventIDToCheck", "invalidUserIDToCheck");
        assertFalse(creatorChecked, "User was found as creator but should not.");
    }

    /**
     * Test getting a creator who doesn't exist
     * */
    @Test
    public void testGetEventCreatorFailed() {

        Optional<User> creatorFromDatabase = CreatorDatabaseConnector.getEventCreator("invalidUserIDToGet");
        assertFalse(creatorFromDatabase.isPresent(), "User is present but should not.");
    }

    //#endregion failed CRUD operations

}
