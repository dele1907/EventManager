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