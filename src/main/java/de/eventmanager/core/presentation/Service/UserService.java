package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.users.User;

import java.util.Optional;

public interface UserService {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email,  String phoneNumber,
                         String password, String passwordConfirmation, String LoggedInUserUserID);
    boolean registerAdminUser(String firstName, String lastName, String dateOfBirth, String email,
                              String phoneNumber, String password, String passwordConfirmation, String loggedUserID);
    String loginUser(String email, String password);
    boolean deleteUser(String userToDeleteEmail, String loggedInUserEmail);
    boolean editUser(User user);
    Optional<User> readUserByEmail(String email);
    boolean getUserIsPresentInDatabaseByEmail(String eMailAddress);
    boolean getAdminUserIsPresentInDatabase();
    boolean getUserIsAdminUser(String userID);
    String getLoggedInUserName(String userID);
}
