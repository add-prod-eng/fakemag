package ro.unibuc.hello.controller;

import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable String id) throws EntityNotFoundException {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/name/{name}")
    public CategoryDTO getCategoryByName(@PathVariable String name) throws EntityNotFoundException {
        return categoryService.getCategoryByName(name);
    }

    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.saveCategory(categoryDTO);
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) throws EntityNotFoundException {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) throws EntityNotFoundException {
        categoryService.deleteCategory(id);
    }
}
