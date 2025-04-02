package ro.unibuc.hello.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDTOTest {

    private UserDTO userDTO;
    private final String id = "1";
    private final String username = "testuser";
    private final String email = "test@example.com";

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(id, username, email);
    }

    @Test
    void test_getId() {
        Assertions.assertEquals(id, userDTO.getId());
    }

    @Test
    void test_getUsername() {
        Assertions.assertEquals(username, userDTO.getUsername());
    }

    @Test
    void test_getEmail() {
        Assertions.assertEquals(email, userDTO.getEmail());
    }

    @Test
    void test_setId() {
        String newId = "2";
        userDTO.setId(newId);
        Assertions.assertEquals(newId, userDTO.getId());
    }

    @Test
    void test_setUsername() {
        String newUsername = "newuser";
        userDTO.setUsername(newUsername);
        Assertions.assertEquals(newUsername, userDTO.getUsername());
    }

    @Test
    void test_setEmail() {
        String newEmail = "new@example.com";
        userDTO.setEmail(newEmail);
        Assertions.assertEquals(newEmail, userDTO.getEmail());
    }

    @Test
    void test_emptyConstructor() {
        UserDTO emptyDTO = new UserDTO();
        Assertions.assertNull(emptyDTO.getId());
        Assertions.assertNull(emptyDTO.getUsername());
        Assertions.assertNull(emptyDTO.getEmail());
    }
}