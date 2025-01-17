package de.eventmanager.core.presentation.UI;

import de.eventmanager.core.users.User;

public interface View {
    boolean registerUser(String firstName, String lastName, String dateOfBirth, String email, String password, int phoneNumber);
    User loginUser(String email, String password);
}
