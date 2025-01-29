package de.eventmanager.core.users.Management;

import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.observer.Observer;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;
import helper.ConfigurationDataSupplierHelper;
import helper.LoggerHelper;
import helper.PasswordHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class UserManagerImpl implements UserManager, Observer {

    Logger logger = LogManager.getLogger(User.class);

    //#region Constant variables

    private final String NO_PERMISSION_CREATE_USER = "User has no permission to create a new user";
    private final String NO_PERMISSION_EDIT_USER = "User has no permission to edit a user";
    private final String NO_PERMISSION_DELETE_USER = "User has no permission to delete a user";
    private final String NO_PERMISSION_GET_USER_INFORMATION = "User has no permission to get user information";
    private final String NO_PERMISSION_GIVE_ADMIN_STATUS = "User has no permission to give admin status";
    private final String NO_PERMISSION_REMOVE_ADMIN_STATUS = "User has no permission to remove admin status";

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
                                 String password, String phoneNumber, boolean isAdmin, String loggedInUserUserID) {

        if (checkUserSelfRegisterOrCreated(loggedInUserUserID)) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_CREATE_USER);

            return false;
        }

        UserDatabaseConnector.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        return true;
    }

    private boolean checkUserSelfRegisterOrCreated(String loggedInUserUserID) {
        return !loggedInUserUserID.equals(ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID) &&
                !UserDatabaseConnector.readUserByID(loggedInUserUserID)
                        .map(user -> user.getRole().equals(Role.ADMIN))
                        .orElse(false);
    }

    /**
     * <h3>Edit User</h3>
     * {@code editUser()} accepts the userID of the user you want to edit and the parameters you want to modify.
     * @see UserDatabaseConnector UserManager
     */

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress,
                         String password, String phoneNumber, User loggedUser) {
        

        if (!loggedUser.getRole().equals(Role.ADMIN)){
            logger.error(NO_PERMISSION_EDIT_USER);

            return;
        }

        Optional<User> user = UserDatabaseConnector.readUserByID(userID);

        if (user.isPresent()){
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
    }

    /**
     * <h3>Delete User</h3>
     * {@code deleteUser()} accepts the eMail of the user you want to delete.
     * @see UserDatabaseConnector UserManager
     */


    @Override
    public boolean deleteUser(String eMailUserToDelete, String loggedInUserID) {
        var loggedUserOptional = UserDatabaseConnector.readUserByID(loggedInUserID);

        if (loggedUserOptional.isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, "User not found");

            return false;
        }

        var loggedUser = loggedUserOptional.get();

        if (!loggedUser.getRole().equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_DELETE_USER);

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

    //#endregion User related CRUD-Operations

    //#region Event related CRUD-Operations

    @Override
    public Optional<PrivateEvent> createPrivateEvent(String eventName, String eventStart, String eventEnd, String category,
                                                     String postalCode, String address, String eventLocation, String description, User loggedUser) {
        
        PrivateEvent event = new PrivateEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description);
        EventDatabaseConnector.createNewEvent(event);
        EventDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUser.getUserID());

        return Optional.of(event);
    }

    @Override
    public Optional<PublicEvent> createPublicEvent(String eventName, String eventStart, String eventEnd, String category,
                                                   String postalCode, String address, String eventLocation, String description, int maxParticipants, User loggedUser) {
        

        if (maxParticipants == 0) {
            maxParticipants = -1;
        }

        PublicEvent event = new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description, maxParticipants);
        EventDatabaseConnector.createNewEvent(event);
        EventDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUser.getUserID());

        return Optional.of(event);
    }

    @Override
    public boolean editEvent(String eventID, String eventName,
                             String eventStart, String eventEnd,
                             String category, String postalCode,
                             String address, String eventLocation,
                             String description, User loggedUser
    ) {
        
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (!isEventExisting(optionalEvent)) {

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUser, eventID)) {

            return false;
        }

        EventModel eventToEdit = optionalEvent.get();

        eventToEdit.setEventName(eventName);
        eventToEdit.setEventStart(eventStart);
        eventToEdit.setEventEnd(eventEnd);
        eventToEdit.setCategory(category);
        eventToEdit.setPostalCode(postalCode);
        eventToEdit.setAddress(address);
        eventToEdit.setEventLocation(eventLocation);
        eventToEdit.setDescription(description);

        EventDatabaseConnector.updateEvent(eventToEdit);

        LoggerHelper.logInfoMessage(User.class, "Event after editing: " + eventToEdit);

        return true;
    }

    @Override
    public boolean deleteEvent(String eventID, User loggedUser) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (!isEventExisting(optionalEvent)) {

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUser, eventID)) {

            return false;
        }

        String userIDofUserCreatedEvent = EventDatabaseConnector.getEventCreator(eventID).get().getUserID();
        EventDatabaseConnector.removeUserAsEventCreator(eventID,userIDofUserCreatedEvent);

        return EventDatabaseConnector.deleteEventByID(eventID);
    }
    @Override
    public ArrayList<String> showEventParticipantList(String eventID) {
        Optional<?extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        ArrayList<String> participants = optionalEvent.get().getBookedUsersOnEvent();

        if (!isEventExisting(optionalEvent)) {

            return null;
        }

        return participants;
    }

    //#endregion Event related CRUD-Operations

    //#region Event-Operations

    @Override
    public boolean bookEvent(String eventID, User loggedUser) {
        Optional<PublicEvent> publicEvent = EventDatabaseConnector.readPublicEventByID(eventID);

        if (!isEventExisting(publicEvent)) {

            return false;
        }

        if (publicEvent.get().isPrivateEvent()){

            return false;
        }

        boolean hasUserBookedTheEvent = publicEvent.get().getBookedUsersOnEvent().contains(loggedUser.getEMailAddress());

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "Event already booked!");

            return false;
        }

        EventDatabaseConnector.addBooking(eventID,loggedUser.getUserID());
        LoggerHelper.logInfoMessage(User.class, "Event booked successfully!");

        return true;
    }

    @Override
    public boolean cancelEvent(String eventID, User loggedUser) {

        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (!isEventExisting(optionalEvent)) {

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(loggedUser.getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }


        EventDatabaseConnector.removeBooking(eventID,loggedUser.getUserID());
        LoggerHelper.logInfoMessage(User.class, "Event cancelled successfully!");

        return true;
    }

    @Override
    public boolean addUserToEvent(String eventID, String userEmail, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> userToAdd = UserDatabaseConnector.readUserByEMail(userEmail);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!isEventExisting(optionalEvent) || !loggedUser.isPresent() || !userToAdd.isPresent()) {

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUser.get(), eventID)) {
            LoggerHelper.logErrorMessage(User.class, "You do not have permission to add user to this event!");

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToAdd.get().getEMailAddress());

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "Event already booked!");

            return false;
        }

        EventDatabaseConnector.addBooking(eventID, userToAdd.get().getUserID());

        return true;
    }

    @Override
    public boolean removeUserFromEvent(String eventID, String userEmail, String loggedUserID) {
        Optional<? extends EventModel> optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        Optional<User> userToRemove = UserDatabaseConnector.readUserByEMail(userEmail);
        Optional<User> loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!isEventExisting(optionalEvent) || !loggedUser.isPresent() || !userToRemove.isPresent()) {

            return false;
        }

        if (!checkPermissionForEventOperations(loggedUser.get(), eventID)) {
            LoggerHelper.logErrorMessage(User.class, "You do not have permission to remove user from this event!");

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToRemove.get().getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }

        EventDatabaseConnector.removeBooking(eventID,userToRemove.get().getUserID());

        return true;
    }

    private boolean isEventExisting(Optional<? extends EventModel> event) {
        if (event.isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, "Event not found");

            return false;
        }

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
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GIVE_ADMIN_STATUS);

            return;
        }

        this.addAdminStatusToUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    @Override
    public void removeAdminStatusFromUserByUserID(String userID, User loggedUser) {
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    private boolean checkPermissionForEventOperations(User loggedUser, String eventID) {
        return loggedUser.getRole().equals(Role.ADMIN) || EventDatabaseConnector.checkIfUserIsEventCreator(eventID, loggedUser.getUserID());
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

            System.out.println("Wrong password!");

            return false;
        }

        return checkPassword.equals(password);
    }

     /* <h3>User Login Authentication</h3>
     The method {@code authenticationUserLogin()} accepts an email address and password, authenticates the user
     by checking if the email exists, and verifies whether the provided password matches the one stored
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

    //#region observer

    @Override
    public void update(EventModel event) {

        if (event instanceof PrivateEvent) {
            PrivateEvent privateEvent = (PrivateEvent) event;
            // TODO: Methode zur Benachrichtigung des Users aufrufen
        } else if (event instanceof PublicEvent) {
            PublicEvent publicEvent = (PublicEvent) event;
            // TODO: Methode zur Benachrichtigung des Users aufrufen
        }
    }

    //#endregion observer


}
