package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
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
        validateCreateUser(DefaultDialogHelper.createNewUserDefaultDialog(textView));
    }

    private void validateCreateUser(UserRegistrationData userRegistrationData) {

        if (!userController.createNewUser(userRegistrationData, loggedInUserUserID)) {
            textView.displayErrorMessage("\nFailed to create user.\n");

            return;
        }

        textView.displaySuccessMessage("\nUser created successfully.\n");
    }

    private String showPhoneNumberDialog() {
        String phoneNumber = "";

        textView.displayUserInputMessage("Enter phone number\n> ");
        phoneNumber = textView.getUserInput();

        if (!ValidationHelper.validatePhoneNumberInput(phoneNumber)) {
            textView.displayErrorMessage("\nInvalid phone number\n");
            return showPhoneNumberDialog();
        }

        return phoneNumber;
    }
}