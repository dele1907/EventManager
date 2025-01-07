package EventManagementCore.UserManagement;

public interface User {

    String getID();

    String getName();

    String getLastName();

    String getDateOfBirth();

    String getEMailAddress();

    String getPassword();

    int getPhoneNumber();

    boolean isAdmin();

    void createEvent();
}
