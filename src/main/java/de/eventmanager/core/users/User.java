package de.eventmanager.core.users;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;
import helper.PasswordHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.Log;

import java.util.ArrayList;
import java.util.Optional;

public class User extends UserModel{

    Logger logger = LogManager.getLogger(User.class);

    //#region Constant variables

    private final String NO_PERMISSION_CREATE_USER = "User has no permission to create a new user";
    private final String NO_PERMISSION_EDIT_USER = "User has no permission to edit a user";
    private final String NO_PERMISSION_DELETE_USER = "User has no permission to delete a user";
    private final String NO_PERMISSION_GET_USER_INFORMATION = "User has no permission to get user information";
    private final String NO_PERMISSION_GIVE_ADMIN_STATUS = "User has no permission to give admin status";
    private final String NO_PERMISSION_REMOVE_ADMIN_STATUS = "User has no permission to remove admin status";

    //#endregion Constant variables

    //#region constructor

    //Administrator
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber, boolean isAdmin) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = PasswordHelper.hashPassword(password);
        this.phoneNumber = phoneNumber;
        this.role = isAdmin ? Role.ADMIN : Role.USER;
    }

    /**
     * TODO: maybe can remove so we use constructor provided above.
     * */
    //Standard-User
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = PasswordHelper.hashPassword(password);
        this.phoneNumber = phoneNumber;
        this.role = Role.USER;
    }

    //User-Object for load DB
    public User(String userID, String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber, boolean isAdmin) {

        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = isAdmin ? Role.ADMIN : Role.USER;
    }
    //#endregion constructor

    //#region User related CRUD-Operations
    /**
     * <h3>Create new User</h3>
     * {@code createNewUser()} accepts user parameters as arguments to initialize a new User object
     * and load it into the database with {@code UserManager.createNewUser()}.
     * @see UserManager UserManager
     */

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                 String password, String phoneNumber, boolean isAdmin) {
        if (!this.role.equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_CREATE_USER);

            return false;
        }

        UserManager.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        return true;
    }

    /**
     * <h3>Edit User</h3>
     * {@code editUser()} accepts the userID of the user you want to edit and the parameters you want to modify.
     * @see UserManager UserManager
     */

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress,
             String password, String phoneNumber) {
        if (!this.role.equals(Role.ADMIN)){
            logger.error(NO_PERMISSION_EDIT_USER);

            return;
        }

        Optional<User> user = UserManager.readUserByID(userID);

        if (user.isPresent()){
            User userToEdit = user.get();
            userToEdit.setFirstName(firstName);
            userToEdit.setLastName(lastName);
            userToEdit.setDateOfBirth(dateOfBirth);
            userToEdit.seteMailAddress(eMailAddress);
            userToEdit.setPassword(password);
            userToEdit.setPhoneNumber(phoneNumber);

            UserManager.updateUser(userToEdit);

            LoggerHelper.logInfoMessage(User.class, "User after Editing: " + userToEdit);
        }
    }

    /**
     * <h3>Delete User</h3>
     * {@code deleteUser()} accepts the userID of the user you want to delete.
     * @see UserManager UserManager
     */


    @Override
    public boolean deleteUser(String userID) {
        if (!this.role.equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_DELETE_USER);

            return false;
        }

        return UserManager.deleteUserByID(userID);
    }

    @Override
    public Optional<User> getUserByID(String userID) {
        if (!this.role.equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return Optional.empty();
        }

        return UserManager.readUserByID(userID);
    }

    @Override
    public Optional<User> getUserByEmail(String eMailAddress) {
        if (!this.role.equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return Optional.empty();
        }

        return UserManager.readUserByEMail(eMailAddress);
    }

    //#endregion User related CRUD-Operations

    //#region Event related CRUD-Operations

    @Override
    public Optional<PrivateEvent> createPrivateEvent(String eventName, String eventStart, String eventEnd, String category,
                                                   String postalCode, String address, String eventLocation, String description) {

        PrivateEvent event = new PrivateEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description);

        EventManager.createNewEvent(event);

        EventManager.addUserCreatedEvent(event.getEventID(), this.userID);

        return Optional.of(event);
    }

    @Override
    public Optional<PublicEvent> createPublicEvent(String eventName, String eventStart, String eventEnd, String category,
                                                  String postalCode, String address, String eventLocation, String description, int maxParticipants) {

        if (maxParticipants == 0) {
            maxParticipants = -1;
        }

        PublicEvent event = new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, address, eventLocation, description, maxParticipants);
        EventManager.createNewEvent(event);
        EventManager.addUserCreatedEvent(event.getEventID(), this.userID);

        return Optional.of(event);
    }

    @Override
    public boolean editEvent(String eventID, String eventName,
         String eventStart, String eventEnd, String category,
         String postalCode, String address, String eventLocation, String description
    ) {

        Optional<PrivateEvent> privateEvent = EventManager.readPrivateEventByID(eventID);

        if (privateEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, "Event not found");

            return false;
        }

        PrivateEvent privateEventToEdit = privateEvent.get();
        privateEventToEdit.setEventName(eventName);
        privateEventToEdit.setEventStart(eventStart);
        privateEventToEdit.setEventEnd(eventEnd);
        privateEventToEdit.setCategory(category);
        privateEventToEdit.setPostalCode(postalCode);
        privateEventToEdit.setAddress(address);
        privateEventToEdit.setEventLocation(eventLocation);
        privateEventToEdit.setDescription(description);

        EventManager.updateEvent(privateEventToEdit);

        LoggerHelper.logInfoMessage(User.class, "Event after editing: " + privateEventToEdit);

        return true;
    }




    @Override
    public boolean deleteEvent(String eventID) {
       return EventManager.deleteEventByID(eventID);
    }

    @Override
    public boolean bookEvent(String eventID) {
        Optional<PublicEvent> publicEvent = EventManager.readPublicEventByID(eventID);

        if (!isEventExisting(publicEvent)) {

            return false;
        }

        if (publicEvent.get().isPrivateEvent()){
            return false;
        }

        /*
        PublicEvent publicEventForBooking = publicEvent.get();
        publicEventForBooking.getBookedUsersOnEvent().add(this.getEMailAddress());
         */

        EventManager.addBooking(eventID,this.userID);
        LoggerHelper.logInfoMessage(User.class, "Event booked successfully!");

        return true;
    }

    @Override
    public boolean cancelEvent(String eventID) {
        Optional<? extends EventModel> OptionalEvent = EventManager.readEventByID(eventID);

        if (!isEventExisting(OptionalEvent)) {

            return false;
        }

        EventModel eventForCancel = OptionalEvent.get();
        /*
        if (!eventForCancel.getBookedUsersOnEvent().contains(this.getEMailAddress())) {
            LoggerHelper.logInfoMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }
        */
        //eventForCancel.getBookedUsersOnEvent().remove(this.getEMailAddress());
        EventManager.deleteBooking(eventID,this.getUserID());
        LoggerHelper.logInfoMessage(User.class, "Event cancelled successfully!");

        return true;
    }

    public String showAllEventParticipants(String eventID) {
        Optional<?extends EventModel> optionalEvent = EventManager.readEventByID(eventID);
        ArrayList<String> participants = optionalEvent.get().getBookedUsersOnEvent();
        if (!isEventExisting(optionalEvent)) {

            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Lastname, firstname\t eMailaddress\t phoneNumber\n\n");
        for (int i = 0; i < participants.size(); i++) {
            User user = getUserByEmail(participants.get(i)).get();
            sb.append(user.getLastName() + ", " + user.getFirstName() + "\t" + user.getEMailAddress() + "\t" + user.getPhoneNumber() + "\n");
        }

        return sb.toString();
    }

    public boolean isEventExisting(Optional<? extends EventModel> event) {
        if (event.isEmpty()) {
            LoggerHelper.logErrorMessage(User.class, "Event not found");

            return false;
        }

        return true;
    }

    //#endregion Event related CRUD-Operations

    //#region Permission-Operations

    public void addAdminStatusToUser(User user){
        user.setRoleAdmin(true);
    }

    public void removeAdminStatusFromUser(User user) {
        user.setRoleAdmin(false);
    }

    public void addAdminStatusToUserByUserID(String userID) {
        if (!this.role.equals(Role.ADMIN)) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GIVE_ADMIN_STATUS);

            return;
        }

        this.addAdminStatusToUser(UserManager.readUserByID(userID).get());
    }

    public void removeAdminStatusFromUserByUserID(String userID) {

        if (!this.role.equals(Role.ADMIN)) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(UserManager.readUserByID(userID).get());
    }
    //#endregion Permission-Operations

    //#region toString()

    /**
     * <h3>Own toString()-Method</h3>
     * <p>
     * Custom {@link #toString()} toString()} method  for a more readable output
     */

    @Override
    public String toString() {
        return "User: \nfirstName: " + firstName + "\nlastName: " + lastName + "\ndateOfBirth: " + dateOfBirth +
                "\neMailAddress: " + eMailAddress + "\npassword: " + password + "\nphoneNumber: " + phoneNumber +
                "\nisAdmin: " + (this.role.equals(Role.ADMIN) ? true : false) + "\n";
    }

    //#endregion toString()

}
