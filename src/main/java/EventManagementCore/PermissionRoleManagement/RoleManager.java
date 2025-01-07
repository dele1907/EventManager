package EventManagementCore.PermissionRoleManagement;

public class RoleManager {
    private class User {
        String userID;
        String name;
        String lastName;
        boolean isAdmin;

        User(String userID) {
            this.userID = userID;
            this.name = "Dummy";
            this.lastName = "DummyDummy";
            this.isAdmin = false;
        }
    }

    public User notAnAdminUser;

    public RoleManager(String userID) {
        this.notAnAdminUser = new User("55er678");
    }

    public boolean hasPermissionDeleteUser(User user) {
        return user.isAdmin;
    }
}
