package de.eventmanager.core.observer;

import de.eventmanager.core.database.Communication.NotificationDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.notifications.Notification;
import de.eventmanager.core.users.User;

import java.util.Objects;

public class UserObserver implements Observer {

    private final User user;
    private final EventModel event;

    /**
     * Wrapping a user as an observer for event notifications
     * */
    public UserObserver(User user, EventModel event) {
        this.user = user;
        this.event = event;
    }

    //#region observer

    /**
     * Adding a user notification if an event was updated
     * */
    @Override
    public void update(EventModel event) {

        if (!this.event.equals(event)) {
            return;
        }

        String updateMessage = "-- UPDATE INFORMATION --" + event;

        NotificationDatabaseConnector.addNotification(new Notification(this.user.getUserID(), updateMessage));
    }

    //#endregion observer

    public User getUser() {

        return user;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {

            return true;
        }
        if (object == null || getClass() != object.getClass()) {

            return false;
        }

        UserObserver other = (UserObserver) object;

        return user.equals(other.user) && event.equals(other.event);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, event);
    }

}
