package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.UserDTO;
import ro.unibuc.hello.dto.UserCreateDTO;
import ro.unibuc.hello.dto.UserLoginDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidCredentialsException;
import ro.unibuc.hello.exception.InvalidEmail;
import ro.unibuc.hello.exception.InvalidPassword;
import ro.unibuc.hello.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void test_createUser() throws Exception {
        UserCreateDTO createDTO = new UserCreateDTO("newUser", "Password123", "new@example.com");
        UserDTO createdUser = new UserDTO("1", "newUser", "new@example.com");
        when(userService.saveUser(any(createDTO.getClass()))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
            .content("{\"username\":\"newUser\",\"password\":\"password123\",\"email\":\"new@example.com\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.username").value("newUser"))
            .andExpect(jsonPath("$.email").value("new@example.com"));
    }

    @Test
    void test_createUser_invalidPassword() throws Exception {
        doThrow(new InvalidPassword("Invalid")).when(userService).checkPassword(anyString());

        mockMvc.perform(post("/users")
            .content("{\"username\":\"user\",\"password\":\"bad\",\"email\":\"user@example.com\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void test_createUser_invalidEmail() throws Exception {
        doThrow(new InvalidEmail("Bad email")).when(userService).checkEmail(anyString());

        mockMvc.perform(post("/users")
            .content("{\"username\":\"user\",\"password\":\"Password123\",\"email\":\"bademail\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void test_createUser_genericException() throws Exception {
        doThrow(new RuntimeException("Boom")).when(userService).checkPassword(anyString());

        mockMvc.perform(post("/users")
            .content("{\"username\":\"user\",\"password\":\"password123\",\"email\":\"email@example.com\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void test_getAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(
            new UserDTO("1", "newUser", "new@example.com"),
            new UserDTO("2", "user1", "user1@example.com"),
            new UserDTO("3", "user2", "user2@example.com")
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[1].id").value("2"))
            .andExpect(jsonPath("$[2].id").value("3"));
    }

    @Test
    void test_getUserById() throws Exception {
        String userId = "1";
        UserDTO user = new UserDTO(userId, "newUser", "new@example.com");
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void test_getUserById_notFound() throws Exception {
        String userId = "999";
        when(userService.getUserById(eq(userId))).thenThrow(new EntityNotFoundException(userId));

        mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isNotFound());
    }

    @Test
    void test_getUserByUsername() throws Exception {
        String username = "user1";
        UserDTO user = new UserDTO("2", username, "user1@example.com");
        when(userService.getUserByUsername(eq(username))).thenReturn(user);

        mockMvc.perform(get("/users/username/{username}", username))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void test_authenticateUser() throws Exception {
        doNothing().when(userService).authenticateUser(any(UserLoginDTO.class));

        mockMvc.perform(post("/users/authenticate")
            .content("{\"username\":\"testUser\",\"password\":\"password123\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void test_authenticateUser_invalidCredentials() throws Exception {
        doThrow(new InvalidCredentialsException()).when(userService).authenticateUser(any(UserLoginDTO.class));

        mockMvc.perform(post("/users/authenticate")
            .content("{\"username\":\"testUser\",\"password\":\"wrongPassword\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void test_authenticateUser_entityNotFound() throws Exception {
        doThrow(new EntityNotFoundException("user not found")).when(userService).authenticateUser(any(UserLoginDTO.class));

        mockMvc.perform(post("/users/authenticate")
            .content("{\"username\":\"ghost\",\"password\":\"ghostpass\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void test_authenticateUser_genericException() throws Exception {
        doThrow(new RuntimeException("Unexpected")).when(userService).authenticateUser(any(UserLoginDTO.class));

        mockMvc.perform(post("/users/authenticate")
            .content("{\"username\":\"user\",\"password\":\"pass\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void test_deleteUser() throws Exception {
        String userId = "1";
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
            .andExpect(status().isNoContent());
    }

    @Test
    void test_deleteUser_notFound() throws Exception {
        doThrow(new EntityNotFoundException("not found")).when(userService).deleteUser("999");

        mockMvc.perform(delete("/users/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void test_deleteUser_genericException() throws Exception {
        doThrow(new RuntimeException("generic error")).when(userService).deleteUser("123");

        mockMvc.perform(delete("/users/123"))
            .andExpect(status().isInternalServerError());
    }
}
