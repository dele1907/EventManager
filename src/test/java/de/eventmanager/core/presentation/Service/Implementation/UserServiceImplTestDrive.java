package de.eventmanager.core.presentation.Service.Implementation;

import de.eventmanager.core.users.Management.UserManager;
import de.eventmanager.core.users.User;
import helper.DatabaseSimulation.JsonDatabaseHelper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTestDrive {

    @Test
    @Order(1)
    void registerUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

       assertTrue(userServiceImpl.registerUser(
               "Dis",
               "Appear",
               "01.01.2000",
               "disappear@muster.com",
                12345678,
               "123456",
               "123456"
       ));

       assertTrue(JsonDatabaseHelper.removeUserByEmail("disappear@muster.com"));
    }

    @Test
    @Order(2)
    void loginUser() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        System.out.println(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").get());

        assertTrue(userServiceImpl.loginUser("max.mustermann@testmail.com", "password").isPresent());
    }
}
