package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserLoginDTOTest {

    private UserLoginDTO userLoginDTO;
    private final String username = "testuser";
    private final String password = "Password123";

    @BeforeEach
    void setUp() {
        userLoginDTO = new UserLoginDTO(username, password);
    }

    @Test
    void test_getUsername() {
        Assertions.assertEquals(username, userLoginDTO.getUsername());
    }

    @Test
    void test_getPassword() {
        Assertions.assertEquals(password, userLoginDTO.getPassword());
    }

    @Test
    void test_setUsername() {
        String newUsername = "newuser";
        userLoginDTO.setUsername(newUsername);
        Assertions.assertEquals(newUsername, userLoginDTO.getUsername());
    }

    @Test
    void test_setPassword() {
        String newPassword = "NewPassword456";
        userLoginDTO.setPassword(newPassword);
        Assertions.assertEquals(newPassword, userLoginDTO.getPassword());
    }

    @Test
    void test_emptyConstructor() {
        UserLoginDTO emptyDTO = new UserLoginDTO();
        Assertions.assertNull(emptyDTO.getUsername());
        Assertions.assertNull(emptyDTO.getPassword());
    }
}