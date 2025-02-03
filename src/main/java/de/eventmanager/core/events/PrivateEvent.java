package de.eventmanager.core.events;

import helper.IDGenerationHelper;

import java.util.ArrayList;

public class PrivateEvent extends EventModel{

    //#region constructors

    /**
     * Regular constructor for initialising a private event
     * */
    public PrivateEvent(String eventName, String eventStart, String eventEnd, String category,
                        String postalCode, String city, String address, String eventLocation, String description) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = true;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
    }

    /**
     * Constructor for initialising a private event from database
     * */
    public PrivateEvent(String eventID, String eventName, String eventStart, String eventEnd, int numberOfBookedUsersOnEvent, ArrayList<String> bookedUsersOnEvent,
                        String category, boolean privateEvent, String postalCode, String city, String address, String eventLocation, String description) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
        this.bookedUsersOnEvent = bookedUsersOnEvent;
        this.category = category;
        this.privateEvent = privateEvent;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
    }

    //#endregion constructors

    //#region toString

    @Override
    public String toString() {
        return  "\nEvent ID: " + eventID +
                "\nEvent name: " + eventName +
                "\nEvent date: " + eventStart + " to " + eventEnd +
                "\nNumber of booked users: " + numberOfBookedUsersOnEvent +
                "\nCategory: " + category +
                "\nPrivate event: " + (privateEvent ? "yes" : "no") +
                "\nPostal code: " + postalCode +
                "\nCity: " + city +
                "\nAddress: " + address +
                "\nEvent location: " + eventLocation +
                "\nDescription: " + description + "\n";
    }

    //#endregion toString

}
