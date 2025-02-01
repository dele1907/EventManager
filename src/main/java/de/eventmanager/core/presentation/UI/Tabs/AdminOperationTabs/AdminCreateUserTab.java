package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminCreateUserTab implements Tab {
    private View textView;
    private UserController userController;
    private String loggedInUserUserID;

    public AdminCreateUserTab(View textView, UserController userController, String loggedInUserUserID) {
        this.textView = textView;
        this.userController = userController;
        this.loggedInUserUserID = loggedInUserUserID;
    }

    @Override
    public void start() {
        textView.displayTabOrPageHeading("\n===== Create User Tab ======");
        showCreateUserDialog();
    }

    public void showCreateUserDialog() {
        boolean validPhoneNumber = true;
        var phoneNumber = "";

        textView.displayUserInputMessage("Enter first name\n> ");
        var firstName = textView.getUserInput();
        textView.displayUserInputMessage("Enter last name\n> ");
        var lastName = textView.getUserInput();
        textView.displayUserInputMessage("Enter date of birth\n> ");
        var dateOfBirth = textView.getUserInput();
        textView.displayUserInputMessage("Enter email\n> ");
        var email = textView.getUserInput();
        textView.displayUserInputMessage("Enter phoneNumber\n> ");
        while (validPhoneNumber) {
            textView.displayMessage("Enter phone number: ");
            phoneNumber = textView.getUserInput();
            validPhoneNumber = !ValidationHelper.checkPhoneNumber(textView, phoneNumber); //If phoneNumber is valid, the loop ends
        }
        textView.displayUserInputMessage("Enter password\n> ");
        var password = textView.getUserInput();
        textView.displayUserInputMessage("Confirm password\n> ");
        var confirmPassword = textView.getUserInput();

        UserRegistrationData userRegistrationData = new UserRegistrationData(
                firstName, lastName, dateOfBirth,
                email, phoneNumber, password, confirmPassword
        );

        validateCreateUser(userRegistrationData);
    }

    private void validateCreateUser(UserRegistrationData userRegistrationData) {

        if (!userController.createNewUser(userRegistrationData, loggedInUserUserID)) {
            textView.displayErrorMessage("\nFailed to create user.\n");

            return;
        }

        textView.displaySuccessMessage("\nUser created successfully.\n");
    }
}