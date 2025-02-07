package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.events.PublicEvent;
import jdk.jfr.Event;

import java.util.List;

public interface EventService {
    List<String> getPublicEventsByName(String name);
    List<String> getPublicEventsByLocation(String location);
    List<String> getPublicEventsByCity(String city);
    String getEventInformationByID(String eventID);
    boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                           String category, String postalCode, String city, String address,
                           int maxCapacity, String eventLocation, String description, int minimumAge, boolean isPrivateEvent,
                           String loggedUserID);
    List<String> getCreatedEventsByUserID(String userID );
    void editEvent(String eventID, String eventName, String eventStart, String eventEnd, String category,
                  String postalCode, String city, String address, String eventLocation, String description,
                  String loggedUserID);
    boolean userBookEvent(String eventID, String userID);
    List<String> getUsersBookedEventsInformation(String userID);
    boolean getUserHasAlreadyBookedEvent(String userID, String eventID);
}
