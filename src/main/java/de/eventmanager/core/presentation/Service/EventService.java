package de.eventmanager.core.presentation.Service;

import java.util.List;

public interface EventService {
    List<String> getPublicEventsByName(String name);
    List<String> getPublicEventsByLocation(String location);
    List<String> getPublicEventsByCity(String city);
    String getEventInformationByID(String eventID);
    boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                           String category, String postalCode, String address,
                           int maxCapacity, String eventLocation, String description, int minimumAge, boolean isPrivateEvent,
                           String loggedUserID);
    List<String> getCreatedEventsByUserID(String userID );
    void editEvent(String eventID, String eventName, String eventStart, String eventEnd, String category,
                  String postalCode, String address, String eventLocation, String description,
                  String loggedUserID);
    boolean userHasEventsMinimumage(String eventId, String userID);
    boolean userBookEvent(String eventID, String userID);
    boolean userCancelParticipationForEvent(String eventID, String userID);
    boolean addUserToEventByUserEmail(String eventID, String userEmail, String loggedInUserID);
    List<String> getUsersBookedEventsInformation(String userID);
    boolean getUserHasAlreadyBookedEventByID(String userID, String eventID);
    boolean getUserHasAlreadyBookedEventByEMail(String userEMail, String eventID);
    boolean exportEventToCalendarFile(String eventID, boolean isTestExport);
    boolean getEventIsExistingByID(String eventID);
    List<String> getPublicEventsUserCanBookByID(String loggedUserID);
    void deleteAllExpiredEvents();
}
