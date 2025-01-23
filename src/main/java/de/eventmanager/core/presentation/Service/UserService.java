package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.users.User;

import java.util.Optional;

public interface UserService {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email,  String phoneNumber, String password, String passwordConfirmation);
    boolean registerAdminUser(String firstName, String lastName, String dateOfBirth, String email,  String phoneNumber, String password, String passwordConfirmation, boolean isAdmin);
    Optional<User> loginUser(String email, String password);
    boolean deleteUser(User user);
    boolean editUser(String userID);
    Optional<User> readUserByEmail(String email);
}
