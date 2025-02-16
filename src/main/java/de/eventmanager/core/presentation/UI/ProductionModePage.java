package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.presentation.PresentationHelpers.DefaultDialogHelper;
import de.eventmanager.core.presentation.UI.Tabs.Tab;
import helper.ConfigurationDataSupplierHelper;

public class ProductionModePage implements Tab {
    View textView;

    public ProductionModePage(View textView) {
        this.textView = textView;
    }

    @Override
    public void start() {
        if (!ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE) {
            displayChangeDialog();
        }
    }

    private void displayChangeDialog() {
        textView.displayWarningMessage(DefaultDialogHelper.WARNING_MESSAGE);
        textView.displayErrorMessage(
                "\nYou are currently in develop mode." +
                "\n\nWould you like to switch to production mode?" +
                "\n"+
                DefaultDialogHelper.ACCEPT_OR_ABORT_MESSAGE
        );
        String choice = textView.getUserInput();

        if (choice.equalsIgnoreCase(DefaultDialogHelper.CONFIRM)) {
            textView.displaySuccessMessage("\nSwitching to production mode...\n");
            enableProductionMode();
        } else {
            textView.displayErrorMessage("\nStaying in develop mode...\n");
        }
    }
    private void enableProductionMode() {
        ConfigurationDataSupplierHelper.setIsProductionMode(true);
    }

}
