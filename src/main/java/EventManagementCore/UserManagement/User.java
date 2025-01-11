package EventManagementCore.UserManagement;

import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;
import EventManagementCore.PermissionRoleManagement.PermissionManager;
import Helper.IDGenerationHelper;
import Helper.PermissionUserAssignmentHelper;

public class User extends UserModel{

    private UserManager userManager = new UserManager();
    private PermissionManager permissionManager = new PermissionManager();
    private final String MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE = "You don't have the permission to do this!";

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

    //User
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

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        getUsersPermissionsFromDatabase();

        if (!this.getPermissions().contains(permissionManager.getCreateUserPermission().getPermissionID())){
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return false;
        }

        userManager.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        return true;
    }

    @Override
    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getEditUserPermission().getPermissionID()) ||
                !this.userID.equals(userID)
        ){
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

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
    }

    @Override
    public boolean deleteUser(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getDeleUserPermission().getPermissionID())){
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return false;
        }

        return userManager.deleteUserByID(userID);
    }

    @Override
    public User getUserByID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return null;
        }

        return userManager.readUserByID(userID);
    }

    @Override
    public User getUserByEmail(String eMailAddress) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getGetUserInformationPermission().getPermissionID())){
            return null;
        }

        return userManager.readUserByEMail(eMailAddress);
    }

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
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return;
        }

        userManager.readUserByID(userID).addAdminStatusToUser();
    }

    public void removeAdminStatusFromUserByUserID(String userID) {
        getUsersPermissionsFromDatabase();

        if (!this.permissions.contains(permissionManager.getRemoveAdminStatusPermission().getPermissionID())) {
            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return;
        }

        userManager.readUserByID(userID).removeAdminStatusFromUser();
    }

    //#region validatePassword
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
    //#endregion validatePassword

    private boolean comparingEmailAddress(String emailAddress) {

        if (getUserByEmail(emailAddress) == null) {

            System.out.println("Email address not found");

            return false;
        }

        return true;
    }

    public boolean authentificateUserLogin(String email, String password) {

        if (comparingEmailAddress(email)) {

            if (comparingPassword(password, getUserByEmail(email).getPassword())) {

                return true;
            }
        }

        return false;
    }

    public void getUsersPermissionsFromDatabase() {
        User user = userManager.readUserByID(this.userID);

        for (Permission permission : PermissionUserAssignmentHelper.getPermissionsForUserFromDatabase(user)) {
            this.permissions.add(permission.getPermissionID());
        }
    }

}
