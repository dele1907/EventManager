package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.Management.UserManagerImpl;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    UserManager userManagerImpl = new UserManagerImpl();

    @Override
    public boolean registerUser(String firstName, String lastName, String dateOfBirth,
            String email, String phoneNumber, String password, String passwordConfirmation, String loggedInUserUserID) {

        if (!userManagerImpl.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }

        return userManagerImpl.createNewUser(firstName, lastName, dateOfBirth, email, password, phoneNumber,
                false, loggedInUserUserID);

    }

    @Override
    public boolean registerAdminUser(String firstName, String lastName, String dateOfBirth, String email,
            String phoneNumber, String password, String passwordConfirmation, String loggedUserID) {

        if (!userManagerImpl.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }

        return userManagerImpl.createNewUser(firstName, lastName, dateOfBirth, email, password, phoneNumber,
                true, loggedUserID);
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        if (!userManagerImpl.authenticationUserLogin(email, password)) {
            return Optional.empty();
        }
        return userManagerImpl.getUserByEmail(email);
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
        return userManagerImpl.getUserByEmail(email);
    }

    @Override
    public boolean getAdminUserIsPresentInDatabase() {
        return UserDatabaseConnector.getAdminUserIsPresentInDatabase();
    }
}
