package de.eventmanager.core.database.Communication.notifications;

public class Notification {

    private String notificationID;
    private String userID;
    private String message;

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

    //#endregion setter

}
