package EventManagementCore.UserManagement;

import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;
import Helper.IDGenerationHelper;
import Helper.PermissionUserAssignmentHelper;

public class User extends UserModel{
    private UserManager userManager = new UserManager();
    private final String MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE = "You don't have the permission to do this!";


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

    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber, boolean isAdmin) {

        if (this.isAdmin){

            // Checking if some user already exists (firstName and lastName, email) in UserManager von Laura

            userManager.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));
            return true;

        }else {

            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);
            return false;

        }

    }

    @Override
    public void editUser(String userID) {

        if (this.isAdmin || this.userID.equals(userID)){

            //edit-Function
        }else {

            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);
        }
    }

    @Override
    public boolean deleteUser(String userID) {

        if (this.isAdmin){

            return userManager.deleteUserByID(userID);
        }else {

            System.out.println(MISSING_PERMISSION_FOR_ACTION_ERROR_MESSAGE);

            return false;
        }

    }

    @Override
    public User showUserByID(String userID) {

        if (this.isAdmin){
            return null;
        }
        return null;
    }

    @Override
    public User getUserByEmail(String eMailAddress) {

        if (this.isAdmin){

            return userManager.readUserByEMail(eMailAddress);
        }

        return null;
    }

    @Override
    public void addPermissionToOwnUser(Permission permission) {
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(this, permission);
    }

    public void addPermissionToAnotherUser(User user, Permission permission) {
        PermissionUserAssignmentHelper.addPermissionToUsersPermissions(user, permission);
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

}
