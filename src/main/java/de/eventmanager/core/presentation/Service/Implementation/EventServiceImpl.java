package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Service.EventService;
import de.eventmanager.core.users.Management.UserManagerImpl;

import java.util.ArrayList;
import java.util.List;

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
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String city, String address,
                                  int maxCapacity, String eventLocation, String description, boolean isPublicEvent,
                                  String loggedUserID) {

        return userManager.createNewEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation,
                description, maxCapacity, isPublicEvent, loggedUserID);
    }

    @Override
    public List<String> getCreatedEventsByUserID(String userID ) {
        List<String > createdEvents = new ArrayList<>();

        EventDatabaseConnector.getEventsByCreatorID(userID).forEach(event -> {
            createdEvents.add(event.toString());
        });

        return createdEvents;
    }
}
