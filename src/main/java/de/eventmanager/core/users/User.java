package de.eventmanager.core.users;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import helper.IDGenerationHelper;
import helper.LoggerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = isAdmin ? Role.ADMIN : Role.USER;
    }

    /**
     * TODO: maybe can remove so we use constructor provided above.
     * */
    //Standard-User
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = Role.USER;
    }

    //User-Object for load DB
    public User(String userID, String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {

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

    //#region CRUD-Operations
    /**
     * <h3>Create new User</h3>
     * {@code createNewUser()} accepts user parameters as arguments to initialize a new User object
     * and load it into the database with {@code UserManager.createNewUser()}.
     * @see UserManager UserManager
     */

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                 String password, int phoneNumber, boolean isAdmin) {
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
             String password, int phoneNumber) {
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

    //#endregion CRUD-Operations

    //#region Event related Operations

    @Override
    public Optional<EventModel> createPrivateEvent(String eventName, String eventStart, String eventEnd, String category) {
        EventModel event;

            event = new PrivateEvent(eventName, eventStart, eventEnd, category);
        EventManager.createNewEvent(event);
        EventManager.addUserCreatedEvent(event.getEventID(), this.userID);

        return Optional.of(event);
    }

    @Override
    public Optional<EventModel> createPublicEvent(String eventName, String eventStart, String eventEnd, String category, int maxParticipants) {
        EventModel event;

        if (maxParticipants == 0) {
            maxParticipants = -1;
        }

        event = new PublicEvent(eventName, eventStart, eventEnd, category, maxParticipants);
        EventManager.createNewEvent(event);
        EventManager.addUserCreatedEvent(event.getEventID(), this.userID);

        return Optional.of(event);
    }

    //#endregion Event related Operations

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
