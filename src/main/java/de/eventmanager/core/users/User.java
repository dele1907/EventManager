package de.eventmanager.core.users;


import de.eventmanager.core.roles.Role;
import helper.IDGenerationHelper;
import helper.PasswordHelper;

public class User extends UserModel {

    //#region constructor

    //Administrator
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber, boolean isAdmin) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = PasswordHelper.hashPassword(password);
        this.phoneNumber = phoneNumber;
        this.role = isAdmin ? Role.ADMIN : Role.USER;
    }

    /**
     * TODO: maybe can remove so we use constructor provided above.
     * */
    //Standard-User
    public User(String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber) {

        this.userID = IDGenerationHelper.generateRandomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = PasswordHelper.hashPassword(password);
        this.phoneNumber = phoneNumber;
        this.role = Role.USER;
    }

    //User-Object for load DB
    public User(String userID, String firstName, String lastName, String dateOfBirth,
                String eMailAddress, String password, String phoneNumber, boolean isAdmin) {

        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.eMailAddress = eMailAddress;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = isAdmin ? Role.ADMIN : Role.USER;
    }
    //#endregion constructor

    //#region toString()

    /**
     * <h3>Own toString()-Method</h3>
     * <p>
     * Custom {@link #toString()} method  for a more readable output
     */

    @Override
    public String toString() {
        return "\nFirst name: " + firstName + "\nLast name: " + lastName + "\nDate of birth: " + dateOfBirth +
                "\neMail address: " + eMailAddress + "\nPhone number: " + phoneNumber +
                (this.role.equals(Role.ADMIN) ? "\nIs an Admin user on the system" : "") + "\n";
    }

    //#endregion toString()

}
