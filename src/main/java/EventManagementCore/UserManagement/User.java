package EventManagementCore.UserManagement;

import Helper.IDGenerationHelper;

public class User extends UserModel{

    public User(String name, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        this.userID = IDGenerationHelper.generateRandomIDString();
        this.firstName = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;

        this.isAdmin = false;
    }

    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    //After testing and review changing the methods to private the test are deleted because they can not access the methods anymore
    private boolean isValidPassword(String password) {
        char[] restrictedCharacters = {' ', '$', '@', '§', '&', '%', 'ä', 'ö', 'ü', 'ß', 'Ä', 'Ü', 'Ö'};

        for (var restricted : restrictedCharacters) {
            if (password.indexOf(restricted) != -1) {
                return false;
            }
        }

        return true;
    }

    //After testing and review changing the methods to private the test are deleted because they can not access the methods anymore
    private boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {
            return false;
        }

        return checkPassword.equals(password);
    }
}
