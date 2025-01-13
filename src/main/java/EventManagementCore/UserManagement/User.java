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

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        getUsersPermissionsFromDatabase();

        if (!this.getPermissions().contains(permissionManager.getCreateUserPermission().getPermissionID())){
            logger.error(NO_PERMISSION_CREATE_USER);

            return false;
        }

        userManager.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        //System.out.println(this.getUserByEmail(eMailAddress).toString());

        return true;
    }

    //Todo @Finn @Timo Herausfinden warum editUser, editierte User nicht in der DB verändert

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getEditUserPermission().getPermissionID()) ||
                !this.userID.equals(userID)
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

        System.out.println("User after Editing: " + this.getUserByEmail(eMailAddress).toString()); //Steht ihr nur solange bis userEdit funktioniert

        //logger.info(this.getUserByEmail(eMailAddress).toString());
    }

    @Override
    public boolean deleteUser(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getDeleteUserPermission().getPermissionID())){
            logger.error(NO_PERMISSION_DELETE_USER);

            return false;
        }

        return userManager.deleteUserByID(userID);
    }

    @Override
    public User getUserByID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            logger.error(NO_PERMISSION_GET_USER_INFORMATION);

            return null;
        }

        return userManager.readUserByID(userID);
    }

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
            logger.error(NO_PERMISSION_GIVE_ADMIN_STATUS);

            return;
        }

        this.addAdminStatusToUser(userManager.readUserByID(userID));
    }

    public void removeAdminStatusFromUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getRemoveAdminStatusPermission().getPermissionID())) {
            logger.error(NO_PERMISSION_REMOVE_ADMIN_STATUS);

            return;
        }

        this.removeAdminStatusFromUser(userManager.readUserByID(userID));
    }

    public void getUsersPermissionsFromDatabase() {
        User user = userManager.readUserByID(this.userID);

        for (Permission permission : PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(user)) {
            this.permissions.add(permission.getPermissionID());
        }
    }


    //#endregion Permission-Operations

    //#region Registration & Authentication
    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

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

            System.out.println("Email address not found");

            return false;
        }

        return true;
    }

    public boolean authenticationUserLogin(String email, String password) {

        if (comparingEmailAddress(email)) {

            return comparingPassword(password, getUserByEmail(email).getPassword());
        }

        return false;
    }
    //#endregion Registration & Authentication

    @Override
    public String toString() {
        return "User: \nfirstName: " + firstName + "\nlastName: " + lastName + "\ndateOfBirth: " + dateOfBirth + "\neMailAddress: " + eMailAddress + "\npassword: "
                + password + "\nphoneNumber: " + phoneNumber + "\nisAdmin: " + isAdmin + "\n";
    }

}
