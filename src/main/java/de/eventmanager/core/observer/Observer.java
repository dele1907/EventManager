package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

public interface Observer {

    void update(EventModel event);
    void updateOnEventDeleted(EventModel event);

}
