package de.eventmanager.core.events;

import java.util.ArrayList;
import java.util.Optional;

public abstract class EventModel {

    String eventID;
    String eventName;
    String eventStart;
    String eventEnd;
    int numberOfBookedUsersOnEvent = 0;
    ArrayList<String> bookedUsersOnEvent = null;
    String category;
    boolean privateEvent;
    String description;
    String city;
    String postalCode;
    String address;
    String eventLocation;

    //#region CRUD-Operationen
    public abstract boolean createNewEvent(
            String eventName,
            String eventStart,
            String eventEnd,
            String category,
            String postalCode,
            String address,
            String eventLocation,
            String description
    );

    public abstract Optional<? extends EventModel> getEventByID(String eventID);

    public abstract void editEvent(
            String eventID,
            String eventName,
            String eventStart,
            String eventEnd,
            ArrayList<String> bookedUsersOnEvent,
            String category,
            String postalCode,
            String address,
            String eventLocation,
            String description
    );

    public abstract boolean deleteEvent(String eventID);

    //#endregion CRUD-Operationen

    //#region Getter
    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventStart() {
        return eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public int getNumberOfBookedUsersOnEvent() {
        return numberOfBookedUsersOnEvent;
    }

    public ArrayList<String> getBookedUsersOnEvent() {
        return bookedUsersOnEvent;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public String getAddress() {
        return address;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getPostalCode() {
        return postalCode;
    }

    //#endregion Getter

    //#region Setter
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public void setNumberOfBookedUsersOnEvent(int numberOfBookedUsersOnEvent) {
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
    }

    public void setBookedUsersOnEvent(ArrayList<String> bookedUsersOnEvent) {
        this.bookedUsersOnEvent = bookedUsersOnEvent;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    //#endregion Setter




}
