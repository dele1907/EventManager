package de.eventmanager.core.notifications;

public class Notification {

    private String notificationID;
    private String userID;
    private String message;
    private boolean markedAsRead;

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

}
