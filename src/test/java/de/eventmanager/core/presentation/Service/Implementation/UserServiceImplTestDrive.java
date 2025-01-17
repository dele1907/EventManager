package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTestDrive {

    @Test
    @Order(1)
    void registerUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        assertTrue(userServiceImpl.registerUser("Haenno", "Mustermann", "01.01.2000", "hae@muster.com",
                123456, "123456", "123456"));

        assertTrue(UserManager.deleteUserByID(UserManager.readUserByEMail("hae@muster.com").getUserID()));
    }

    @Test
    @Order(2)
    void loginUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        System.out.println(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").get());

        assertTrue(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").isPresent());
    }
}
