package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.ProductDTO;
import ro.unibuc.hello.service.ProductService;
import ro.unibuc.hello.exception.ValidationException;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void test_getAllProducts() throws Exception {
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO("1", "Laptop", 3000L, 10L, "Electronics"),
                new ProductDTO("2", "Phone", 1500L, 20L, "Electronics")
        );
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].description").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].description").value("Phone"));
    }

    @Test
    void test_getProductById() throws Exception {
        String id = "1";
        ProductDTO product = new ProductDTO(id, "Laptop", 3000L, 10L, "Electronics");
        when(productService.getProductById(id)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Laptop"));
    }

    @Test
void test_getTopMostOrderedProducts() throws Exception {
    List<ProductDTO> topProducts = Arrays.asList(
            new ProductDTO("1", "Laptop", 3000L, 10L, "Electronics"),
            new ProductDTO("2", "Phone", 1500L, 20L, "Electronics")
    );
    when(productService.getTopMostOrderedProducts(2)).thenReturn(topProducts);

    mockMvc.perform(get("/products/top-ordered")
            .param("limit", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].description").value("Laptop"))
            .andExpect(jsonPath("$[1].id").value("2"))
            .andExpect(jsonPath("$[1].description").value("Phone"));
}

    @Test
    void test_createProduct() throws Exception {
        ProductDTO newProduct = new ProductDTO("1", "Laptop", 3000L, 10L, "Electronics");
        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(newProduct);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Laptop\",\"price\":3000,\"stock\":10,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Laptop"));
    }

    @Test
    void test_createProductWithoutPrice() throws Exception {
        doThrow(new ValidationException("Product price must be greater than zero."))
                .when(productService).saveProduct(any(ProductDTO.class));
    
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Laptop\",\"stock\":10,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product price must be greater than zero."));
    }

    @Test
    void test_createProductWithNegativeStock() throws Exception {
        doThrow(new ValidationException("Product stock cannot be negative."))
                .when(productService).saveProduct(any(ProductDTO.class));
    
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Laptop\",\"price\":3000,\"stock\":-5,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product stock cannot be negative."));
    }

    @Test
    void test_createProductWithZeroPrice() throws Exception {
        doThrow(new ValidationException("Product price must be greater than zero."))
                .when(productService).saveProduct(any(ProductDTO.class));
    
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Laptop\",\"price\":0,\"stock\":10,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product price must be greater than zero."));
    }

    @Test
    void test_createProductWithoutDescription() throws Exception {
        ProductDTO invalidProduct = new ProductDTO(null, 3000L, 10L, "Electronics");

        doThrow(new ValidationException("Product description is mandatory."))
                .when(productService).saveProduct(any(ProductDTO.class));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\":3000,\"stock\":10,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product description is mandatory."));
    }

    @Test
    void test_saveAllProductsWithoutDescription() throws Exception {
         List<ProductDTO> invalidProducts = Arrays.asList(
                 new ProductDTO(null, 3000L, 10L, "Electronics")
          );

         doThrow(new ValidationException("Product description is mandatory."))
                 .when(productService).saveAll(anyList());

        mockMvc.perform(post("/products/bulk")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("[{\"price\":3000,\"stock\":10,\"categoryId\":\"Electronics\"}]"))
                 .andExpect(status().isBadRequest())
                 .andExpect(content().string("Product description is mandatory."));
     }

    @Test
    void test_updateProduct() throws Exception {
        String id = "1";
        ProductDTO updatedProduct = new ProductDTO(id, "Updated Laptop", 3500L, 8L, "Electronics");
        when(productService.updateProduct(eq(id), any(ProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Updated Laptop\",\"price\":3500,\"stock\":8,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("Updated Laptop"));
    }

    @Test
    void test_deleteProduct() throws Exception {
        String id = "1";
        ProductDTO product = new ProductDTO(id, "Laptop", 3000L, 10L, "Electronics");
        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Laptop\",\"price\":3000,\"stock\":10,\"categoryId\":\"Electronics\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(id);

        when(productService.getAllProducts()).thenReturn(List.of());
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
