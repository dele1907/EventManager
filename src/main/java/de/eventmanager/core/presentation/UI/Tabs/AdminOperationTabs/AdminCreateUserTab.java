package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.Controller.UserController;
import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminCreateUserTab implements Tab {
    private View view;
    private UserController userController;
    private String loggedInUserUserID;

    public AdminCreateUserTab(View view, UserController userController, String loggedInUserUserID) {
        this.view = view;
        this.userController = userController;
        this.loggedInUserUserID = loggedInUserUserID;
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Create User");
        showCreateUserDialog();
    }

    public void showCreateUserDialog() {
        validateCreateUser(DefaultDialogHelper.createNewUserDefaultDialog(view));
    }

    private void validateCreateUser(UserRegistrationDataPayload userRegistrationDataPayload) {

        if (!userController.createNewUser(userRegistrationDataPayload, loggedInUserUserID)) {
            view.displayErrorMessage("\nFailed to create user.\n");

            return;
        }

        view.displaySuccessMessage("\nUser created successfully.\n");
    }
}