package main.java.ro.unibuc.hello.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
 
import main.java.ro.unibuc.hello.dto.OrderProductDTO;
import main.java.ro.unibuc.hello.data.OrderProduct;
import main.java.ro.unibuc.hello.repository.OrderProductRepository;
import main.java.ro.unibuc.hello.repository.OrderRepository;
import main.java.ro.unibuc.hello.repository.ProductRepository;
 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Controller
@RequestMapping("/order-products")
public class OrderProductController {
 
    @Autowired
    private OrderProductRepository orderProductRepository;
 
    @Autowired
    private OrderRepository orderRepository;
 
    @Autowired
    private ProductRepository productRepository;
 
    @GetMapping("/order/{orderId}")
    @ResponseBody
    public List<OrderProductDTO> getProductsByOrder(@PathVariable Long orderId) {
        return orderProductRepository.findByOrderId(orderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
 
    @GetMapping("/product/{productId}")
    @ResponseBody
    public List<OrderProductDTO> getOrdersByProduct(@PathVariable Long productId) {
        return orderProductRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
 
    @PostMapping
    @ResponseBody
    public ResponseEntity<OrderProductDTO> addProductToOrder(@RequestBody OrderProductDTO orderProductDTO) {
        Optional<OrderEntity> order = orderRepository.findById(orderProductDTO.getOrderId());
        Optional<ProductEntity> product = productRepository.findById(orderProductDTO.getProductId());
 
        if (order.isPresent() && product.isPresent()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order.get());
            orderProduct.setProduct(product.get());
            orderProduct.setQuantity(orderProductDTO.getQuantity());
            orderProduct.setPriceAtOrderTime(orderProductDTO.getPriceAtOrderTime());
 
            return ResponseEntity.ok(convertToDTO(orderProductRepository.save(orderProduct)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<OrderProductDTO> updateProductInOrder(@PathVariable Long id, @RequestBody OrderProductDTO orderProductDTO) {
        Optional<OrderProduct> optionalOrderProduct = orderProductRepository.findById(id);
 
        if (optionalOrderProduct.isPresent()) {
            OrderProduct orderProduct = optionalOrderProduct.get();
            orderProduct.setQuantity(orderProductDTO.getQuantity());
            orderProduct.setPriceAtOrderTime(orderProductDTO.getPriceAtOrderTime());
 
            return ResponseEntity.ok(convertToDTO(orderProductRepository.save(orderProduct)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> removeProductFromOrder(@PathVariable Long id) {
        if (orderProductRepository.existsById(id)) {
            orderProductRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
 
    private OrderProductDTO convertToDTO(OrderProduct orderProduct) {
        OrderProductDTO dto = new OrderProductDTO();
        dto.setId(orderProduct.getId());
        dto.setOrderId(orderProduct.getOrder().getId());
        dto.setProductId(orderProduct.getProduct().getId());
        dto.setQuantity(orderProduct.getQuantity());
        dto.setPriceAtOrderTime(orderProduct.getPriceAtOrderTime());
        return dto;
    }
}
