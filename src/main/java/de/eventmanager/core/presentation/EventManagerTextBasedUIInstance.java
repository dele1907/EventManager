package de.eventmanager.core.presentation;

import de.eventmanager.core.database.Communication.DatabaseConnector;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabaseInitializer;
import de.eventmanager.core.database.Communication.ProductiveSystemDatabase.DatabasePathManager;
import de.eventmanager.core.presentation.Service.Implementation.UserServiceImpl;
import de.eventmanager.core.presentation.Service.UserService;
import de.eventmanager.core.presentation.UI.AdminUserStartupRegistrationPage;
import de.eventmanager.core.presentation.UI.ProductionModePage;
import de.eventmanager.core.presentation.UI.Tabs.LoginRegistrationPage;
import de.eventmanager.core.presentation.UI.Tabs.MainMenuTab;
import de.eventmanager.core.presentation.UI.TextView;
import de.eventmanager.core.presentation.UI.View;
import helper.ConfigurationDataSupplierHelper;

public class EventManagerTextBasedUIInstance implements EventManagerInstance {
    private static View textView = new TextView();
    private static UserService userService = new UserServiceImpl();
    private static String loggedInUserID = "";
    private static LoginRegistrationPage loginRegistrationPage;
    private static AdminUserStartupRegistrationPage adminUserStartupRegistrationPage;
    private static ProductionModePage productionModePage = new ProductionModePage(textView);

    public void startEventManagerInstance() {
        ConfigurationDataSupplierHelper.setIsProductionMode(true);
        productionModePage.start();

        initPages();

        initDatabase(ConfigurationDataSupplierHelper.IS_PRODUCTION_MODE);

        boolean programIsRunning = true;
        boolean adminInDatabase = userService.getAdminUserIsPresentInDatabase();

        if (!adminInDatabase) {
            adminUserStartupRegistrationPage.start();
        }

        while (programIsRunning) {
            try {
                loginRegistrationPage.start();
                loggedInUserID = loginRegistrationPage.getLoggedInUser();

                if (!loggedInUserID.isEmpty()) {
                    new MainMenuTab(textView, loggedInUserID, loginRegistrationPage).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initDatabase(boolean isProductiveSystem) {
        String databasePath = DatabasePathManager.loadDatabasePath(isProductiveSystem);
        DatabaseConnector.setDatabasePath(databasePath);
        DatabaseInitializer.initialize();
        //textView.displaySuccessMessage("\nDatabase path: " + databasePath + "\n");
    }

    private void initPages() {
        loginRegistrationPage = new LoginRegistrationPage(textView);
        adminUserStartupRegistrationPage = new AdminUserStartupRegistrationPage(textView);
    }
}