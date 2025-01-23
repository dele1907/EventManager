package de.eventmanager.core.events;

import de.eventmanager.core.events.Management.EventManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;

import java.util.ArrayList;
import java.util.Optional;

public class PrivateEvent extends EventModel{

    //#region constructors

    public PrivateEvent(String eventName, String eventStart, String eventEnd, String category,
                        String postalCode, String address, String eventLocation, String description) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = true;
        this.postalCode = postalCode;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
    }

    // Konstruktor für private Events aus DB
    public PrivateEvent(String eventID, String eventName, String eventStart, String eventEnd, int numberOfBookedUsersOnEvent, String category, boolean privateEvent,
                        String postalCode, String address, String eventLocation, String description) {
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
    }

    //#region toString
    @Override
    public String toString() {
        return "Event: \nevent name: " + eventName + "\nevent date: " + eventStart + " to " + eventEnd + "\nnumber of booked users: " + numberOfBookedUsersOnEvent +
                "\ncategory: " + category + "\nprivate event: " + privateEvent + "\nPostal-Code: " + postalCode +
                "\naddress: " + address + "\nevent location: " + eventLocation + "\ndescription: " + description;
    }
    //#endregion toString
}
