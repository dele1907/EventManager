package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventNotificator implements Subject {

    private static final EventNotificator INSTANCE = new EventNotificator();
    private static final List<Observer> OBSERVERS = new CopyOnWriteArrayList<>();

    public static EventNotificator getInstance() {

        return INSTANCE;
    }

    @Override
    public void addObserver(Observer observer) {
        if (OBSERVERS.contains(observer)) {
            return;
        }

        OBSERVERS.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        OBSERVERS.remove(observer);
    }

    @Override
    public void notifyObservers(EventModel event) {
        for (Observer observer : OBSERVERS) {
            observer.update(event);
        }
    }

    @Override
    public void notifyObserversOnEventDeleted(EventModel event) {
        for (Observer observer : OBSERVERS) {
            observer.updateOnEventDeleted(event);
        }
    }

}
