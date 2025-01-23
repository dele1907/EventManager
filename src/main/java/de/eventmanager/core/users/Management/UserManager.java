package de.eventmanager.core.users.Management;

import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.User;

import java.util.Optional;

public interface UserManager {
    //#region User related CRUD-Operations

    boolean createNewUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String eMailAddress,
            String password,
            String phoneNumber,
            boolean isAdmin,
            User loggedUser
    );

    void editUser(String userID, String firstName,
                                  String lastName, String dateOfBirth,
                                  String eMailAddress, String password,
                                  String phoneNumber,User loggedUser);

    boolean deleteUser(String userID, User loggedUser);

    Optional<User> getUserByID(String userID, User loggedUser);

    Optional<User> getUserByEmail(String eMailAddress, User loggedUser);

    Optional<PrivateEvent> createPrivateEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode,
                                                              String address, String eventLocation, String description, User loggedUser);
    Optional<PublicEvent> createPublicEvent(String eventName, String eventStart, String eventEnd, String category, String postalCode,
                                                            String address, String eventLocation, String description, int maxParticipants, User loggedUser);

    boolean editEvent(String eventID, String eventName,
                                      String eventStart, String eventEnd, String category,
                                      String postalCode, String address, String eventLocation,
                                      String description, User loggedUser);

    boolean deleteEvent(String eventID, User loggedUser);

    boolean bookEvent(String eventID, User loggedUser);

    boolean cancelEvent(String eventID, User loggedUser);
}
