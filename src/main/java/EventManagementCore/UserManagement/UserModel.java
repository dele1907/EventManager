package EventManagementCore.UserManagement;

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
    UserModel() {
        permissions = new ArrayList<>();
    }

    public abstract boolean createNewUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String eMailAddress,
            String password,
            int phoneNumber,
            boolean isAdmin
    );

    public void editUser(String userID, String firstName, String lastName, String dateOfBirth, String eMailAddress, String password, int phoneNumber) {
    }

    public abstract boolean deleteUser(String userID);

    public User getUserByID(String userID){
        return null;
    }

    public User getUserByEmail(String eMailAddress){
        return null;
    }

    public void addAdminStatusToUser(){
        this.isAdmin = true;
    }

    public void removeAdminStatusFromUser(){
        this.isAdmin = false;
    }

    public String[] showUserIDsByEventID(String eventID) {
        return null;
    }


    public void createEvent(String eventID) {}

    public void bookEvent(String eventID) {}

    public void editEvent(String eventID) {}

    public boolean deleteEvent(String eventID) {
        return false;
    }

    public void addUserToEvent(String userID, String eventID) {}

    public void removeUserFromEvent(String userID, String eventID) {}

    //public Event showEventByID(String userID){};

    public String[] showEventIDsByUserID(String userID) {
        return null;
    }

    public abstract void addPermissionToOwnUser(Permission permission);


    //#region Getter
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
    //#endregion Getter

    //#region Setter

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //#endregion Setter
}
