package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.PresentationHelpers.UserRegistrationDataPayload;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminCreateUserTab implements Tab {
    private View view;
    private String loggedInUserUserID;

    public AdminCreateUserTab(View view, String loggedInUserUserID) {
        this.view = view;
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

        if (!(new UserServiceImpl().registerUser(userRegistrationDataPayload, false, loggedInUserUserID))) {
            view.displayErrorMessage("\nFailed to create user.\n");

            return;
        }

        view.displaySuccessMessage("\nUser created successfully.\n");
    }
}