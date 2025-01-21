package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public boolean registerUser(
            String firstName,
            String lastName,
            String dateOfBirth,
            String email,
            String phoneNumber,
            String password,
            String passwordConfirmation
            ) {

        if (!UserManager.isValidRegistrationPassword(password, passwordConfirmation)) {
            return false;
        }

        return UserManager.createNewUser(new User(firstName, lastName, dateOfBirth, email, password, phoneNumber));
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        if (!UserManager.authenticationUserLogin(email, password)) {
            return Optional.empty();
        }

        return UserManager.readUserByEMail(email);
    }
}
