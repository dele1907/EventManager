package de.eventmanager.core.events;

import de.eventmanager.core.events.Management.EventManager;
import helper.IDGenerationHelper;

import java.util.ArrayList;

public class PrivateEvent extends EventModel{

    // Konstruktor für private Events
    public PrivateEvent(String eventName, String eventDateTime, String category) {
        this.eventID = IDGenerationHelper.generateRandomIDString();
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.category = category;
        this.privateEvent = true;
    }

    // Konstruktor für private Events aus DB
    public PrivateEvent(String eventID, String eventName, String eventDateTime, int numberOfBookedUsersOnEvent, String category, boolean privateEvent) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
        this.category = category;
        this.privateEvent = privateEvent;
    }

    // #region CRUD-Operationen
    @Override
    public boolean createNewEvent(String eventName, String eventDateTime, String category) {

        return EventManager.createEvent(new PrivateEvent(eventName, eventDateTime, category));
    }

    @Override
    public EventModel getEventByID(String eventID) {

        return EventManager.readEventByID(eventID);
    }

    @Override
    public void editEvent(String eventID, String eventName, String eventDateTime, ArrayList<String> bookedUsersOnEvent, String category) {

        PrivateEvent eventToEdit = (PrivateEvent) EventManager.readEventByID(eventID);

        eventToEdit.setEventName(eventName);
        eventToEdit.setEventDateTime(eventDateTime);
        eventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
        eventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
        eventToEdit.setCategory(category);

        EventManager.updateEvent(eventToEdit);
    }

    @Override
    public boolean deleteEvent(String eventID) {

        return EventManager.deleteEventByID(eventID);
    }
    // #endregion CRUD-Operationen

}
