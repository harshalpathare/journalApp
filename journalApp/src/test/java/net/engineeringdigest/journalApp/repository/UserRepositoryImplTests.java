package net.engineeringdigest.journalApp.repository;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserArguementsProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    void testGetUserForSA() {
        List<User> users = userRepository.getUserForSA("ram");
        assertNotNull(users);
    }

}

