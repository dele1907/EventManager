package de.eventmanager.core.events;

import de.eventmanager.core.users.User;

import java.util.ArrayList;

public abstract class EventModel {

    private String eventID;
    private String eventName;
    private String eventDateTime;
    private int numberOfBookedUsersOnEvent;
    private ArrayList<User> bookedUsersOnEvent;
    private String category;
    private boolean privateEvent;

    // #region CRUD-Operationen
    public abstract boolean createNewEvent(
            String eventName,
            String eventDateTime,
            String category,
            boolean privateEvent
    );

    public abstract EventModel getEventByID(String eventID);

    public abstract void editEvent(
            String eventID,
            String eventName,
            String eventDateTime,
            ArrayList<User> bookedUsersOnEvent,
            String category,
            boolean privateEvent
    );

    public abstract boolean deleteEvent(String eventID);
    // #endregion CRUD-Operationen

    // #region Getter
    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public int getNumberOfBookedUsersOnEvent() {
        return numberOfBookedUsersOnEvent;
    }

    public ArrayList<User> getBookedUsersOnEvent() {
        return bookedUsersOnEvent;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }
    // #endregion Getter

    // #region Setter
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public void setNumberOfBookedUsersOnEvent(int numberOfBookedUsersOnEvent) {
        this.numberOfBookedUsersOnEvent = numberOfBookedUsersOnEvent;
    }

    public void setBookedUsersOnEvent(ArrayList<User> bookedUsersOnEvent) {
        this.bookedUsersOnEvent = bookedUsersOnEvent;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }
    // #endregion Setter

}
