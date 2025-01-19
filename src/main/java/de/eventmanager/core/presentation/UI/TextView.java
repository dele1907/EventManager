package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.users.User;

import java.util.Scanner;

public class TextView implements View{
    Scanner scanner = new Scanner(System.in);

    private final String FONT_COLOR_RED = "\u001B[31m";
    private final String FONT_COLOR_RESET = "\u001B[0m";

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
    public String getUserInput() {
        return scanner.nextLine();
    }
}
