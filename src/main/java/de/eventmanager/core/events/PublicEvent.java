package de.eventmanager.core.events;

import de.eventmanager.core.events.Management.EventManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;

import java.util.ArrayList;
import java.util.Optional;

public class PublicEvent extends EventModel {

    private int maximumCapacity = -1;

    // Konstruktor für öffentliche Events ohne Teilnehmerbeschränkung
    public PublicEvent(String eventName, String eventDateTime, String category) {
        this.eventID = IDGenerationHelper.generateRandomIDString();
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.category = category;
        this.privateEvent = false;
    }

    // Konstruktor für öffentliche Events mit Teilnehmerbeschränkung
    public PublicEvent(String eventName, String eventDateTime, String category, int maximumCapacity) {
        this.eventID = IDGenerationHelper.generateRandomIDString();
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.category = category;
        this.privateEvent = false;
        this.maximumCapacity = maximumCapacity;
    }

    // Konstruktor für öffentliche Events aus DB
    public PublicEvent(String eventID, String eventName, String eventDateTime, int numberOfBookedUsersOnEvent, String category, boolean privateEvent, int maximumCapacity) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
        this.category = category;
        this.privateEvent = privateEvent;
        this.maximumCapacity = maximumCapacity;
    }

    // #region CRUD-Operationen
    @Override
    public boolean createNewEvent(String eventName, String eventDateTime, String category) {

        return EventManager.createNewEvent(new PublicEvent(eventName, eventDateTime, category));
    }

    // überladene Methode für Events mit maximaler Kapazität
    public boolean createNewEvent(String eventName, String eventDateTime, String category, int maximumCapacity) {

        return EventManager.createNewEvent(new PublicEvent(eventName, eventDateTime, category, maximumCapacity));
    }

    @Override
    public Optional<? extends EventModel> getEventByID(String eventID) {

        return EventManager.readPublicEventByID(eventID);
    }

    @Override
    public void editEvent(String eventID, String eventName, String eventDateTime, ArrayList<String> bookedUsersOnEvent, String category) {

        Optional<PublicEvent> publicEvent = EventManager.readPublicEventByID(eventID);

        if (publicEvent.isPresent()) {
            PublicEvent publicEventToEdit = publicEvent.get();

            publicEventToEdit.setEventName(eventName);
            publicEventToEdit.setEventDateTime(eventDateTime);
            publicEventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
            publicEventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
            publicEventToEdit.setCategory(category);

            EventManager.updateEvent(publicEventToEdit);

            LoggerHelper.logInfoMessage(PublicEvent.class, "Event after editing: " + publicEventToEdit);
        }
    }

    // überladene Methode für Events mit maximaler Kapazität
    public void editEvent(String eventID, String eventName, String eventDateTime, ArrayList<String> bookedUsersOnEvent, String category, int maximumCapacity) {

        Optional<PublicEvent> publicEvent = EventManager.readPublicEventByID(eventID);

        if (publicEvent.isPresent()) {
            PublicEvent publicEventToEdit = publicEvent.get();

            publicEventToEdit.setEventName(eventName);
            publicEventToEdit.setEventDateTime(eventDateTime);
            publicEventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
            publicEventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
            publicEventToEdit.setCategory(category);
            publicEventToEdit.setMaximumCapacity(maximumCapacity);

            EventManager.updateEvent(publicEventToEdit);

            LoggerHelper.logInfoMessage(PublicEvent.class, "Event after editing: " + publicEventToEdit);
        }
    }

    @Override
    public boolean deleteEvent(String eventID) {

        return EventManager.deleteEventByID(eventID);
    }
    // #endregion CRUD-Operationen

    // #region Getter
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    // #endregion Getter

    // #region Setter
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    // #endregion Setter

    // #region toString
    @Override
    public String toString() {
        return "\nevent name: " + eventName + "\nevent date: " + eventDateTime + "\nnumber of booked users: " + numberOfBookedUsersOnEvent +
                "\ncategory: " + category + "\nprivate event: " + privateEvent + "\nmaximum capacity: " + maximumCapacity;
    }
    // #endregion toString
}