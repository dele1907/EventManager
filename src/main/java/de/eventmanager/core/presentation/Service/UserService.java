package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.users.User;

import java.util.Optional;

public interface UserService {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email,  int phoneNumber, String password, String passwordConfirmation);
    Optional<User> loginUser(String email, String password);
}
