package EventManagementCore.UserManagement;

import Helper.IDGenerationHelper;

public class User extends UserModel{
    public User(String name, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        this.userID = IDGenerationHelper.generateRandomString(IDGenerationHelper.ID_DEFAULT_LENGHT);
        this.name = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }
    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    public boolean isValidPassword(String password) {
        String[] characters = {" ", "$", "@", "§", "&", "%", "ä", "ö", "ü", "ß", "Ä", "Ü", "Ö"};
        for(var character : characters) {
            if (!password.contains(character)) {
                return true;
            }
        }
        System.out.println("Password has a Invalid Character!");
        System.out.println("Password should not contains one of these Characters, \n" +
                "$, §, @, &, %, ß, Ä, Ü, Ö, ä, ü, ö and no Whitespace!");
        return false;
    }
    public boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {
            return false;
        }
        return checkPassword.equals(password);
    }
}
