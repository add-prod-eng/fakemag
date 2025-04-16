package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.UserDTO;
import ro.unibuc.hello.dto.UserCreateDTO;
import ro.unibuc.hello.dto.UserLoginDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidCredentialsException;
import ro.unibuc.hello.exception.InvalidPassword;
import ro.unibuc.hello.exception.InvalidEmail;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private UserDTO testUserDTO;
    private UserCreateDTO testCreateDTO;
    private UserLoginDTO testLoginDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new UserEntity("testuser", "1", "Password123" , "test@example.com");
        testUserDTO = new UserDTO("1", "testuser", "test@example.com");
        testCreateDTO = new UserCreateDTO("testuser", "test@example.com", "Password123");
        testLoginDTO = new UserLoginDTO("testuser", "Password123");
    }

    @Test
    void testGetUserById_ExistingUser() throws EntityNotFoundException {
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));

        UserDTO result = userService.getUserById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetUserById_NonExistingUser() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById("999"));
    }

    @Test
    void testGetUserByUsername_ExistingUser() throws EntityNotFoundException {
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        UserDTO result = userService.getUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserByUsername_NonExistingUser() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername("nonexistent"));
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> users = Arrays.asList(
            testUser,
            new UserEntity("user2", "2", "Password123", "user2@example.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("2", result.get(1).getId());
        assertEquals("user2", result.get(1).getUsername());
    }

    // @Test
    // void testSaveUser() {
    //     when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

    //     UserDTO result = userService.saveUser(testCreateDTO);

    //     assertNotNull(result);
    //     assertEquals(testUser.getUsername(), result.getUsername());
    //     assertEquals(testUser.getEmail(), result.getEmail());
    // }

    @Test
    void testDeleteUser_ExistingUser() {
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("1");

        assertDoesNotThrow(() -> userService.deleteUser("1"));
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteUser_NonExistingUser() {
        when(userRepository.existsById("999")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser("999"));
    }

    @Test
    void testCheckPassword_ValidPassword() {
        assertDoesNotThrow(() -> userService.checkPassword("Password123"));
    }

    @Test
    void testCheckPassword_TooShort() {
        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> userService.checkPassword("Pass1"));
        assertTrue(exception.getMessage().contains("at least 6 characters"));
    }

    @Test
    void testCheckPassword_NoLowercase() {
        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> userService.checkPassword("PASSWORD123"));
        assertTrue(exception.getMessage().contains("lowercase"));
    }

    @Test
    void testCheckPassword_NoUppercase() {
        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> userService.checkPassword("password123"));
        assertTrue(exception.getMessage().contains("uppercase"));
    }

    @Test
    void testCheckPassword_NoDigit() {
        InvalidPassword exception = assertThrows(InvalidPassword.class, () -> userService.checkPassword("PasswordTest"));
        assertTrue(exception.getMessage().contains("digit"));
    }

    @Test
    void testCheckEmail_ValidEmail() {
        assertDoesNotThrow(() -> userService.checkEmail("test@example.com"));
    }

    @Test
    void testCheckEmail_NoAtSymbol() {
        Exception exception = assertThrows(RuntimeException.class, () -> userService.checkEmail("testexample.com"));
        assertTrue(exception.getMessage().contains("@"));
    }

    @Test
    void testCheckEmail_NoDomain() {
        Exception exception = assertThrows(RuntimeException.class, () -> userService.checkEmail("test@example"));
        assertTrue(exception.getMessage().contains("domain"));
    }

    @Test
    void testAuthenticateUser_ValidCredentials() {
        String rawPassword = "Password123";
        UserEntity userWithHashedPassword = new UserEntity("testuser", "1", "Password123", "test@example.com");
        
        when(userRepository.findByUsername("testuser")).thenReturn(userWithHashedPassword);

        assertDoesNotThrow(() -> userService.authenticateUser(testLoginDTO));
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        UserLoginDTO invalidLoginDTO = new UserLoginDTO("testuser", "WrongPassword");
        String rawPassword = "Password123";
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(rawPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        UserEntity userWithHashedPassword = new UserEntity("testuser", "1", hashedPassword, "test@example.com");
        
        when(userRepository.findByUsername("testuser")).thenReturn(userWithHashedPassword);

        assertThrows(InvalidCredentialsException.class, () -> userService.authenticateUser(invalidLoginDTO));
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        UserLoginDTO nonExistentUser = new UserLoginDTO("nonexistent", "Password123");
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userService.authenticateUser(nonExistentUser));
    }

    @Test
    void testEqualUser_ValidMatch() {
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.equalUser(testLoginDTO, "1"));
    }

    @Test
    void testEqualUser_UsernameMismatch() {
        UserLoginDTO mismatchedUser = new UserLoginDTO("differentuser", "Password123");
        when(userRepository.findById("1")).thenReturn(Optional.of(testUser));

        assertThrows(InvalidCredentialsException.class, () -> userService.equalUser(mismatchedUser, "1"));
    }

    @Test
    void testEqualUser_UserNotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.equalUser(testLoginDTO, "999"));
    }
}