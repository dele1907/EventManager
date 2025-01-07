package EventManagementCore.UserManagement;

public class AdminUser extends BookAndCreateUser implements User {


    public AdminUser(String userID, String name, String lastName, String dateOfBirth,
                     String eMailAddress, String password, int phoneNumber,
                     boolean isAdmin, String[] createdEventsIDs) {
        super(userID, name, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin,createdEventsIDs);

    }

    @Override
    public String getID() {
        return userID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getEMailAddress() {
        return eMailAddress;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public void createEvent() {

    }

    public boolean deleteUser(String userID) {
        return false;
    }

    /*public BookAndCreateUser createNewUser(String userType){
        if (userType.equals("Admin")){
            return new AdminUser();
        }
        if (userType.equals("User")){
            return new BookAndCreateUser();
        }
        if (userType.equals("CreateOnlyUser")){
            return new CreateOnlyUser();
        }
    }*/
}
