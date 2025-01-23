package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public boolean registerUser(String firstName, String lastName, String dateOfBirth,
            String email, String phoneNumber, String password, String passwordConfirmation) {

        if (!UserDatabaseConnector.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }

        return UserDatabaseConnector.createNewUser(new User(firstName, lastName, dateOfBirth, email, password, phoneNumber));
    }

    @Override
    public boolean registerAdminUser(String firstName, String lastName, String dateOfBirth, String email, String phoneNumber, String password, String passwordConfirmation, boolean isAdmin) {

        if (!UserDatabaseConnector.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }

        return UserDatabaseConnector.createNewUser(new User(firstName, lastName, dateOfBirth, email, password, phoneNumber, isAdmin));
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        if (!UserDatabaseConnector.authenticationUserLogin(email, password)) {
            return Optional.empty();
        }

        return UserDatabaseConnector.readUserByEMail(email);
    }

    @Override
    public boolean deleteUser(User user) {
        return UserDatabaseConnector.deleteUserByEmail(user.getEMailAddress());
    }

    @Override
    public boolean editUser(User user) {
        return UserDatabaseConnector.updateUser(user);
    }

    @Override
    public Optional<User> readUserByEmail(String email) {
        return UserDatabaseConnector.readUserByEMail(email);
    }

    @Override
    public boolean getAdminUserIsPresentInDatabase() {
        return UserDatabaseConnector.getAdminUserIsPresentInDatabase();
    }
}
