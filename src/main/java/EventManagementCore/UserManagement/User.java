package EventManagementCore.UserManagement;

import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.PermissionRoleManagement.PermissionManager;
import Helper.IDGenerationHelper;
import Helper.PermissionUserAssignmentHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User extends UserModel{

    private UserManager userManager = new UserManager();
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
     * <p>
     * {@code createNewUser()} accepts user parameters as arguments to initialize a new User object and load it into the database.
     * The method first checks the user's permissions from the database to verify if the user has the required {@link Permission} to create a new user.
     * If the permission check fails, an error message is logged, and the method returns {@code false}.
     * If the permission check passes, the method executes the following:
     * </p>
     * <blockquote><pre>
     * userManager.createNewUser(new User(firstName, lastName, dateOfBirth, emailAddress, password, phoneNumber, isAdmin));
     * </pre></blockquote>
     * At this point, the new user is initialized and loaded into the database.
     *
     * @see UserManager UserManager
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */


    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        getUsersPermissionsFromDatabase();

        if (!this.getPermissions().contains(permissionManager.getCreateUserPermission().getPermissionID())){
            logger.error(NO_PERMISSION_CREATE_USER);

            return false;
        }

        userManager.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));


        return true;
    }

    /**
     * <h3>Edit User</h3>
     * <p>
     * {@code editUser()} accepts the userID of the user you want to edit and the parameters you want to modify.
     * The method first checks the user's permissions from the database to verify if the user has the required {@link Permission} to edit a user.
     * If the permission check fails, an error message is logged, and the method returns {@code false}.
     * If the permission check passes, the method executes the following:
     * </p>
     * <blockquote><pre>
     *     userManager.updateUser(userToEdit);
     * </pre></blockquote>
     * At this point, the user is modified and updated in the database.
     * @see UserManager UserManager
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */


    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getEditUserPermission().getPermissionID())
        ){
            logger.error(NO_PERMISSION_EDIT_USER);

            return;
        }

        User userToEdit = userManager.readUserByID(userID);

        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userToEdit.setDateOfBirth(dateOfBirth);
        userToEdit.seteMailAddress(eMailAddress);
        userToEdit.setPassword(password);
        userToEdit.setPhoneNumber(phoneNumber);

        userManager.updateUser(userToEdit);

        logger.info("User after Editing: " + userToEdit);


    }

    /**
     * <h3>Delete User</h3>
     * <p>
     * {@code deleteUser()} accepts the userID of the user you want to delete.
     * The method first checks the user's permissions from the database to verify if the user has the required {@link Permission} to delete a user.
     * If the permission check fails, an error message is logged, and the method returns {@code false}.
     * If the permission check passes, the method executes the following:
     * </p>
     * <blockquote><pre>
     *     userManager.deleteUserByID(userID);
     * </pre></blockquote>
     * At this point, the user is deleted from the database.
     * @see UserManager UserManager
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */


    @Override
    public boolean deleteUser(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getDeleteUserPermission().getPermissionID())){
            logger.error(NO_PERMISSION_DELETE_USER);

            return false;
        }

        return userManager.deleteUserByID(userID);
    }

    /**
     * <h3>Get User By ID</h3>
     * <p>
     * {@code getUserByID()} accepts the userID of the user you want to retrieve.
     * The method first checks the user's permissions from the database to verify if the user has the required {@link Permission} to access user information.
     * If the permission check fails, an error message is logged, and the method returns {@code null}.
     * If the permission check passes, the method executes the following:
     * </p>
     * @see UserManager UserManager
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */


    @Override
    public User getUserByID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            logger.error(NO_PERMISSION_GET_USER_INFORMATION);

            return null;
        }

        return userManager.readUserByID(userID);
    }

    /**
     * <h3>Get User By eMail</h3>
     * <p>
     * {@code getUserByEmail} accepts the eMail-Address of the user you want to retrieve.
     * The method first checks the user's permissions from the database to verify if the user has the required {@link Permission} to access user information.
     * If the permission check fails, an error message is logged, and the method returns {@code null}.
     * If the permission check passes, the method executes the following:
     * </p>
     * @see UserManager UserManager
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    @Override
    public User getUserByEmail(String eMailAddress) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            logger.error(NO_PERMISSION_GET_USER_INFORMATION);

            return null;
        }

        return userManager.readUserByEMail(eMailAddress);
    }

    //#endregion CRUD-Operations

    //#region Permission-Operations

    /**
     * <h3>Add Permission to Own User</h3>
     * <p>
     * {@code addPermissionToOwnUser()} accepts a {@link Permission} object and adds the associated permission ID
     * to the list of permissions of the current user.
     * </p>
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    @Override
    public void addPermissionToOwnUser(Permission permission) {
        this.permissions.add(permission.getPermissionID());
    }

    /**
     * <h3>Add Permission to Another User</h3>
     * <p>
     * {@code addPermissionToAnotherUser()} accepts a {@link User} object and a {@link Permission} object,
     * and adds the associated permission ID to the list of permissions of the user.
     * </p>
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    public void addPermissionToAnotherUser(User user, Permission permission) {
        user.addPermissionToOwnUser(permission);
    }

    /**
     * <h3>Add Admin Status to User by User ID</h3>
     * <p>
     * {@code addAdminStatusToUserByUserID()} accepts a userID, retrieves the permissions of the current user,
     * and checks if they have the required permission to assign admin status to another user.
     * If the permission check passes, the admin status is added to the target user.
     * If the permission check fails, an error message is logged, and the method returns without making any changes.
     * </p>
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    public void addAdminStatusToUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGiveAdminStatusPermission().getPermissionID())) {
            logger.error(NO_PERMISSION_GIVE_ADMIN_STATUS);

            return;
        }

        this.addAdminStatusToUser(userManager.readUserByID(userID));
    }

    /**
     * <h3>Remove Admin Status from User by User ID</h3>
     * <p>
     * {@code removeAdminStatusFromUserByUserID()} accepts a userID, retrieves the permissions of the current user,
     * and checks if they have the required permission to remove admin status from another user.
     * If the permission check passes, the admin status is removed from the target user.
     * If the permission check fails, an error message is logged, and the method returns without making any changes.
     * </p>
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    public void removeAdminStatusFromUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getRemoveAdminStatusPermission().getPermissionID())) {
            logger.error(NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(userManager.readUserByID(userID));
    }

    /**
     * <h3>Get User's Permissions from Database</h3>
     * <p>
     * {@code getUsersPermissionsFromDatabase()} retrieves the user's permissions from the database and adds each
     * permission's ID to the current user's permissions list.
     * </p>
     * @see EventManagementCore.PermissionRoleManagement PermissionRoleManagement
     */

    public void getUsersPermissionsFromDatabase() {
        User user = userManager.readUserByID(this.userID);

        for (Permission permission : PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(user)) {
            this.permissions.add(permission.getPermissionID());
        }
    }


    //#endregion Permission-Operations

    //#region Registration & Authentication

    /**
     * <h3>Validate Registration Password</h3>
     * <p>
     * {@code isValidRegistrationPassword()} accepts a password and a password confirmation, ensuring both meet the validation criteria.
     * The method first checks if the password is valid by calling {@link #isValidPassword(String)}, and then compares it with the confirmation password using {@link #comparingPassword(String, String)}.
     * If both checks pass, the method returns {@code true}, indicating the password is valid. Otherwise, it returns {@code false}.
     * </p>
     */

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

    /**
     * <h3>Compare Passwords</h3>
     * <p>
     * {@code comparingPassword()} compares two passwords to ensure they match.
     * If either password is empty, an error message is printed, and the method returns {@code false}.
     * If the passwords match, the method returns {@code true}.
     * </p>
     */

    private boolean comparingPassword(String password, String checkPassword) {

        if (password.isEmpty() || checkPassword.isEmpty()) {

            System.out.println("Wrong password!");

            return false;
        }

        return checkPassword.equals(password);
    }

    /**
     * <h3>Compare Email Address</h3>
     * <p>
     * {@code comparingEmailAddress()} checks if the provided email address exists in the system by calling {@link #getUserByEmail(String)}.
     * If the email address is not found, an error message is printed, and the method returns {@code false}. If the email is found, it returns {@code true}.
     * </p>
     */

    private boolean comparingEmailAddress(String emailAddress) {

        if (getUserByEmail(emailAddress) == null) {

            System.out.println("Email address not found");

            return false;
        }

        return true;
    }

    /**
     * <h3>User Login Authentication</h3>
     * <p>
     * {@code authenticationUserLogin()} accepts an email address and password, and authenticates the user by checking if the email exists and if the provided password matches the one stored in the system.
     * The method first checks if the email address is valid using {@link #comparingEmailAddress(String)}.
     * If the email exists, it compares the provided password with the stored password using {@link #comparingPassword(String, String)}.
     * If both checks pass, the method returns {@code true}, indicating successful authentication. Otherwise, it returns {@code false}.
     * </p>
     */

    public boolean authenticationUserLogin(String email, String password) {

        if (comparingEmailAddress(email)) {

            return comparingPassword(password, getUserByEmail(email).getPassword());
        }

        return false;
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
