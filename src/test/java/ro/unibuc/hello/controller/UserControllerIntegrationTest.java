package ro.unibuc.hello.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ro.unibuc.hello.data.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class UserControllerIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String mongoUrl = "mongodb://localhost:" + mongoDBContainer.getMappedPort(27017);
        registry.add("mongodb.connection.url", () -> mongoUrl);
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeAll
    public static void startContainer() {
        mongoDBContainer.start();
    }
    
    @AfterAll
    public static void stopContainer() {
        mongoDBContainer.stop();
    }
    
    @BeforeEach
    public void cleanDatabase() {
        userRepository.deleteAll();
    }
    
    @Test
    public void testCreateUserAndGetUser() throws Exception {
        String userJson = "{\"username\":\"newUser\",\"password\":\"Password123\",\"email\":\"new@example.com\"}";
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newUser"))
            .andExpect(jsonPath("$.email").value("new@example.com"));
        
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }
}