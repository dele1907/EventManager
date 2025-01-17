package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.users.User;

public interface UserService {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email, String password, int phoneNumber);
    User loginUser(String email, String password);
}
