package EventManagementCore.UserManagement;

import Helper.IDGenerationHelper;

public class User extends UserModel{
    //TODO @Timo (review): here could be whitespace
    public User(String name, String lastName, String dateOfBirth,
                String eMailAddress, String password, int phoneNumber, boolean isAdmin) {
        this.userID = IDGenerationHelper.generateRandomIDString(IDGenerationHelper.ID_DEFAULT_LENGHT);
        this.firstName = name;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        //TODO @whoEverItMayConcern: isAdmin should be *false* by default
        this.isAdmin = isAdmin;
    }
    //TODO @Timo (review): here could be whitespace
    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    //TODO @Timo (review): should be private
    public boolean isValidPassword(String password) {
        String[] characters = {" ", "$", "@", "§", "&", "%", "ä", "ö", "ü", "ß", "Ä", "Ü", "Ö"};
        //TODO @Timo (review): here could be whitespace
        for(var character : characters) {
            if (!password.contains(character)) {
                return true;
            }
        }
        //TODO @Timo (review): here could be whitespace
        //TODO @Timo (review): console logs in testDrive
        System.out.println("Password has a Invalid Character!");
        System.out.println("Password should not contains one of these Characters, \n" +
                "$, §, @, &, %, ß, Ä, Ü, Ö, ä, ü, ö and no Whitespace!");
        //TODO @Timo (review): here could be whitespace
        return false;
    }
    //TODO @Timo (review): here could be whitespace
    //TODO @Timo (review): should be private
    public boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {
            return false;
        }
        //TODO @Timo (review): here could be whitespace
        return checkPassword.equals(password);
    }
}
