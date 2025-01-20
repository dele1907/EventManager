package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;
import helper.DatabaseSimulation.JsonDatabaseHelper;
import helper.IDGenerationHelper;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public boolean registerUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String email,
            int phoneNumber,
            String password,
            String passwordConfirmation
            ) {

        if (!UserManager.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }
         /**
          * commented out because the test will run on the simulated json database
          * */
        //return UserManager.createNewUser(new User(firstName, lastName, dateOfBirth, email, password, phoneNumber));
        String userID = IDGenerationHelper.generateRandomUUID();

        return JsonDatabaseHelper.addUserToJson(userID, firstName, lastName, dateOfBirth, email, phoneNumber, password, false);
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        if (!UserManager.authenticationUserLogin(email, password)) {
            return Optional.empty();
        }

        //return UserManager.readUserByEMail(email);
        return JsonDatabaseHelper.getUserByEmailFromJson(email);
    }
}
