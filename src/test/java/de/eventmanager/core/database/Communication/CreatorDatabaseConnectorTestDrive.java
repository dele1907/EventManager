package de.eventmanager.core.database.Communication;

import de.eventmanager.core.users.User;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.jooq.generated.tables.Created.CREATED;
import static org.jooq.generated.tables.User.USER;
import static org.junit.jupiter.api.Assertions.*;

public class CreatorDatabaseConnectorTestDrive {

    User testUser;

    /**
     * Clean up the database after testing
     * */
    /*@AfterAll
    static void cleanUp() throws SQLException {

        try (Connection cleanupConnection = DatabaseConnector.connect()) {
           DSLContext cleanupDsl = DSL.using(cleanupConnection, SQLDialect.SQLITE);
            cleanupDsl.deleteFrom(USER).execute();
            cleanupDsl.deleteFrom(CREATED).execute();
        }
    }*/

    //#region successful CRUD operations

    /**
     * Test assigning a user as event creator
     * */
    @Test
    public void testAssignUserAsEventCreatorAndCheck() {

        boolean creatorAssigned = CreatorDatabaseConnector.assignUserAsEventCreator("testEventToBeAssigned", "testUserToAssignAsCreator");
        assertTrue(creatorAssigned, "Adding user to created event failed but should not.");

        boolean userIsEventCreator = CreatorDatabaseConnector.checkIfUserIsEventCreator("testEventToBeAssigned", "testUserToAssignAsCreator");
        assertTrue(userIsEventCreator, "User ist not creator but should be.");

        CreatorDatabaseConnector.removeUserAsEventCreator("testEventToBeAssigned", "testUserToAssignAsCreator");
    }

    /**
     * Test getting the event creator
     * */
    @Test
    public void testGetEventCreator() {

        testUser = new User("assignTestCreatorDatabaseConnector", "Max", "Mustermann", "1980-01-10",
                "max.eventcreator@testmail.com", "Password123", "1234567890", false);

        UserDatabaseConnector.createNewUser(testUser);
        CreatorDatabaseConnector.assignUserAsEventCreator("assignTestEventDatabaseConnector", "assignTestCreatorDatabaseConnector");

        Optional<User> creatorFromDatabase = CreatorDatabaseConnector.getEventCreator("assignTestEventDatabaseConnector");
        assertTrue(creatorFromDatabase.isPresent());

        assertEquals("assignTestCreatorDatabaseConnector", creatorFromDatabase.get().getUserID());
        assertEquals("Max", creatorFromDatabase.get().getFirstName());
        assertEquals("Mustermann", creatorFromDatabase.get().getLastName());
        assertEquals("1980-01-10", creatorFromDatabase.get().getDateOfBirth());
        assertEquals("max.eventcreator@testmail.com", creatorFromDatabase.get().getEMailAddress());
        assertEquals("Password123", creatorFromDatabase.get().getPassword());
        assertEquals("1234567890", creatorFromDatabase.get().getPhoneNumber());

        CreatorDatabaseConnector.removeUserAsEventCreator("assignTestEventDatabaseConnector", "assignTestCreatorDatabaseConnector");
        UserDatabaseConnector.deleteUserByID("assignTestCreatorDatabaseConnector");
    }

    /**
     * Test deleting the event creator
     * */
    @Test
    public void testRemoveUserAsEventCreator() {

        CreatorDatabaseConnector.assignUserAsEventCreator("testEventToBeRemoved", "testUserToRemoveAsCreator");

        boolean creatorToRemove = CreatorDatabaseConnector.removeUserAsEventCreator("testEventToBeRemoved", "testUserToRemoveAsCreator");
        assertTrue(creatorToRemove, "Adding user to created event failed but should not.");
    }

    //#endregion successful CRUD operations

    //#region failed CRUD operations

    /**
     * Test that creators are unique
     * */
    @Test
    public void testAssignUserAsCreatorFailed() {

        CreatorDatabaseConnector.assignUserAsEventCreator("testEventToFailAssignment", "testUserToFailAssignment");

        boolean creatorAssigned = CreatorDatabaseConnector.assignUserAsEventCreator("testEventToFailAssignment", "testUserToFailAssignment");
        assertFalse(creatorAssigned, "Second creator assignment was successful but should not.");

        CreatorDatabaseConnector.removeUserAsEventCreator("testEventToFailAssignment", "testUserToFailAssignment");
    }

    /**
     * Test checking that creator and event aren't available
     * */
    @Test
    public void testCheckIfUserIsCreatorFailed() {

        boolean creatorChecked = CreatorDatabaseConnector.checkIfUserIsEventCreator("invalidEventIDToCheck", "invalidUserIDToCheck");
        assertFalse(creatorChecked, "User was found as creator but should not.");
    }

    /**
     * Test getting a user who doesn't exist
     * */
    @Test
    public void testGetEventCreatorFailed() {

        Optional<User> creatorFromDatabase = CreatorDatabaseConnector.getEventCreator("invalidUserIDToGet");
        assertFalse(creatorFromDatabase.isPresent(), "User is present but should not.");
    }

    /**
     * Test removing a creator who doesn't exist
     * */
    @Test
    public void testRemoveUserAsEventCreatorFailed() {

        boolean creatorToRemove = CreatorDatabaseConnector.removeUserAsEventCreator("invalidEventIDToRemove", "invalidUserIDToRemove");
        assertFalse(creatorToRemove, "Creator was successfully removed but should not.");
    }

    //#endregion failed CRUD operations

}
