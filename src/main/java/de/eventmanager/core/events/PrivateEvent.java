package de.eventmanager.core.events;

import de.eventmanager.core.events.Management.EventManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;

import java.util.ArrayList;
import java.util.Optional;

public class PrivateEvent extends EventModel{

    // Konstruktor für private Events
    public PrivateEvent(String eventName, String eventStart, String eventEnd, String category) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = true;
    }

    // Konstruktor für private Events aus DB
    public PrivateEvent(String eventID, String eventName, String eventStart, String eventEnd, int numberOfBookedUsersOnEvent, String category, boolean privateEvent) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
        this.category = category;
        this.privateEvent = privateEvent;
    }

    // #region CRUD-Operationen
    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category) {

        return EventManager.createNewEvent(new PrivateEvent(eventName, eventStart, eventEnd, category));
    }

    @Override
    public Optional<? extends EventModel> getEventByID(String eventID) {

        return EventManager.readPrivateEventByID(eventID);
    }

    @Override
    public void editEvent(String eventID, String eventName, String eventStart, String eventEnd, ArrayList<String> bookedUsersOnEvent, String category) {

        Optional<PrivateEvent> privateEvent = EventManager.readPrivateEventByID(eventID);

        if (privateEvent.isPresent()) {
            PrivateEvent privateEventToEdit = privateEvent.get();

            privateEventToEdit.setEventName(eventName);
            privateEventToEdit.setEventStart(eventStart);
            privateEventToEdit.setEventEnd(eventEnd);
            privateEventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
            privateEventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
            privateEventToEdit.setCategory(category);

            EventManager.updateEvent(privateEventToEdit);

            LoggerHelper.logInfoMessage(PublicEvent.class, "Event after editing: " + privateEventToEdit);
        }
    }

    @Override
    public boolean deleteEvent(String eventID) {

        return EventManager.deleteEventByID(eventID);
    }
    // #endregion CRUD-Operationen

    // #region toString
    @Override
    public String toString() {
        return "\nevent name: " + eventName + "\nevent date: " + eventStart + " to " + eventEnd + "\nnumber of booked users: " + numberOfBookedUsersOnEvent +
                "\ncategory: " + category + "\nprivate event: " + privateEvent;
    }
    // #endregion toString
}
