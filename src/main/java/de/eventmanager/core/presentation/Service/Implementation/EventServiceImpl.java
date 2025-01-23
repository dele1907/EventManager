package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.database.Communication.EventDataBaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Service.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {
    EventDataBaseConnector eventDataBaseConnector = new EventDataBaseConnector();

    @Override
    public List<PublicEvent> getPublicEventsByName(String name) {
        return eventDataBaseConnector.readPublicEventsByName(name);
    }

    @Override
    public List<PublicEvent> getPublicEventsByLocation(String location) {
        return eventDataBaseConnector.readPublicEventsByLocation(location);
    }

    @Override
    public List<PublicEvent> getPublicEventsByCity(String city) {
        return eventDataBaseConnector.readPublicEventByCity(city);
    }

    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                                  String category, String postalCode, String address,
                                  int maxCapacity, String eventLocation, String description, boolean isPublicEvent) {

        EventModel eventToCreate = isPublicEvent ?
                   new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation,
                           description, maxCapacity) :
                   new PrivateEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation,
                           description);

        return eventDataBaseConnector.createNewEvent(eventToCreate);
    }
}
