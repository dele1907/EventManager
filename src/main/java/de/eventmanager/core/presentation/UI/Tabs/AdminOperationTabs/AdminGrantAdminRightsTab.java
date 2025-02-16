package de.eventmanager.core.presentation.UI.Tabs.AdminOperationTabs;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import de.eventmanager.core.presentation.UI.View;

public class AdminGrantAdminRightsTab implements Tab {
    private View view;
    private UserService userService;

    public AdminGrantAdminRightsTab(View view) {
        this.view = view;
        this.userService = new UserServiceImpl();
    }

    @Override
    public void start() {
       DefaultDialogHelper.getTabOrPageHeading(view, "Grant Admin Rights");

       view.displayUserInputMessage("\nEnter the e-Mail address of the user you want to grant admin rights to\n> ");
       var email = view.getUserInput();

        if (userService.getUserIsAdminUserByEmail(email)) {
            view.displayErrorMessage("\nUser is an admin user!\n");

            return;
        }

       if (!handleSureToGrantAdminRights(email)) {
          view.displayErrorMessage("\nAdmin rights not granted!\n");

          return;
       }

       if (!userService.grantAdminRightsToUser(email)) {
           view.displayErrorMessage("\nError granting admin rights.\nCheck if user exists!\n");

           return;
       }

       view.displaySuccessMessage("\nAdmin rights granted successfully!\n");
    }

    private boolean handleSureToGrantAdminRights(String email) {
        view.displayMessage(
        "\nAre you sure you want to grant admin rights to " +
        userService.getUserInformationByEmail(email) +
        "?"
        );
        view.displayUserInputMessage(DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE);

        return view.getUserInput().equalsIgnoreCase("yes");
    }
}
