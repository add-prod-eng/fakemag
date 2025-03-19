package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.data.CategoryEntity;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(String id) throws EntityNotFoundException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);
        CategoryEntity category = optionalCategory.orElseThrow(() -> new EntityNotFoundException(id));
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public CategoryDTO getCategoryByName(String name) throws EntityNotFoundException {
        CategoryEntity category = categoryRepository.findByName(name);
        if (category == null) {
            throw new EntityNotFoundException("Category not found with name: " + name);
        }
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }
    

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        CategoryEntity category = new CategoryEntity(categoryDTO.getName(), categoryDTO.getDescription());
        categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) throws EntityNotFoundException {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepository.save(category);
        
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public void deleteCategory(String id) throws EntityNotFoundException {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        categoryRepository.delete(category);
    }
}
