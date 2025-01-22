package de.eventmanager.core.events;

import de.eventmanager.core.events.Management.EventManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;

import java.util.ArrayList;
import java.util.Optional;

public class PublicEvent extends EventModel {

    private int maximumCapacity = -1;

    //#region constructors

    // Konstruktor für öffentliche Events ohne Teilnehmerbeschränkung
    public PublicEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode, String address, String eventLocation, String description) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = false;
        this.postalCode = postalCode;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
    }

    // Konstruktor für öffentliche Events mit Teilnehmerbeschränkung
    public PublicEvent(String eventName,  String eventStart, String eventEnd, String category, String postalCode, String address,
                       String eventLocation, String description, int maximumCapacity) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = false;
        this.postalCode = postalCode;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
        this.maximumCapacity = maximumCapacity;

    }

    // Konstruktor für öffentliche Events aus DB
    public PublicEvent(String eventID, String eventName, String eventStart, String eventEnd, int numberOfBookedUsersOnEvent,
                       String category, boolean privateEvent, String postalCode, String address, String eventLocation, String description, int maximumCapacity) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
        this.category = category;
        this.privateEvent = privateEvent;
        this.postalCode = postalCode;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
        this.maximumCapacity = maximumCapacity;
    }

    //#endregion constructors

    /*
    // #region CRUD-Operationen
    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode,
                                  String address, String eventLocation, String description) {

        return EventManager.createNewEvent(new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description));
    }

    // überladene Methode für Events mit maximaler Kapazität
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode,
                                  String address, String eventLocation, String description, int maximumCapacity) {

        return EventManager.createNewEvent(new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description, maximumCapacity));
    }

    @Override
    public Optional<? extends EventModel> getEventByID(String eventID) {

        return EventManager.readPublicEventByID(eventID);
    }

    @Override
    public void editEvent(String eventID, String eventName, String eventStart, String eventEnd, ArrayList<String> bookedUsersOnEvent,
                          String category, String postalCode, String address, String eventLocation, String description) {

        Optional<PublicEvent> publicEvent = EventManager.readPublicEventByID(eventID);

        if (publicEvent.isPresent()) {
            PublicEvent publicEventToEdit = publicEvent.get();

            publicEventToEdit.setEventName(eventName);
            publicEventToEdit.setEventStart(eventStart);
            publicEventToEdit.setEventEnd(eventEnd);
            publicEventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
            publicEventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
            publicEventToEdit.setCategory(category);
            publicEventToEdit.setPostalCode(postalCode);
            EventManager.updateEvent(publicEventToEdit);

            LoggerHelper.logInfoMessage(PublicEvent.class, "Event after editing: " + publicEventToEdit);
        }
    }

    // überladene Methode für Events mit maximaler Kapazität
    public void editEvent(String eventID, String eventName, String eventStart, String eventEnd, ArrayList<String> bookedUsersOnEvent, String category,
                          int maximumCapacity, String postalCode, String address, String eventLocation, String description) {

        Optional<PublicEvent> publicEvent = EventManager.readPublicEventByID(eventID);

        if (publicEvent.isPresent()) {
            PublicEvent publicEventToEdit = publicEvent.get();

            publicEventToEdit.setEventName(eventName);
            publicEventToEdit.setEventStart(eventStart);
            publicEventToEdit.setEventEnd(eventEnd);
            publicEventToEdit.setNumberOfBookedUsersOnEvent(bookedUsersOnEvent.size());
            publicEventToEdit.setBookedUsersOnEvent(bookedUsersOnEvent);
            publicEventToEdit.setCategory(category);
            publicEventToEdit.setMaximumCapacity(maximumCapacity);
            publicEventToEdit.setPostalCode(postalCode);
            publicEventToEdit.setAddress(address);
            publicEventToEdit.setEventLocation(eventLocation);
            publicEventToEdit.setDescription(description);

            EventManager.updateEvent(publicEventToEdit);

            LoggerHelper.logInfoMessage(PublicEvent.class, "Event after editing: " + publicEventToEdit);
        }
    }

    @Override
    public boolean deleteEvent(String eventID) {

        return EventManager.deleteEventByID(eventID);
    }
    // #endregion CRUD-Operationen
    */

    //#region Getter
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    // #endregion Getter

    //#region Setter
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    // #endregion Setter

    //#region toString
    @Override
    public String toString() {
        return "Event: \nevent name: " + eventName + "\nevent date: " + eventStart + " to " + eventEnd + "\nnumber of booked users: " + numberOfBookedUsersOnEvent +
                "\ncategory: " + category + "\nprivate event: " + privateEvent + "\nmaximum capacity: " + maximumCapacity + "\nPostal-Code: " + postalCode +
                "\naddress: " + address + "\nevent location: " + eventLocation + "\ndescription: " + description;
    }
    //#endregion toString
}