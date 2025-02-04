package de.eventmanager.core.export;

import de.eventmanager.core.database.Communication.BookingDatabaseConnector;
import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.export.Management.ExportManager;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ExportManagerTestDrive {
    final ExportManager exportManager = new ExportManager();
    final ArrayList<String> testArrayList = new ArrayList<>();
    final String TEST_PUBLIC_EVENT_ID = "ExportTestID";
    final String TEST_ADMIN_ID = "AdminID_ExportTest";
    final String TEST_USER_ID = "UserID_ExportTest";
    final String TEST_ADMIN_EMAIL_ADDRESS = "firstName.lastName@adminExportTestmail.com";
    final String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@userExportTestmail.com";
    EventModel event;
    User testAdminUser;
    User testUser;

    @BeforeEach
    void setUp() {
        testAdminUser = new User(TEST_ADMIN_ID,"firstNameExport","Admin","1999-04-05",
                TEST_ADMIN_EMAIL_ADDRESS,"AdminPassword", "+497733686", true);
        testAdminUser.setRoleAdmin(true);
        testUser = new User(TEST_USER_ID, "firstNameExport", "User", "2000-04-20",
                TEST_USER_EMAIL_ADDRESS, "password", "+497788866", false);

        event = new PublicEvent(TEST_PUBLIC_EVENT_ID,"TestPublicEvent", "2025-01-05 12:30",
                "2025-01-05 14:30", 0, testArrayList, "TestCategory",
                false,"66115","Saarbrücken", "TestStraße 6", "Turmschule",
                "This is a cool event", 20, 0);
        UserDatabaseConnector.createNewUser(testAdminUser);
        UserDatabaseConnector.createNewUser(testUser);
        EventDatabaseConnector.createNewEvent(event);
        CreatorDatabaseConnector.assignUserAsEventCreator(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);
        BookingDatabaseConnector.addBooking(TEST_PUBLIC_EVENT_ID, TEST_USER_ID);
    }

    @AfterEach
    void cleanUp() {
        BookingDatabaseConnector.removeBooking(TEST_PUBLIC_EVENT_ID, TEST_USER_ID);
        CreatorDatabaseConnector.removeUserAsEventCreator(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PUBLIC_EVENT_ID);
        UserDatabaseConnector.deleteUserByID(TEST_ADMIN_ID);
        UserDatabaseConnector.deleteUserByID(TEST_USER_ID);
    }

    @Test
    @DisplayName("Create Calender Test")
    void createCalenderTest() {
        assertTrue(exportManager.createCalendar(event));
    }

}
