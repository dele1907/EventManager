package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.users.User;

import java.util.Optional;

public interface UserService {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email, String password, String passwordConfirmation, int phoneNumber);
    Optional<User> loginUser(String email, String password);
}
