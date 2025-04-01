package ro.unibuc.hello.e2e.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestTemplate;
import ro.unibuc.hello.dto.UserDTO;
import ro.unibuc.hello.service.UserService;
import ro.unibuc.hello.e2e.util.HeaderSetup;
import ro.unibuc.hello.e2e.util.ResponseErrorHandler;
import ro.unibuc.hello.e2e.util.ResponseResults;

import java.util.HashMap;
import java.util.List;

@CucumberContextConfiguration
@SpringBootTest
public class UserSteps {

    @Autowired
    private UserService userService;
    
    private List<UserDTO> users;
    
    @Given("the user database is populated")
    public void the_user_database_is_populated() {
    }
    
    @When("I request all users")
    public void i_request_all_users() {
        users = userService.getAllUsers();
    }
    
    @Then("I should receive a list of users")
    public void i_should_receive_a_list_of_users() {
        assertFalse(users.isEmpty(), "User list should not be empty");
    }
}