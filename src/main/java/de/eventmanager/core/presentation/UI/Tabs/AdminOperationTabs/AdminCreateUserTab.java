package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationData;
import de.eventmanager.core.presentation.PresentationHelpers.ValidationHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminCreateUserTab implements Tab {
    private View textView;
    private UserController userController;

    public AdminCreateUserTab(View textView, UserController userController) {
        this.textView = textView;
        this.userController = userController;
    }

    @Override
    public void start() {
        textView.displayMessage("\n===== Create User Tab ======\n");
        showCreateUserDialog();
    }

    public void showCreateUserDialog() {
        textView.displayMessage("Enter first name: ");
        var firstName = textView.getUserInput();
        textView.displayMessage("Enter last name: ");
        var lastName = textView.getUserInput();
        textView.displayMessage("Enter date of birth: ");
        var dateOfBirth = textView.getUserInput();
        textView.displayMessage("Enter email: ");
        var email = textView.getUserInput();
        var phoneNumber = ValidationHelper.checkPhoneNumber(textView);
        textView.displayMessage("Enter password: ");
        var password = textView.getUserInput();
        textView.displayMessage("Confirm password: ");
        var confirmPassword = textView.getUserInput();

        UserRegistrationData userRegistrationData = new UserRegistrationData(
                firstName, lastName, dateOfBirth,
                email, phoneNumber, password, confirmPassword
        );

        validateCreateUser(userRegistrationData);
    }

    private void validateCreateUser(UserRegistrationData userRegistrationData) {
        if (!userController.createNewUser(userRegistrationData)) {
            textView.displayErrorMessage("\nFailed to create user.\n");

            return;
        }

        textView.displaySuccessMessage("\nUser created successfully.\n");
    }
}