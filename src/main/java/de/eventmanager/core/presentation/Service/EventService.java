package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.events.PublicEvent;
import jdk.jfr.Event;

import java.util.List;

public interface EventService {
    List<PublicEvent> getPublicEventsByName(String name);
    List<PublicEvent> getPublicEventsByLocation(String location);
    List<PublicEvent> getPublicEventsByCity(String city);
    boolean createNewEvent(String eventName, String eventStart, String eventEnd,
                           String category, String postalCode, String address,
                           int maxCapacity, String eventLocation, String description, boolean isPublicEvent
    );

}
