package de.eventmanager.core.users;

import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.permissions.Permission;
import de.eventmanager.core.permissions.Management.PermissionManager;
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

        User userToEdit = UserManager.readUserByID(userID);

        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userToEdit.setDateOfBirth(dateOfBirth);
        userToEdit.seteMailAddress(eMailAddress);
        userToEdit.setPassword(password);
        userToEdit.setPhoneNumber(phoneNumber);

        UserManager.updateUser(userToEdit);

        LoggerHelper.logInfoMessage(User.class, "User after Editing: " + userToEdit);
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
    public User getUserByID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return null;
        }

        return UserManager.readUserByID(userID);
    }

    @Override
    public User getUserByEmail(String eMailAddress) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_GET_USER_INFORMATION);

            return null;
        }

        return UserManager.readUserByEMail(eMailAddress);
    }

    //#endregion CRUD-Operations

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

        this.addAdminStatusToUser(UserManager.readUserByID(userID));
    }

    public void removeAdminStatusFromUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getRemoveAdminStatusPermission().getPermissionID())) {
            LoggerHelper.logErrorMessage(User.class, NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(UserManager.readUserByID(userID));
    }

    public void getUsersPermissionsFromDatabase() {
        User user = UserManager.readUserByID(this.userID);
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

    //#region Registration & Authentication

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

    private boolean comparingEmailAddress(String emailAddress) {

        if (getUserByEmail(emailAddress) == null) {

            LoggerHelper.logInfoMessage(User.class, "Email address not found");
            return false;
        }

        return true;
    }

    /**
     * <h3>User Login Authentication</h3>
     The method {@code authenticationUserLogin()} accepts an email address and password, authenticates the user
     by checking if the email exists, and verifies whether the provided password matches the one stored
     in the DB. If both conditions are met, the user is successfully logged in.
     */

    public boolean authenticationUserLogin(String email, String password) {

        if (!comparingEmailAddress(email)) {

            return false;
        }

        return comparingPassword(password, getUserByEmail(email).getPassword());
    }
    //#endregion Registration & Authentication

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
