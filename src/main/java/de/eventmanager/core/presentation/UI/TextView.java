package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.users.User;

import java.util.Scanner;

public class TextView implements View{
    Scanner scanner = new Scanner(System.in);

    @Override
    public void displayMessage(String message) {
        System.out.print(message);
    }

    @Override
    public String getUserInput() {
        return scanner.nextLine();
    }
}
