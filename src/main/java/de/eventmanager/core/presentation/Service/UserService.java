package de.eventmanager.core.presentation.Service;

import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;

import java.util.List;

public interface UserService {
    boolean registerUser(UserRegistrationDataPayload userRegistrationDataPayload,
                         boolean isAdminUser, String LoggedInUserUserID);
    String loginUser(String email, String password);
    boolean deleteUser(String userToDeleteEmail, String loggedInUserEmail);
    void editUser(String userEmailAddress, String loggedInUserID, String newFirstName, String newLastName,
                  String newEmailAddress, String newPhoneNumber);
    boolean getUserIsPresentInDatabaseByEmail(String eMailAddress);
    boolean getAdminUserIsPresentInDatabase();
    boolean getUserIsAdminUserByID(String userID);
    boolean getUserIsAdminUserByEmail(String email);
    String getLoggedInUserName(String userID);
    String getUserInformationByEmail(String email);
    boolean grantAdminRightsToUser(String email);
    List<String> getLoggedUsersNotifications(String userID);
    List<String> getLoggedUsersUnreadNotifications(String userID);
    void onOpenNotificationsMarkAsRead(String userID);
    int getNumberOfUnreadNotifications(String userID);
    void emptyUsersNotifications(String userID);
}
