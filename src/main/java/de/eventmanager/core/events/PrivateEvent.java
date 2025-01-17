package de.eventmanager.core.events;

import de.eventmanager.core.users.User;
import helper.IDGenerationHelper;

import java.util.ArrayList;

public class PrivateEvent extends EventModel{

    // Konstruktor für private Events
    public PrivateEvent(String eventName, String eventDateTime, String category, boolean privateEvent) {
        this.eventID = IDGenerationHelper.generateRandomIDString();
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.category = category;
        this.privateEvent = privateEvent;
    }

    // #region CRUD-Operationen
    @Override
    public boolean createNewEvent(String eventName, String eventDateTime, String category, boolean privateEvent) {
        return false;
    }

    @Override
    public EventModel getEventByID(String eventID) {
        return null;
    }

    @Override
    public void editEvent(String eventID, String eventName, String eventDateTime, ArrayList<User> bookedUsersOnEvent, String category, boolean privateEvent) {

    }

    @Override
    public boolean deleteEvent(String eventID) {
        return false;
    }
    // #endregion CRUD-Operationen

}
