package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.service.CategoryService;
import ro.unibuc.hello.exception.ValidationException;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void test_getAllCategories() throws Exception {
        List<CategoryDTO> categories = Arrays.asList(
                new CategoryDTO("1", "Electronics", "Devices and gadgets"),
                new CategoryDTO("2", "Books", "Various books")
        );
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    @Test
    void test_getCategoryById() throws Exception {
        String id = "1";
        CategoryDTO category = new CategoryDTO(id, "Electronics", "Devices and gadgets");
        when(categoryService.getCategoryById(id)).thenReturn(category);

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void test_getCategoryByName() throws Exception {
        String name = "Electronics";
        CategoryDTO category = new CategoryDTO("1", name, "Devices and gadgets");
        when(categoryService.getCategoryByName(name)).thenReturn(category);

        mockMvc.perform(get("/categories/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void test_createCategory() throws Exception {
        CategoryDTO newCategory = new CategoryDTO("1", "Electronics", "Devices and gadgets");
        when(categoryService.saveCategory(any(CategoryDTO.class))).thenReturn(newCategory);

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Electronics\",\"description\":\"Devices and gadgets\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void test_updateCategory() throws Exception {
        String id = "1";
        CategoryDTO updatedCategory = new CategoryDTO(id, "Updated Electronics", "Updated description");
        when(categoryService.updateCategory(eq(id), any(CategoryDTO.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Electronics\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    @Test
    void test_deleteCategory() throws Exception {
        String id = "1";
        doNothing().when(categoryService).deleteCategory(id);

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).deleteCategory(id);
    }
}