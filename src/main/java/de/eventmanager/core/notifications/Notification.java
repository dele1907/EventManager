package de.eventmanager.core.notifications;

import helper.IDGenerationHelper;

import java.util.Objects;

public class Notification {

    private String notificationID;
    private String userID;
    private String message;
    private boolean markedAsRead;

    //#region constructors

    /**
     * Regular constructor for initialising a notification
     * */
    public Notification(String userID, String message) {
        this.notificationID = IDGenerationHelper.generateRandomUUID();
        this.userID = userID;
        this.message = message;
        this.markedAsRead = false;
    }

    /**
     * Constructor for initialising a notification from database
     * */
    public Notification(String notificationID, String userID, String message, boolean markedAsRead) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.message = message;
        this.markedAsRead = markedAsRead;
    }

    //#endregion constructors

    //#region getter

    public String getNotificationID() {
        return notificationID;
    }

    public String getUserID() {
        return userID;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMarkedAsRead() {
        return markedAsRead;
    }

    //#endregion getter

    //#region setter

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMarkedAsRead(boolean markedAsRead) {
        this.markedAsRead = markedAsRead;
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

        Notification other = (Notification) object;

        return notificationID.equals(other.notificationID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(notificationID);
    }

}
