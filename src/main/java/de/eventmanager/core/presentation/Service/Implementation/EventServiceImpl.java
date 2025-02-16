package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.users.Management.UserManagerImpl;
import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
    UserManagerImpl userManager = new UserManagerImpl();

    //#region readEvents
    @Override
    public List<String> getPublicEventsByName(String name) {
        return userManager.readPublicEventsByName(name);
    }

    @Override
    public List<String> getPublicEventsByLocation(String location) {
        return userManager.readPublicEventsByLocation(location);
    }

    @Override
    public List<String> getPublicEventsByCity(String city) {
        return userManager.readPublicEventByCity(city);
    }

    @Override
    public String getEventInformationByID(String eventID) {
        return userManager.getEventInformationByEventID(eventID);
    }
    //#endregion readEvents

    //#region event operations
    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String address,
                                  int maxCapacity, String eventLocation, String description, int minimumAge,
                                  boolean isPrivateEvent, String loggedUserID) {

        return userManager.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation,
                description, maxCapacity, minimumAge, isPrivateEvent, loggedUserID);
    }

    @Override
    public List<String> getCreatedEventsByUserID(String userID ) {
        return userManager.readCreatedEventsByUserID(userID);
    }

    @Override
    public void editEvent(String eventID, String newEventName, String newEventStart, String newEventEnd,
                          String newCategory, String newPostalCode, String newAddress,
                          String newEventLocation, String newDescription, String loggedUserID) {

        userManager.getEventByID(eventID).ifPresent(event -> {
            String eventName = Optional.ofNullable(newEventName).orElse(event.getEventName());
            String eventStart = Optional.ofNullable(newEventStart).orElse(event.getEventStart());
            String eventEnd = Optional.ofNullable(newEventEnd).orElse(event.getEventEnd());
            String category = Optional.ofNullable(newCategory).orElse(event.getCategory());
            String postalCode = Optional.ofNullable(newPostalCode).orElse(event.getPostalCode());
            String address = Optional.ofNullable(newAddress).orElse(event.getAddress());
            String eventLocation = Optional.ofNullable(newEventLocation).orElse(event.getEventLocation());
            String description = Optional.ofNullable(newDescription).orElse(event.getDescription());

            userManager.editEvent(event.getEventID(), eventName, eventStart, eventEnd, category, postalCode,
                    address, eventLocation, description, loggedUserID);
        });
    }

    @Override
    public boolean userBookEvent(String eventID, String userID) {
        return userManager.bookEvent(eventID, userID);
    }

    @Override
    public boolean userCancelParticipationForEvent(String eventID, String userID) {
        return userManager.cancelEvent(eventID, userID);
    }

    @Override
    public boolean addUserToEventByUserEmail(String eventID, String userEmail, String loggedInUserID) {
        if (!userManager.getUserByEmail(userEmail).isPresent()) {
            return false;
        }

        userManager.addUserToEvent(eventID, userEmail, loggedInUserID);

        return true;
    }

    @Override
    public List<String> getUsersBookedEventsInformation(String userID) {
        return userManager.getUsersBookedEventsInformation(userID);
    }

    @Override
    public boolean getUserHasAlreadyBookedEventByID(String userID, String eventID) {
        return userManager.getUserHasAlreadyBookedEvent(userID, eventID);
    }

    @Override
    public boolean getUserHasAlreadyBookedEventByEMail(String userEMail, String eventID) {
        return userManager.getUserHasAlreadyBookedEvent(userManager.getUserByEmail(userEMail).get().getUserID(), eventID);
    }

    @Override
    public boolean exportEventToCalendarFile(String eventID) {
        return userManager.exportEventByEventID(eventID);
    }

    public boolean getEventIsExistingByID(String eventID) {
        return userManager.getEventByID(eventID).isPresent();
    }
    //#endregion event operations
}
