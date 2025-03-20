package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.database.Communication.NotificationDatabaseConnector;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.Management.UserManagerImpl;
import de.eventmanager.core.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    UserManager userManagerImpl = new UserManagerImpl();

    @Override
    public boolean registerUser(UserRegistrationDataPayload userRegistrationDataPayload, String loggedInUserUserID) {

        if (!userManagerImpl.isValidRegistrationPassword(
                userRegistrationDataPayload.password(),
                userRegistrationDataPayload.confirmPassword())) {
            return false;
        }

        return createNewUserFromPayload(userRegistrationDataPayload, false, loggedInUserUserID);

    }

    private boolean createNewUserFromPayload(UserRegistrationDataPayload userRegistrationDataPayload,
                                             boolean isAdminUser, String loggedInUserUserID) {

        return userManagerImpl.createNewUser(
                userRegistrationDataPayload.firstName(),
                userRegistrationDataPayload.lastName(),
                userRegistrationDataPayload.dateOfBirth(),
                userRegistrationDataPayload.email(),
                userRegistrationDataPayload.password(),
                userRegistrationDataPayload.phoneNumber(),
                isAdminUser,
                loggedInUserUserID
        );

    }

    @Override
    public boolean registerAdminUser(UserRegistrationDataPayload userRegistrationDataPayload, String loggedUserID) {

        if (!userManagerImpl.isValidRegistrationPassword(
                userRegistrationDataPayload.password(),
                userRegistrationDataPayload.confirmPassword()
        )) {
            return false;
        }

        return createNewUserFromPayload(userRegistrationDataPayload, true, loggedUserID);
    }

    @Override
    public String loginUser(String email, String password) {
        if (!userManagerImpl.authenticationUserLogin(email, password)) {
            return "";
        }
        return userManagerImpl.getUserByEmail(email).get().getUserID();
    }

    @Override
    public boolean deleteUser(String userToDeleteEmail, String loggedInUserID) {
        var userToDelete = userManagerImpl.getUserByEmail(userToDeleteEmail);

        if (userToDelete.isEmpty()) {
            return false;
        }

       return userManagerImpl.deleteUser(userToDeleteEmail, loggedInUserID);
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
    public boolean getUserIsAdminUserByID(String userID) {
        return userManagerImpl.getUserByID(userID).get().getRole().equals(Role.ADMIN);
    }

    @Override
    public boolean getUserIsAdminUserByEmail(String email) {
        return userManagerImpl.getUserByEmail(email).get().getRole().equals(Role.ADMIN);
    }

    @Override
    public String getLoggedInUserName(String userID) {
        return userManagerImpl.getUserByID(userID).get().getFirstName();
    }

    @Override
    public boolean grantAdminRightsToUser(String email) {
        if (!getUserIsPresentInDatabaseByEmail(email)) {
            return false;
        }

        return userManagerImpl.addAdminStatusToUser(readUserByEmail(email).get());
    }

    public boolean removeAdminRightsFromUser(String email) {
        if (!getUserIsPresentInDatabaseByEmail(email)) {
            return false;
        }

        return userManagerImpl.removeAdminStatusFromUser(readUserByEmail(email).get());
    }

    @Override
    public List<String> getLoggedUsersNotifications(String userID) {
        var usersNotifications = new ArrayList<String>();
        NotificationDatabaseConnector.readNotificationsByUserID(userID).forEach(notification -> {
            usersNotifications.add(notification.getMessage());
        });

        return usersNotifications;
    }

    public List<String> getLoggedUsersUnreadNotifications(String userID) {
        var usersUnreadNotifications = new ArrayList<String>();
        NotificationDatabaseConnector.readNotificationsByUserID(userID).forEach(notification -> {
            if (!notification.isMarkedAsRead()) {
                usersUnreadNotifications.add(notification.getMessage());
            }
        });

        return usersUnreadNotifications;
    }

    @Override
    public void onOpenNotificationsMarkAsRead(String userID) {
        NotificationDatabaseConnector.readNotificationsByUserID(userID).stream()
            .filter(notification -> !notification.isMarkedAsRead())
            .forEach(notification -> {
                notification.setMarkedAsRead(true);
                NotificationDatabaseConnector.updateNotification(notification);
            });
    }

   @Override
   public int getNumberOfUnreadNotifications(String userID) {
       return (int) NotificationDatabaseConnector.readNotificationsByUserID(userID).stream()
               .filter(notification -> !notification.isMarkedAsRead())
               .count();
   }

    public void emptyUsersNotifications(String userID) {
        NotificationDatabaseConnector.readNotificationsByUserID(userID)
                .forEach(notification -> NotificationDatabaseConnector
                        .deleteNotification(notification.getNotificationID())
        );
    }
}
