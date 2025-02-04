package de.eventmanager.core.presentation.UI;

public interface View {
    void displayMessage(String message);
    void displayErrorMessage(String message);
    void displaySuccessMessage(String message);
    void displayWarningMessage(String message);
    void displayTabOrPageHeading(String message);
    void displayUnderlinedSubheading(String message);
    void displayUserInputMessage(String message);
    void displayItemSeparatorMessage(String message);
    String getUserInput();
}
