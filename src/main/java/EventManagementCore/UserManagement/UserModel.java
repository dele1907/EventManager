package EventManagementCore.UserManagement;

import EventManagementCore.PermissionRoleManagement.Permission;

import java.util.ArrayList;

public abstract class UserModel {
    String userID;
    String name;
    String lastName;
    String dateOfBirth;
    String eMailAddress;
    String password;
    int phoneNumber;
    boolean isAdmin;
    ArrayList<Permission> permissions;

    UserModel() {
        permissions = new ArrayList<>();
    }

    public void createUser() {}

    public boolean deleteUser(String userID) {
        return false;
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
        return null;
    }

    public void removeUserFromEvent(String userID, String eventID) {}

    public void addUserToEvent(String userID, String eventID) {}

    public void addAdminStatusToUser(String userID){}

    public void removeAdminStatusFromUser(String userID){}

    public User createNewUser(String type) {
        return null;
    }

    public void editUser(String userID) {}

    public void bookEvent(String eventID) {}

    public void editEvent(String eventID) {}

    public void addPermissionToUser(Permission permission) {
        permissions.add(permission);
    }


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

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }


}
