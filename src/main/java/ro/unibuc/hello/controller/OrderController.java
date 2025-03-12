package main.java.ro.unibuc.hello.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
 
import main.java.ro.unibuc.hello.dto.OrderDTO;
import main.java.ro.unibuc.hello.data.OrderEntity;
import main.java.ro.unibuc.hello.data.UserEntity;
import main.java.ro.unibuc.hello.repository.OrderRepository;
import main.java.ro.unibuc.hello.repository.UserRepository;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Controller
@RequestMapping("/orders")
public class OrderController {
 
    @Autowired
    private OrderRepository orderRepository;
 
    @Autowired
    private UserRepository userRepository;
 
    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<OrderDTO> getOrdersByUser(@PathVariable String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
 
    @PostMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<OrderDTO> createOrder(@PathVariable String userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
 
        if (user.isPresent()) {
            OrderEntity order = new OrderEntity();
            order.setUser(user.get());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("PENDING");
 
            return ResponseEntity.ok(convertToDTO(orderRepository.save(order)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @PutMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
 
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setStatus(status);
 
            return ResponseEntity.ok(convertToDTO(orderRepository.save(order)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
 
    private OrderDTO convertToDTO(OrderEntity order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        return dto;
    }
}
