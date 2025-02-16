package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminRemoveAdminRightsTab implements Tab {
    private View view;
    private UserServiceImpl userServiceImpl;

    public AdminRemoveAdminRightsTab(View view) {
        this.view = view;
        this.userServiceImpl = new UserServiceImpl();
    }

    @Override
    public void start() {
        DefaultDialogHelper.getTabOrPageHeading(view, "Remove Admin Rights");
        view.displayUserInputMessage("\nEnter the e-Mail address of the user you want to remove admin rights from\n> ");
        var email = view.getUserInput();

        if (!userServiceImpl.getUserIsAdminUserByEmail(email)) {
            view.displayErrorMessage("\nUser is not an admin user!\n");

            return;
        }

        if (!handleSureToRemoveAdminRights(email)) {
            view.displayErrorMessage("\nAdmin rights not removed!\n");

            return;
        }

        if (!userServiceImpl.removeAdminRightsFromUser(email)) {
            view.displayErrorMessage("\nError removing admin rights.\nCheck if user exists!\n");

            return;
        }

        view.displaySuccessMessage("\nAdmin rights removed successfully!\n");
    }

    private boolean handleSureToRemoveAdminRights(String email) {
        view.displayMessage(
        "\nAre you sure you want to remove admin rights from " +
        userServiceImpl.getUserInformationByEmail(email) +
        "?"
        );
        view.displayUserInputMessage("\n" + DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE);

        return view.getUserInput().equalsIgnoreCase("yes");
    }
}
