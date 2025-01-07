package EventManagementCore.UserManagement;

public class BookAndCreateUser{
    String[] createdEventsIDs;
    String userID;
    String name;
    String lastName;
    String dateOfBirth;
    String eMailAddress;
    String password;
    int phoneNumber;
    boolean isAdmin;

    public BookAndCreateUser(String userID, String name, String lastName, String dateOfBirth,
                             String eMailAddress, String password, int phoneNumber,
                             boolean isAdmin, String[] createdEventsIDs) {
        this.userID = userID;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
        this.createdEventsIDs = createdEventsIDs;

    }
}
