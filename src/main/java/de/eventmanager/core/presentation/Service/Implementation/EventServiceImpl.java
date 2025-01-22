package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.presentation.Service.EventService;

import java.util.List;

public class EventServiceImpl implements EventService {
    EventManager eventManager = new EventManager();

    @Override
    public List<PublicEvent> getPublicEventsByName(String name) {
        return eventManager.readPublicEventsByName(name);
    }

    @Override
    public List<PublicEvent> getPublicEventsByLocation(String location) {
        return eventManager.readPublicEventsByLocation(location);
    }

    @Override
    public List<PublicEvent> getPublicEventsByCity(String city) {
        return eventManager.readPublicEventByCity(city);
    }
}
