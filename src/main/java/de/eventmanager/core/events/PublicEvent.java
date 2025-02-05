package de.eventmanager.core.events;

import helper.IDGenerationHelper;

import java.util.ArrayList;

public class PublicEvent extends EventModel {

    private int maximumCapacity = -1;
    private int minimumAge;

    //#region constructors

    /**
     * Regular constructor for initialising a public event
     * */
    public PublicEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode, String city, String address, String eventLocation, String description, int minimumAge) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = false;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
        this.minimumAge = minimumAge;
    }

    /**
     * Regular constructor for initialising a public event with maximum capacity
     * */
    public PublicEvent(String eventName,  String eventStart, String eventEnd, String category, String postalCode, String city, String address,
                       String eventLocation, String description, int maximumCapacity, int minimumAge) {
        this.eventID = IDGenerationHelper.generateRandomUUID();
        this.eventName = eventName;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.category = category;
        this.privateEvent = false;
        this.postalCode = postalCode;
        this.city = city;
        this.address = address;
        this.eventLocation = eventLocation;
        this.description = description;
        this.maximumCapacity = maximumCapacity;
        this.minimumAge = minimumAge;

    }

    /**
     * Constructor for initialising a public event from database
     * */
    public PublicEvent(String eventID, String eventName, String eventStart, String eventEnd, int numberOfBookedUsersOnEvent, ArrayList<String> bookedUsersOnEvent,
                       String category, boolean privateEvent, String postalCode, String city, String address, String eventLocation, String description, int maximumCapacity, int minimumAge) {
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
        this.maximumCapacity = maximumCapacity;
        this.minimumAge = minimumAge;
    }

    //#endregion constructors

    //#region getter

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    // #endregion Getter

    //#region setter

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    // #endregion setter

    //#region toString

    @Override
    public String toString() {
        return  "\nEvent ID: " + eventID +
                "\nEvent name: " + eventName +
                "\nEvent date: " + eventStart + " to " + eventEnd +
                "\nNumber of booked users: " + numberOfBookedUsersOnEvent +
                "\nMaximum capacity: " + (maximumCapacity < 0 ? "unlimited" : maximumCapacity) +
                "\nCategory: " + category +
                "\nPrivate event: " + (privateEvent ? "yes" : "no") +
                "\nPostal code: " + postalCode +
                "\nCity: " + city +
                "\nAddress: " + address +
                "\nEvent location: " + eventLocation +
                "\nDescription: " + description +
                "\nMinimum age: " + minimumAge + "\n";
    }

    //#endregion toString

}