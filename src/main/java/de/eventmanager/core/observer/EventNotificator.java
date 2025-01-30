package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventNotificator implements Subject {

    private static final EventNotificator INSTANCE = new EventNotificator();
    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    public static EventNotificator getInstance() {

        return INSTANCE;
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EventModel event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

}
