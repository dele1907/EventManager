package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

public interface Subject {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(EventModel event);

}
