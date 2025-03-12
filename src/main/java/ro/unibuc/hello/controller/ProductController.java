package main.java.ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.service.GreetingsService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return productRepository.save(product);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock());
                    product.setCategory(updatedProduct.getCategory());
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}