package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.Management.UserManagerImpl;
import de.eventmanager.core.users.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    UserManager userManagerImpl = new UserManagerImpl();

    @Override
    public boolean registerUser(String firstName, String lastName, String dateOfBirth,
            String email, String phoneNumber, String password,
            String passwordConfirmation, String loggedInUserUserID) {

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
    public String loginUser(String email, String password) {
        if (!userManagerImpl.authenticationUserLogin(email, password)) {
            return "";
        }
        return userManagerImpl.getUserByEmail(email).get().getUserID();
    }

    @Override
    public boolean deleteUser(String userToDeleteEmail, String loggedInUserEmail) {
        var userToDelete = userManagerImpl.getUserByEmail(userToDeleteEmail);

        if (userToDelete.isEmpty()) {
            return false;
        }


       return userManagerImpl.deleteUser(userToDeleteEmail, loggedInUserEmail);
    }

    @Override
    public void editUser(String userEmailAddress, String loggedInUserID, String newFirstName, String newLastName,
                         String newEmailAddress, String newPhoneNumber) {

        userManagerImpl.getUserByEmail(userEmailAddress).ifPresent(user -> {
            String firstName = Optional.ofNullable(newFirstName).orElse(user.getFirstName());
            String lastName = Optional.ofNullable(newLastName).orElse(user.getLastName());
            String emailAddress = Optional.ofNullable(newEmailAddress).orElse(user.getEMailAddress());
            String phoneNumber = Optional.ofNullable(newPhoneNumber).orElse(user.getPhoneNumber());

            userManagerImpl.editUser(user.getUserID(), firstName, lastName, user.getDateOfBirth(),
                    emailAddress, user.getPassword(), phoneNumber, loggedInUserID);
        });
    }

    @Override
    public Optional<User> readUserByEmail(String email) {
        return userManagerImpl.getUserByEmail(email);
    }

    @Override
    public String getUserInformationByEmail(String email) {
        return getUserIsPresentInDatabaseByEmail(email) ? readUserByEmail(email).get().toString() : "";
    }

    @Override
    public boolean getUserIsPresentInDatabaseByEmail(String eMailAddress) {
        return readUserByEmail(eMailAddress).isPresent();
    }

    @Override
    public boolean getAdminUserIsPresentInDatabase() {
        return UserDatabaseConnector.getAdminUserIsPresentInDatabase();
    }

    @Override
    public boolean getUserIsAdminUser(String userID) {
        return userManagerImpl.getUserByID(userID).get().getRole().equals(Role.ADMIN);
    }

    @Override
    public String getLoggedInUserName(String userID) {
        return userManagerImpl.getUserByID(userID).get().getFirstName();
    }
}
