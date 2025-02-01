package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

public interface Subject {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(EventModel event);

}
