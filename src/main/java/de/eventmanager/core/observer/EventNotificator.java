package de.eventmanager.core.observer;

import de.eventmanager.core.events.EventModel;

import java.util.ArrayList;

public class EventNotificator implements Subject {

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(EventModel event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

}
