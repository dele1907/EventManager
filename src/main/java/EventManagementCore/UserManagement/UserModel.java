package EventManagementCore.UserManagement;

import EventManagementCore.DatabaseCommunication.UserManager;
import EventManagementCore.PermissionRoleManagement.Permission;

import java.util.ArrayList;

public abstract class UserModel {
    String userID;
    String firstName;
    String lastName;
    String dateOfBirth;
    String eMailAddress;
    String password;
    int phoneNumber;
    boolean isAdmin = false;
    ArrayList<Permission> permissions;
    protected UserManager userManager;

    UserModel() {
        permissions = new ArrayList<>();
    }

    public abstract void createNewUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String eMailAddress,
            String password,
            int phoneNumber,
            boolean isAdmin
    );

    public boolean deleteUser(String userID) {
        //TODO check user's permission to delete an user
        return userManager.deleteUserByID(userID);
    }

    public boolean deleteEvent(String eventID) {
        return false;
    }

    public String[] showEventIDsByUserID(String userID) {
        return null;
    }

    public String[] showUserIDsByEventID(String eventID) {
        return null;
    }

    public User showUserByID (String userID){
        //User user = userManager.showUserByID();
        return null;
    }

    public void removeUserFromEvent(String userID, String eventID) {}

    public void addUserToEvent(String userID, String eventID) {}

    public void addAdminStatusToUser(){
        this.isAdmin = true;
    }

    public void removeAdminStatusFromUser(){
        this.isAdmin = false;
    }

    public void editUser(String userID) {}


    public void bookEvent(String eventID) {}

    public void editEvent(String eventID) {}

    public abstract void addPermissionToOwnUser(Permission permission);


    //Getter
    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserID() {
        return userID;
    }


}
