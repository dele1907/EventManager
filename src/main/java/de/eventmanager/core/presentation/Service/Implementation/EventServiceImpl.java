package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.users.Management.UserManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {
    UserManagerImpl userManager = new UserManagerImpl();

    @Override
    public List<String> getPublicEventsByName(String name) {
        List<String> publicEventsByName = new ArrayList<>();

        EventDatabaseConnector.readPublicEventsByName(name).forEach(event -> {
            if (event instanceof PublicEvent) {
                publicEventsByName.add(event.toString());
            }
        });

        return publicEventsByName;
    }

    @Override
    public List<String> getPublicEventsByLocation(String location) {
        List<String> publicEventsByLocation = new ArrayList<>();

        EventDatabaseConnector.readPublicEventsByLocation(location).forEach(event -> {
            if (event instanceof PublicEvent) {
                publicEventsByLocation.add(event.toString());
            }
        });

        return publicEventsByLocation;
    }

    @Override
    public List<String> getPublicEventsByCity(String city) {
        List<String> publicEventsByCity = new ArrayList<>();

        EventDatabaseConnector.readPublicEventByCity(city).forEach(event -> {
            if (event instanceof PublicEvent) {
                publicEventsByCity.add(event.toString());
            }
        });
        return publicEventsByCity;
    }

    @Override
    public String getEventInformationByID(String eventID) {
        return userManager.getEventInformationByEventID(eventID);
    }

    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String city, String address,
                                  int maxCapacity, String eventLocation, String description, int minimumAge,
                                  boolean isPrivateEvent, String loggedUserID) {

        return userManager.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation,
                description, maxCapacity, isPrivateEvent, loggedUserID);
    }

    @Override
    public List<String> getCreatedEventsByUserID(String userID ) {
        List<String > createdEvents = new ArrayList<>();

        EventDatabaseConnector.getEventsByCreatorID(userID).forEach(event -> {
            createdEvents.add(event.toString());
        });

        return createdEvents;
    }

    public void editEvent(String eventID, String newEventName, String newEventStart, String newEventEnd,
                          String newCategory, String newPostalCode, String newCity, String newAddress,
                          String newEventLocation, String newDescription, String loggedUserID) {

        userManager.getEventByID(eventID).ifPresent(event -> {
            String eventName = Optional.ofNullable(newEventName).orElse(event.getEventName());
            String eventStart = Optional.ofNullable(newEventStart).orElse(event.getEventStart());
            String eventEnd = Optional.ofNullable(newEventEnd).orElse(event.getEventEnd());
            String category = Optional.ofNullable(newCategory).orElse(event.getCategory());
            String postalCode = Optional.ofNullable(newPostalCode).orElse(event.getPostalCode());
            String city = Optional.ofNullable(newCity).orElse(event.getCity());
            String address = Optional.ofNullable(newAddress).orElse(event.getAddress());
            String eventLocation = Optional.ofNullable(newEventLocation).orElse(event.getEventLocation());
            String description = Optional.ofNullable(newDescription).orElse(event.getDescription());

            userManager.editEvent(event.getEventID(), eventName, eventStart, eventEnd, category, postalCode, city,
                    address, eventLocation, description, loggedUserID);
        });
    }
}
