package EventManagementCore.PermissionRoleManagement;

public class PermissionManager {
    private class DummyUser {
        private String userID;
        private String name;
        private String lastName;
        private boolean isAdmin;

        DummyUser(String userID, boolean isAdmin) {
            this.userID = userID;
            this.name = "Dummy";
            this.lastName = "DummyDummy";
            this.isAdmin = isAdmin;
        }

        public boolean getIsAdmin() {
            return isAdmin;
        }
    }

    private DummyUser notAnAdminDummyUser;

    private DummyUser anAdminDummyUser;

    public PermissionManager(String userID) {
        this.notAnAdminDummyUser = new DummyUser(userID, false);
        this.anAdminDummyUser = new DummyUser(userID, true);
    }

    public boolean hasPermissionDeleteUser(DummyUser dummyUser) {
        return dummyUser.getIsAdmin();
    }

    public boolean hasPermissionAddUserFor(DummyUser dummyUser) {
        return dummyUser.getIsAdmin();
    }

    public boolean hasPermissionEditUser(DummyUser dummyUser) {
        return dummyUser.getIsAdmin();
    }

    public DummyUser getAnAdminDummyUser() {
        return this.anAdminDummyUser;
    }

    public DummyUser getNotAnAdminDummyUser() {
        return this.notAnAdminDummyUser;
    }
}
