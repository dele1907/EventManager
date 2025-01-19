package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.users.User;

public interface View {
    void displayMessage(String message);
    void displayErrorMessage(String message);
    String getUserInput();
}
