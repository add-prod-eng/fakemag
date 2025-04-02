package ro.unibuc.hello.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.unibuc.hello.data.ProductEntity;
import ro.unibuc.hello.data.ProductRepository;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("PerformanceTest")
public class OrderPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private String orderJson;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        productRepository.deleteAll();

        UserEntity user = userRepository.save(new UserEntity("john", "pass123", "john@example.com"));
        ProductEntity product = productRepository.save(new ProductEntity("Book", "Desc", 40L, 100L, null));

        orderJson = "{" +
                "\"userId\": \"" + user.getId() + "\"," +
                "\"status\": \"PENDING\"," +
                "\"productOrders\": [{\"productId\": \"" + product.getId() + "\", \"quantity\": 1}]" +
                "}";
    }

    @Test
    public void testCreateOrderPerformance() throws Exception {
        int requests = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < requests; i++) {
            mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(orderJson))
                    .andExpect(status().isOk());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Created " + requests + " orders in " + duration + " ms");
        System.out.println("Average time per request: " + (duration / (double) requests) + " ms");
    }
}

