package de.eventmanager.core.events;

import java.util.ArrayList;
import java.util.Objects;

public abstract class EventModel {

    String eventID;
    String eventName;
    String eventStart;
    String eventEnd;
    int numberOfBookedUsersOnEvent = 0;
    ArrayList<String> bookedUsersOnEvent = new ArrayList<>();
    String category;
    boolean privateEvent;
    String postalCode;
    String city;
    String address;
    String eventLocation;
    String description;


    //#region getter

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

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public String getDescription() {
        return description;
    }

    //#endregion getter

    //#region setter

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

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
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

    //#endregion setter

    @Override
    public boolean equals(Object object) {

        if (this == object) {

            return true;
        }
        if (object == null || getClass() != object.getClass()) {

            return false;
        }

        EventModel other = (EventModel) object;

        return eventID.equals(other.eventID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(eventID);
    }

}
