package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserCreateDTOTest {

    private UserCreateDTO userCreateDTO;
    private final String username = "testuser";
    private final String password = "Password123";
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        userCreateDTO = new UserCreateDTO(username, email, password);
    }

    @Test
    void test_getUsername() {
        Assertions.assertEquals(username, userCreateDTO.getUsername());
    }

    @Test
    void test_getPassword() {
        Assertions.assertEquals(password, userCreateDTO.getPassword());
    }

    @Test
    void test_getEmail() {
        Assertions.assertEquals(email, userCreateDTO.getEmail());
    }

    @Test
    void test_setUsername() {
        String newUsername = "newuser";
        userCreateDTO.setUsername(newUsername);
        Assertions.assertEquals(newUsername, userCreateDTO.getUsername());
    }

    @Test
    void test_setPassword() {
        String newPassword = "NewPassword456";
        userCreateDTO.setPassword(newPassword);
        Assertions.assertEquals(newPassword, userCreateDTO.getPassword());
    }

    @Test
    void test_setEmail() {
        String newEmail = "new@example.com";
        userCreateDTO.setEmail(newEmail);
        Assertions.assertEquals(newEmail, userCreateDTO.getEmail());
    }

    @Test
    void test_emptyConstructor() {
        UserCreateDTO emptyDTO = new UserCreateDTO();
        Assertions.assertNull(emptyDTO.getUsername());
        Assertions.assertNull(emptyDTO.getPassword());
        Assertions.assertNull(emptyDTO.getEmail());
    }
}