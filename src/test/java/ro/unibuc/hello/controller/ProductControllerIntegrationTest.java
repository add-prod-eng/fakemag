package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.dto.ProductDTO;
import ro.unibuc.hello.service.ProductService;
import ro.unibuc.hello.service.CategoryService;

import org.junit.jupiter.api.*;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Tag("IntegrationTest")
public class ProductControllerIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017);

    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String mongoUrl = "mongodb://localhost:" + mongoDBContainer.getMappedPort(27017);
        registry.add("mongodb.connection.url", () -> mongoUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void cleanUpAndAddTestData() throws Exception {
        categoryService.saveCategory(new CategoryDTO("10", "Category1", "Description1"));
        categoryService.saveCategory(new CategoryDTO("11", "Category2", "Description2"));

        productService.saveProduct(new ProductDTO("1", "Product 1", 100L, 10L, "10"));
        productService.saveProduct(new ProductDTO("2", "Product 2", 200L, 20L, "11"));

        // ProductDTO product1 = new ProductDTO("1", "Product 1", 100L, 10L, "Category1");
        // ProductDTO product2 = new ProductDTO("2", "Product 2", 200L, 20L, "Category2");

        // mockMvc.perform(post("/products")
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .content(new ObjectMapper().writeValueAsString(product1)))
        //         .andExpect(status().isOk());

        // mockMvc.perform(post("/products")
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .content(new ObjectMapper().writeValueAsString(product2)))
        //         .andExpect(status().isOk());
    }

    @AfterEach
    public void cleanUp() {
        productService.deleteProduct("1");
        productService.deleteProduct("2");
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductDTO product = new ProductDTO("Product New", 300L, 30L, "10");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Product New"))
                .andExpect(jsonPath("$.price").value(300L));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDTO product = new ProductDTO("1", "Product Updated", 150L, 15L, "Category1");

        mockMvc.perform(put("/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Product Updated"))
                .andExpect(jsonPath("$.price").value(150L));

        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Product Updated"))
                .andExpect(jsonPath("$[1].description").value("Product 2"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/1"))
            .andExpect(status().isOk());

        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].description").value("Product 2"));
    }

}