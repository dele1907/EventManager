package de.eventmanager.core.export;

import de.eventmanager.core.database.Communication.BookingDatabaseConnector;
import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.export.Management.ExportManager;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.*;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExportManagerTestDrive {
    final ExportManager exportManager = new ExportManager();
    static ArrayList<String> testArrayList = new ArrayList<>();
    static final String TEST_PUBLIC_EVENT_ID = "ExportPublicEventID";
    static final String TEST_PRIVATE_EVENT_ID = "ExportPrivateEventID";
    static final String TEST_ADMIN_ID = "AdminID_ExportTest";
    static final String TEST_USER_ID = "UserID_ExportTest";
    static final String TEST_ADMIN_EMAIL_ADDRESS = "firstName.lastName@adminExportTestmail.com";
    static final String TEST_USER_EMAIL_ADDRESS = "firstName.lastName@userExportTestmail.com";
    static PublicEvent publicEvent;
    static PrivateEvent privateEvent;
    static User testAdminUser;
    static User testUser;
    static List<EventModel> eventList = new ArrayList<>();

    @BeforeAll
    static void globalSetUp() {
        testAdminUser = new User(TEST_ADMIN_ID,"Finn","Oettinger","2003-13-04",
                TEST_ADMIN_EMAIL_ADDRESS,"AdminPassword", "+497733686", true);
        testAdminUser.setRoleAdmin(true);
        testUser = new User(TEST_USER_ID, "firstNameExport", "User", "2000-04-20",
                TEST_USER_EMAIL_ADDRESS, "password", "+497788866", false);

        publicEvent = new PublicEvent(TEST_PUBLIC_EVENT_ID,"Valentinstag-Party", "2025-02-20 12:30",
                "2025-02-20 14:30", 1, testArrayList, "TestCategory",
                false,"66123","Saarbrücken", "Dudweiler Landstraße 7", "Kulturfabrik",
                "This is a cool publicEvent", 20, 0);
        privateEvent = new PrivateEvent(TEST_PRIVATE_EVENT_ID, "Geburtstag-Party", "2025-02-25 12:30",
                "2025-02-25 14:30", 1, testArrayList, "TestCategory",
                true,"66123","Saarbrücken", "Dudweiler Landstraße 7", "Kulturfabrik",
                "This is a cool publicEvent");

        UserDatabaseConnector.createNewUser(testAdminUser);
        UserDatabaseConnector.createNewUser(testUser);
        EventDatabaseConnector.createNewEvent(publicEvent, TEST_ADMIN_ID);
        EventDatabaseConnector.createNewEvent(privateEvent, TEST_ADMIN_ID);
        BookingDatabaseConnector.addBooking(TEST_PUBLIC_EVENT_ID, TEST_USER_ID);
        BookingDatabaseConnector.addBooking(TEST_PRIVATE_EVENT_ID, TEST_USER_ID);
        eventList.add(publicEvent);
        eventList.add(privateEvent);
    }

    @AfterAll
    static void globalCleanUp() {
        BookingDatabaseConnector.removeBooking(TEST_PUBLIC_EVENT_ID, TEST_USER_ID);
        BookingDatabaseConnector.removeBooking(TEST_PRIVATE_EVENT_ID, TEST_USER_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PRIVATE_EVENT_ID, TEST_ADMIN_ID);
        EventDatabaseConnector.deleteEventByID(TEST_PUBLIC_EVENT_ID, TEST_ADMIN_ID);
        UserDatabaseConnector.deleteUserByID(TEST_ADMIN_ID);
        UserDatabaseConnector.deleteUserByID(TEST_USER_ID);
        eventList.clear();
    }

    @Test
    @DisplayName("Export Events Test")
    void exportEventsTest() {
        assertTrue(exportManager.exportEvents(eventList, true));
    }

}
