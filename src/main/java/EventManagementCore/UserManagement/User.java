package EventManagementCore.UserManagement;

public class User extends UserModel{
    public User(String userID, String name, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        this.userID = userID;
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }
}
