package ro.unibuc.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import ro.unibuc.hello.dto.ProductDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.ProductService;
import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ProductDTO getProductById(@PathVariable String id) throws EntityNotFoundException {
        return productService.getProductById(id);
    }

    @GetMapping("/products/category/{categoryId}")
    public List<ProductDTO> getProductsByCategory(@PathVariable String categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/products/top-ordered")
    public ResponseEntity<List<ProductDTO>> getTopMostOrderedProducts(@RequestParam(defaultValue = "5") int limit) {
        List<ProductDTO> topProducts = productService.getTopMostOrderedProducts(limit);
        return ResponseEntity.ok(topProducts);
}

    @PostMapping("/products")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productService.saveProduct(productDTO);
    }

    @PutMapping("/products/{id}")
    public ProductDTO updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) throws EntityNotFoundException {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable String id) throws EntityNotFoundException {
        productService.deleteProduct(id);
    }
}
