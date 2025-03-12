package main.java.ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.model.Category;
import ro.unibuc.hello.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @ResponseBody
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(value -> ResponseEntity.ok(convertToDTO(value)))
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = convertToEntity(categoryDTO);
        return convertToDTO(categoryRepository.save(category));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());
            return ResponseEntity.ok(convertToDTO(categoryRepository.save(category)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}
