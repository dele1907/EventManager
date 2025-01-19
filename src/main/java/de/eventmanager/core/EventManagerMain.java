package de.eventmanager.core;

import de.eventmanager.core.presentation.EventManagerTextBasedUIInstance;

public class EventManagerMain {
    private static EventManagerTextBasedUIInstance eventManagerTextBasedUIInstance = new EventManagerTextBasedUIInstance();

    public static void main(String[] args) {
        eventManagerTextBasedUIInstance.startEventManagerInstance();
    }
}