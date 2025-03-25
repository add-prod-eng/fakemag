package ro.unibuc.hello.service;

import ro.unibuc.hello.data.ProductEntity;
import ro.unibuc.hello.data.ProductRepository;
import ro.unibuc.hello.data.CategoryEntity;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.data.OrderRepository;
import ro.unibuc.hello.dto.ProductDTO;
import ro.unibuc.hello.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final AtomicLong counter = new AtomicLong();

    public ProductDTO getProductById(String id) throws EntityNotFoundException {
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);
        ProductEntity product = optionalProduct.orElseThrow(() -> new EntityNotFoundException(id));
        return new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(), 
                              product.getCategory() != null ? product.getCategory().getId() : null);
    }

    public List<ProductDTO> getProductsByCategory(String categoryId) {
        List<ProductEntity> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
        .map(product -> new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(), 
        product.getCategory() != null ? product.getCategory().getId() : null))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
                .map(product -> new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(),
                                               product.getCategory() != null ? product.getCategory().getId() : null))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getTopMostOrderedProducts(int limit) {
        Map<ProductDTO, Long> productOrderCounts = orderRepository.findAll().stream()
            .flatMap(order -> order.getProductOrders().stream()) // Assuming OrderEntity has a getProductOrders() method
            .map(productOrder -> {
                ProductEntity product = productOrder.getProduct();
                return new ProductDTO(
                    product.getId(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory() != null ? product.getCategory().getId() : null
                );
            })
            .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
    
        return productOrderCounts.entrySet().stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public ProductDTO saveProduct(ProductDTO productDTO) throws EntityNotFoundException {
        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + productDTO.getCategoryId()));

        ProductEntity product = new ProductEntity(
                Long.toString(counter.incrementAndGet()),
                productDTO.getDescription(),
                productDTO.getPrice(),
                productDTO.getStock(),
                category
        );
        productRepository.save(product);
        return new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getId());
    }

    public List<ProductDTO> saveAll(List<ProductDTO> productDTOs) {
        List<ProductEntity> products = productDTOs.stream()
                .map(dto -> {
                    CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId())); 
                    return new ProductEntity(
                            Long.toString(counter.incrementAndGet()),
                            dto.getDescription(),
                            dto.getPrice(),
                            dto.getStock(),
                            category
                    );
                })
                .collect(Collectors.toList());

        List<ProductEntity> savedProducts = productRepository.saveAll(products);

        return savedProducts.stream()
                .map(product -> new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getId()))
                .collect(Collectors.toList());
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) throws EntityNotFoundException {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + productDTO.getCategoryId()));

        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);
        productRepository.save(product);

        return new ProductDTO(product.getId(), product.getDescription(), product.getPrice(), product.getStock(), product.getCategory().getId());
    }

    public void deleteProduct(String id) throws EntityNotFoundException {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    
        productRepository.delete(product);
    }
    
}
