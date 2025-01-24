package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.users.User;

import java.util.Scanner;

public class TextView implements View{
    Scanner scanner = new Scanner(System.in);

    private final String FONT_COLOR_RED = "\u001B[31m";
    private final String FONT_COLOR_RESET = "\u001B[0m";
    private final String FONT_COLOR_GREEN = "\u001B[32m";
    private final String FONT_COLOR_NEON_RED = "\u001B[91m";
    private final String FONT_COLOR_NEON_BLUE = "\u001B[94m";
    private final String FONT_CYAN = "\u001B[36m";

    private final String FONT_UNDERLINED = "\n\u001B[4m";
    private final String FONT_BOLD = "\u001B[1m";

    @Override
    public void displayMessage(String message) {
        System.out.print(message);
    }

    @Override
    public void displayErrorMessage(String message) {
        System.out.print(FONT_COLOR_RED + message + FONT_COLOR_RESET);
    }

    @Override
    public void displaySuccessMessage(String message) {
        System.out.print(FONT_COLOR_GREEN + FONT_BOLD + message + FONT_COLOR_RESET);
    }

    @Override
    public void displayWarningMessage(String message) {
        System.out.print(FONT_COLOR_NEON_RED + message + FONT_COLOR_RESET);
    }

    @Override
    public void displayTabOrPageHeading(String message) {
        System.out.println(FONT_BOLD + FONT_COLOR_NEON_BLUE + message + FONT_COLOR_RESET);
    }

    @Override
    public void displayUnderlinedSubheading(String message) {
        System.out.println(FONT_UNDERLINED + FONT_BOLD + message + FONT_COLOR_RESET);
    }

    @Override
    public void displayUserInputMessage(String message) {
        System.out.print(FONT_CYAN + FONT_BOLD + message + FONT_COLOR_RESET);
    }

    @Override
    public String getUserInput() {
        return scanner.nextLine();
    }
}
