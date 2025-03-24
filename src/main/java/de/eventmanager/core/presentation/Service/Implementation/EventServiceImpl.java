package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.observer.EventNotificator;
import de.eventmanager.core.observer.UserObserver;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.users.Management.UserManagerImpl;
import helper.DateOperationsHelper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventServiceImpl implements EventService {
    UserManagerImpl userManager = new UserManagerImpl();
    EventNotificator eventNotificator = new EventNotificator();

    //#region readEvents
   public List<String> getPublicEventsUserCanBookByID(String loggedUserID) {
       return EventDatabaseConnector.getAllPublicEventsUserHasNotBookedAlready(loggedUserID)
               .stream()
               .map(Object::toString)
               .collect(Collectors.toList());
   }

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
    public boolean userHasEventsMinimumage(String eventId, String userID) {
        var event = userManager.getEventByID(eventId).get();
        int minimumAge = 0;

        if (event instanceof PublicEvent) {
            minimumAge = ((PublicEvent) event).getMinimumAge();
        }

        var loggedUsersAge = DateOperationsHelper.getTheAgeFromDatabase(userManager.getUserByID(userID).get()
                .getEMailAddress());

        if ( (event instanceof PublicEvent) && loggedUsersAge < minimumAge) {
            return false;
        }

        return true;
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
        var eventOptional = EventDatabaseConnector.readEventByID(eventID);
        var invitedUserOptional = userManager.getUserByEmail(userEmail);

        if (!invitedUserOptional.isPresent() || !eventOptional.isPresent()) {
            return false;
        }

        if (invitedUserOptional.isPresent() && eventOptional.isPresent()) {
            eventNotificator.addObserver(new UserObserver(invitedUserOptional.get(), eventOptional.get()));
            eventNotificator.notifyObserversOnEventInvitation(eventOptional.get());
        }

        return  userManager.addUserToEvent(eventID, userEmail, loggedInUserID);
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
    public boolean exportEventToCalendarFile(String eventID, boolean isTestExport) {
        if (!getEventIsExistingByID(eventID) || eventID.isEmpty()) {
            return false;
        }

        return userManager.exportEventByEventID(eventID, isTestExport);
    }

    @Override
    public boolean getEventIsExistingByID(String eventID) {
        return userManager.getEventByID(eventID).isPresent();
    }

    @Override
    public void deleteAllExpiredEvents() {
        var eventsToDelete = DateOperationsHelper.getAllExpiredEvents();

        for (var eventID : eventsToDelete) {
            deleteEvent(eventID);
        }
    }

    private void deleteEvent(String eventId) {
       var eventCreator = CreatorDatabaseConnector.getEventCreator(eventId);
       var event = userManager.getEventByID(eventId);

       if (eventCreator.isPresent() && event.isPresent()) {
           EventDatabaseConnector.deleteEventByID(eventId, eventCreator.get().getUserID());

            eventNotificator.addObserver(new UserObserver(eventCreator.get(), event.get()));
            eventNotificator.notifyObserversOnEventDeleted(event.get());
       }
    }
    //#endregion event operations
}
