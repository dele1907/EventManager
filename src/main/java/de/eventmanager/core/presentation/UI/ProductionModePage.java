package de.eventmanager.core.presentation.UI;

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
        textView.displayTabOrPageHeading("\n===== Production Mode Page ======");
        textView.displayMessage(
                "You are currently in develop mode." +
                        "\n\nWould you like to switch to production mode?" +
                        "\n\nType 'yes' to switch to production mode or 'no' to stay in develop mode:\n > "
        );
        String choice = textView.getUserInput();

        if ("yes".equals(choice.toLowerCase())) {
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
