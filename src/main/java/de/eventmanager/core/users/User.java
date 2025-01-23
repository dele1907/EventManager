package de.eventmanager.core.users;

import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.database.Communication.EventDataBaseConnector;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import helper.IDGenerationHelper;
import helper.LoggerHelper;
import helper.PasswordHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class User extends UserModel{



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
     * Custom {@link #toString()} toString()} method  for a more readable output
     */

    @Override
    public String toString() {
        return "User: \nfirstName: " + firstName + "\nlastName: " + lastName + "\ndateOfBirth: " + dateOfBirth +
                "\neMailAddress: " + eMailAddress + "\npassword: " + password + "\nphoneNumber: " + phoneNumber +
                "\nisAdmin: " + (this.role.equals(Role.ADMIN) ? true : false) + "\n";
    }

    //#endregion toString()

}
