package de.eventmanager.core.users;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.Management.EventManager;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.permissions.Management.PermissionManager;
import helper.DatabaseSimulation.JsonDatabaseHelper;
import helper.IDGenerationHelper;
import helper.LoggerHelper;
import helper.PermissionUserAssignmentHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class User extends UserModel{

    private PermissionManager permissionManager = new PermissionManager();

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

        this.userID = IDGenerationHelper.generateRandomIDString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

    //Standard-User
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber) {

        this.userID = IDGenerationHelper.generateRandomIDString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
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
        this.isAdmin = isAdmin;
    }
    //#endregion constructor

    //#region CRUD-Operations
    /**
     * <h3>Create new User</h3>
     * {@code createNewUser()} accepts user parameters as arguments to initialize a new User object
     * and load it into the database with {@code UserManager.createNewUser()}.
     * @see UserManager UserManager
     * @see de.eventmanager.core.permissions PermissionRoleManagement
     */

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                 String password, int phoneNumber, boolean isAdmin) {
        getUsersPermissionsFromDatabase();

        if (!this.getPermissions().contains(permissionManager.getCreateUserPermission().getPermissionID())){
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
     * @see de.eventmanager.core.permissions PermissionRoleManagement
     */

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress,
                         String password, int phoneNumber) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getEditUserPermission().getPermissionID())){
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
     * @see de.eventmanager.core.permissions PermissionRoleManagement
     */


    @Override
    public boolean deleteUser(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getDeleteUserPermission().getPermissionID())){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_DELETE_USER);

            return false;
        }

        return UserManager.deleteUserByID(userID);
    }

    @Override
    public Optional<User> getUserByID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return Optional.empty();
        }

        return UserManager.readUserByID(userID);
    }

    @Override
    public Optional<User> getUserByEmail(String eMailAddress) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return Optional.empty();
        }

        return UserManager.readUserByEMail(eMailAddress);
    }

    //#endregion CRUD-Operations

    //#region Event related Operations

    @Override
    public Optional<EventModel> createEvent(boolean isPrivateEvent , String eventName, String eventDateTime, String category) {
        EventModel event;
        if (isPrivateEvent) {
            event = new PrivateEvent(eventName, eventDateTime, category);
        } else {
            event = new PublicEvent(eventName, eventDateTime, category);
        }
        /*EventManager.createNewEvent(event);
        EventManager.addUserCreatedEvent(event.getEventID(), this.userID);*/
        JsonDatabaseHelper.createNewEvent(event);
        JsonDatabaseHelper.addUserCreatedEvent(event.getEventID(), this.userID);

        return Optional.of(event);
    }

    //#endregion Event related Operations

    //#region Permission-Operations

    @Override
    public void addPermissionToOwnUser(Permission permission) {
        this.permissions.add(permission.getPermissionID());
    }

    public void addPermissionToAnotherUser(User user, Permission permission) {
        user.addPermissionToOwnUser(permission);
    }

    public void addAdminStatusToUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGiveAdminStatusPermission().getPermissionID())) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GIVE_ADMIN_STATUS);

            return;
        }

        this.addAdminStatusToUser(UserManager.readUserByID(userID).get());
    }

    public void removeAdminStatusFromUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getRemoveAdminStatusPermission().getPermissionID())) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(UserManager.readUserByID(userID).get());
    }

    public void getUsersPermissionsFromDatabase() {
        User user = UserManager.readUserByID(this.userID).get();
        Optional<List<Permission>> optionalPermissionList = PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(user);

        if (!optionalPermissionList.isPresent()) {
            LoggerHelper.logErrorMessage(User.class, "No permissions found for user " + user.getFirstName());

            return;
        }
        List<Permission> permissions = optionalPermissionList.get();

        for (Permission permission : permissions) {
            this.permissions.add(permission.getPermissionID());
        }
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
                "\nisAdmin: " + isAdmin + "\n";
    }

    //#endregion toString()

}
