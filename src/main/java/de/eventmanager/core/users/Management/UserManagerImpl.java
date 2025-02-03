package de.eventmanager.core.users.Management;

import de.eventmanager.core.database.Communication.BookingDatabaseConnector;
import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.observer.EventNotificator;
import de.eventmanager.core.observer.UserObserver;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;
import helper.ConfigurationDataSupplierHelper;
import helper.LoggerHelper;
import helper.PasswordHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class UserManagerImpl implements UserManager {

    private final Logger logger = LogManager.getLogger(User.class);
    private final EventNotificator eventNotificator = EventNotificator.getInstance();

    //#region Constant variables

    private final String GENERAL_MISSING_PERMISSION = "Permission denied!";
    private final String NOT_EVENT_CREATOR_OR_ADMIN = "Only the Event-Creator or Admins can do this!";
    private final String EVENT_NOT_FOUND = "Event not found!";
    private final String USER_NOT_FOUND = "User not found!";


    //#endregion Constant variables

    //#region User related CRUD-Operations
    /**
     * <h3>Create new User</h3>
     * {@code createNewUser()} accepts user parameters as arguments to initialize a new User object
     * and load it into the database with {@code UserManager.createNewUser()}.
     * @see UserDatabaseConnector UserManager
     */

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                 String password, String phoneNumber, boolean isAdmin, String loggedUserByID) {

        if (checkUserSelfRegisterOrCreated(loggedUserByID)) {
            LoggerHelper.logErrorMessage(User.class, GENERAL_MISSING_PERMISSION);

            return false;
        }

        UserDatabaseConnector.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        return true;
    }

    /**
     * <h3>Edit User</h3>
     * {@code editUser()} accepts the userID of the user you want to edit and the parameters you want to modify.
     * @see UserDatabaseConnector UserManager
     */

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress,
                         String password, String phoneNumber, String loggedUserByID) {
        Optional<User> user = UserDatabaseConnector.readUserByID(userID);

        if (user.isEmpty() || getUserByID(loggedUserByID).isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, USER_NOT_FOUND);

            return;
        }

        if (!getUserByID(loggedUserByID).get().getRole().equals(Role.ADMIN)){
            logger.error(GENERAL_MISSING_PERMISSION);

            return;
        }

        User userToEdit = user.get();
        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userToEdit.setDateOfBirth(dateOfBirth);
        userToEdit.seteMailAddress(eMailAddress);
        userToEdit.setPassword(password);
        userToEdit.setPhoneNumber(phoneNumber);

        UserDatabaseConnector.updateUser(userToEdit);

        LoggerHelper.logInfoMessage(User.class, "User after Editing: " + userToEdit);
    }

    /**
     * <h3>Delete User</h3>
     * {@code deleteUser()} accepts the eMail of the user you want to delete.
     * @see UserDatabaseConnector UserManager
     */

    @Override
    public boolean deleteUser(String eMailUserToDelete, String loggedUserByID) {
        var loggedUserOptional = UserDatabaseConnector.readUserByID(loggedUserByID);

        if (loggedUserOptional.isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, USER_NOT_FOUND);

            return false;
        }

        var loggedUser = loggedUserOptional.get();

        if (!loggedUser.getRole().equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, GENERAL_MISSING_PERMISSION);

            return false;
        }

        return UserDatabaseConnector.deleteUserByEmail(eMailUserToDelete);
    }

    @Override
    public Optional<User> getUserByID(String userID) {
        return UserDatabaseConnector.readUserByID(userID);
    }

    @Override
    public Optional<User> getUserByEmail(String eMailAddress) {
        return UserDatabaseConnector.readUserByEMail(eMailAddress);
    }

    private boolean checkUserSelfRegisterOrCreated(String loggedUserByID) {
        return !loggedUserByID.equals(ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID) &&
                !UserDatabaseConnector.readUserByID(loggedUserByID)
                        .map(user -> user.getRole().equals(Role.ADMIN))
                        .orElse(false);
    }

    //#endregion User related CRUD-Operations

    //#region Event related CRUD-Operations
    @Override
    public String getEventInformationByEventID(String eventID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (optionalEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return null;
        }

        return optionalEvent.get().toString();
    }

    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category,
                                  String postalCode, String city, String address, String eventLocation,
                                  String description, int maxParticipants, boolean isPrivateEvent, String loggedUserID) {

        return isPrivateEvent ?
                createPrivateEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation,
                        description, loggedUserID).isPresent() :
                createPublicEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation,
                        description, maxParticipants, loggedUserID).isPresent();
    }

    /**
     * <h3>Create Private-Event</h3>
     * {@code createPrivateEvent()} create a new Private Event and safe the EventCreator in the Database.
     * @see EventDatabaseConnector EventDatabaseConnector
     */
    private Optional<PrivateEvent> createPrivateEvent(String eventName, String eventStart,
                                                     String eventEnd, String category,
                                                     String postalCode, String city,
                                                     String address, String eventLocation,
                                                     String description, String loggedUserID) {
        PrivateEvent event = new PrivateEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation, description);
        EventDatabaseConnector.createNewEvent(event);
        CreatorDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUserID);

        return Optional.of(event);
    }

    /**
     * <h3>Create Public-Event</h3>
     * {@code createPublicEvent()} create a new Public Event and safe the EventCreator in the Database.
     * @see EventDatabaseConnector EventDatabaseConnector
     */
    private Optional<PublicEvent> createPublicEvent(String eventName, String eventStart,
                                                   String eventEnd, String category,
                                                   String postalCode, String city,
                                                   String address, String eventLocation,
                                                   String description, int maxParticipants,
                                                   String loggedUserID) {
        if (maxParticipants == 0) {
            maxParticipants = -1;
        }

        PublicEvent event = new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation, description, maxParticipants);
        EventDatabaseConnector.createNewEvent(event);
        CreatorDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUserID);

        return Optional.of(event);
    }

    /**
     * <h3>Edit Event</h3>
     * {@code editEvent()} edit an Event by eventID.
     * Only Admins or the EventCreator can edit the Event
     * @see EventDatabaseConnector EventDatabaseConnector
     */


    @Override
    public boolean editEvent(String eventID, String eventName,
                             String eventStart, String eventEnd,
                             String category, String postalCode,
                             String city, String address,
                             String eventLocation, String description,
                             String loggedUserID
    ) {
        Optional<? extends EventModel> optionalEvent = getEventByID(eventID);

        if (!optionalEvent.isPresent()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, NOT_EVENT_CREATOR_OR_ADMIN);

            return false;
        }

        EventModel eventToEdit = optionalEvent.get();

        eventToEdit.setEventName(eventName);
        eventToEdit.setEventStart(eventStart);
        eventToEdit.setEventEnd(eventEnd);
        eventToEdit.setCategory(category);
        eventToEdit.setPostalCode(postalCode);
        eventToEdit.setCity(city);
        eventToEdit.setAddress(address);
        eventToEdit.setEventLocation(eventLocation);
        eventToEdit.setDescription(description);

        EventDatabaseConnector.updateEvent(eventToEdit);
        eventNotificator.notifyObservers(eventToEdit);
        LoggerHelper.logInfoMessage(User.class, "Event after editing: " + eventToEdit);

        return true;
    }

    @Override
    public Optional<? extends EventModel> getEventByID(String eventID) {
        return EventDatabaseConnector.readEventByID(eventID);
    }

    /**
     * <h3>Delete Event</h3>
     * {@code deleteEvent()} delete an Event by eventID.
     * Only Admins or the EventCreator can delete the Event!
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public boolean deleteEvent(String eventID, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> optionalEventCreator = CreatorDatabaseConnector.getEventCreator(eventID);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);
        String userIDofUserCreatedEvent = "";

        if (optionalEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (loggedUser.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, USER_NOT_FOUND);

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, NOT_EVENT_CREATOR_OR_ADMIN);

            return false;
        }

        if (optionalEventCreator.isPresent()) {
            userIDofUserCreatedEvent = optionalEventCreator.get().getUserID();
        }

        CreatorDatabaseConnector.removeUserAsEventCreator(eventID,userIDofUserCreatedEvent);

        return EventDatabaseConnector.deleteEventByID(eventID);
    }

    /**
     * <h3>Show EventParticipant-List</h3>
     * {@code showEventParticipantList()} returns an ArrayList<String>
     * with all Participants of an Event.
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public ArrayList<String> showEventParticipantList(String eventID) {
        Optional<?extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        ArrayList<String> participants = optionalEvent.get().getBookedUsersOnEvent();

        if (!optionalEvent.isPresent()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return null;
        }

        return participants;
    }

    //#endregion Event related CRUD-Operations

    //#region Event-Operations

    /**
     * <h3>Book Event</h3>
     * {@code bookEvent()} books an Event for the loggedUser by eventID.
     * Only Public Events can be booked.
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public boolean bookEvent(String eventID, String loggedUserID) {
        Optional<PublicEvent> publicEvent = EventDatabaseConnector.readPublicEventByID(eventID);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (publicEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (publicEvent.get().isPrivateEvent()){

            return false;
        }

        if (loggedUser.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, USER_NOT_FOUND);

            return false;
        }

        boolean hasUserBookedTheEvent = publicEvent.get().getBookedUsersOnEvent().contains(loggedUser.get().getEMailAddress());

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "Event already booked!");

            return false;
        }

        BookingDatabaseConnector.addBooking(eventID,loggedUserID);
        eventNotificator.addObserver(new UserObserver(loggedUser.get(), publicEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "Event booked successfully!");

        return true;
    }

    /**
     * <h3>Cancel Event</h3>
     * {@code cancelEvent()} cancels an Event for the loggedUser by eventID.
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public boolean cancelEvent(String eventID, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (optionalEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (loggedUser.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, USER_NOT_FOUND);

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(loggedUser.get().getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }

        BookingDatabaseConnector.removeBooking(eventID,loggedUserID);
        eventNotificator.removeObserver(new UserObserver(loggedUser.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "Event cancelled successfully!");

        return true;
    }

    /**
     * <h3>Add User to Event</h3>
     * {@code addUserToEvent()} add the user, with the provided email, to the Event, by eventID.
     * Only Admins or the EventCreator can add Users to the Event!
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public boolean addUserToEvent(String eventID, String userEmail, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> userToAdd = UserDatabaseConnector.readUserByEMail(userEmail);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (optionalEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (loggedUser.isEmpty() || userToAdd.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, USER_NOT_FOUND);

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(User.class, NOT_EVENT_CREATOR_OR_ADMIN);

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToAdd.get().getEMailAddress());

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "User is already booked for this event!");

            return false;
        }

        BookingDatabaseConnector.addBooking(eventID, userToAdd.get().getUserID());
        eventNotificator.addObserver(new UserObserver(userToAdd.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "User added to event successfully!");

        return true;
    }

    /**
     * <h3>Remove User to Event</h3>
     * {@code removeUserToEvent()} remove the User, with the provided email, from the Event, by eventID.
     * Only Admins or the EventCreator can remove Users from the Event!
     * @see EventDatabaseConnector EventDatabaseConnector
     */

    @Override
    public boolean removeUserFromEvent(String eventID, String userEmail, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> userToRemove = UserDatabaseConnector.readUserByEMail(userEmail);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (optionalEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, EVENT_NOT_FOUND);

            return false;
        }

        if (loggedUser.isEmpty() || userToRemove.isEmpty()) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, USER_NOT_FOUND);

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(User.class, NOT_EVENT_CREATOR_OR_ADMIN);

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToRemove.get().getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }

        BookingDatabaseConnector.removeBooking(eventID,userToRemove.get().getUserID());
        eventNotificator.addObserver(new UserObserver(userToRemove.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "User removed from event successfully!");

        return true;
    }

    //#endregion Event-Operations

    //#region Permission-Operations

    @Override
    public void addAdminStatusToUser(User user){
        user.setRoleAdmin(true);
    }

    @Override
    public void removeAdminStatusFromUser(User user) {
        user.setRoleAdmin(false);
    }

    @Override
    public void addAdminStatusToUserByUserID(String userID, User loggedUser) {
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            LoggerHelper.logErrorMessage(User.class, GENERAL_MISSING_PERMISSION);

            return;
        }

        this.addAdminStatusToUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    @Override
    public void removeAdminStatusFromUserByUserID(String userID, User loggedUser) {
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            LoggerHelper.logErrorMessage(User.class, GENERAL_MISSING_PERMISSION);

            return;
        }

        this.removeAdminStatusFromUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    private boolean checkPermissionForEventOperations(String loggedUserID, String eventID) {
        return getUserByID(loggedUserID).get().getRole().equals(Role.ADMIN) ||
                CreatorDatabaseConnector.checkIfUserIsEventCreator(eventID, loggedUserID);
    }

    //#endregion Permission-Operations

    //#region Registration & Authentication
    @Override
    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    /**
     * <h3>Validate Password</h3>
     * <p>
     * {@code isValidPassword()} checks whether a given password contains any restricted characters from a predefined list.
     * If the password contains any of the restricted characters, the method returns {@code false}. If no restricted characters are found, it returns {@code true}.
     * </p>
     */
    private boolean isValidPassword(String password) {
        char[] restrictedCharacters = {' ', '$', '@', '§', '&', '%', 'ä', 'ö', 'ü', 'ß', 'Ä', 'Ü', 'Ö'};

        for (var restricted : restrictedCharacters) {
            if (password.indexOf(restricted) != -1) {
                return false;
            }
        }

        return true;
    }

    private boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {

            LoggerHelper.logErrorMessage(User.class, "Wrong password!");

            return false;
        }

        return checkPassword.equals(password);
    }

     /** <h3>User Login Authentication</h3>
     The method {@code authenticationUserLogin()} authenticates the user by checking
      if the email exists, and verifies whether the provided password matches the one stored
     in the DB. If both conditions are met, the user is successfully logged in.
     */
    @Override
    public boolean authenticationUserLogin(String email, String password) {
        Optional<User> userOptional = getUserByEmail(email);

        if (userOptional.isEmpty()) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, "Email address not found");

            return false;

        }

        return PasswordHelper.verifyPassword(password, userOptional.get().getPassword());
    }
    //#endregion Registration & Authentication

}
