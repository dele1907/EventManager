package de.eventmanager.core.users.Management;

import de.eventmanager.core.users.User;

import java.util.ArrayList;
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
            String loggedUserByID
    );

    void editUser(String userID, String firstName,
                                  String lastName, String dateOfBirth,
                                  String eMailAddress, String password,
                                  String phoneNumber, String loggedUserByID);

    boolean deleteUser(String eMailUserToDelete, String loggedUserByID);

    Optional<User> getUserByID(String userID);

    Optional<User> getUserByEmail(String eMailAddress);

    //#endregion User related Crud-Operations

    //#region Event related CRUD-Operations
    String getEventInformationByEventID(String eventID);
    boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category,
                           String postalCode, String city, String address, String eventLocation,
                           String description, int maxParticipants, boolean isPrivateEvent, String loggedUserID);

    boolean editEvent(String eventID, String eventName,
                      String eventStart, String eventEnd, String category,
                      String postalCode, String city, String address,
                      String eventLocation, String description, String loggedUserID);

    boolean deleteEvent(String eventID, String loggedUserID);

    ArrayList<String> showEventParticipantList(String eventID);

    //#endregion Event related CRUD-Operations

    //#region Event-Operations

    boolean bookEvent(String eventID, String loggedUserID);

    boolean cancelEvent(String eventID, String loggedUserID);

    boolean addUserToEvent(String eventID, String userEmail, String loggedUserID);

    boolean removeUserFromEvent(String eventID, String userEmail, String loggedUserID);

    //#endregion Event-Operations

    //#region Permission-Operations

    void addAdminStatusToUser(User user);

    void removeAdminStatusFromUser(User user);

    void addAdminStatusToUserByUserID(String userID, User loggedUser);

    void removeAdminStatusFromUserByUserID(String userID, User loggedUser);

    //#endregion Permission-Operations

    //#region Registration & Authentication

    boolean isValidRegistrationPassword(String password, String checkPassword);
    boolean authenticationUserLogin(String email, String password);

    //#endregion Registration & Authentication

}
