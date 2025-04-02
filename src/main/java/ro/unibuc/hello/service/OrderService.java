package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.OrderDTO;
import ro.unibuc.hello.data.ProductOrderRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;


    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(order.getId(), order.getUser().getId(), null, order.getStatus(), order.getCreatedAt(), null))
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(String id) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(order -> new OrderDTO(order.getId(), order.getUser().getId(), null, order.getStatus(), order.getCreatedAt(), null)).orElse(null);
    }

    public List<OrderDTO> getOrderHistoryByUserId(String userId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .map(order -> new OrderDTO(order.getId(), order.getUser().getId(), null, order.getStatus(), order.getCreatedAt(), null))
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(UserEntity user, List<ProductOrderEntity> productOrders, String status) {
        for (ProductOrderEntity productOrder : productOrders) {
            ProductEntity product = productOrder.getProduct();
            if (product.getStock() < productOrder.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - productOrder.getQuantity());
            productRepository.save(product);
        }

        List<ProductOrderEntity> savedProductOrders = productOrderRepository.saveAll(productOrders);

        // Performance-safe: exclude popularea orderHistory
        OrderEntity order = new OrderEntity(null, user, savedProductOrders, status, LocalDateTime.now(), null);
        orderRepository.save(order);

        return new OrderDTO(order.getId(), user.getId(), null, status, order.getCreatedAt(), null);
    }
    
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    public OrderDTO updateOrderStatus(String id, String newStatus) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
            return new OrderDTO(order.getId(), order.getUser().getId(), null, order.getStatus(), order.getCreatedAt(), order.getOrderHistory().stream()
                    .map(o -> new OrderDTO(o.getId(), o.getUser().getId(), null, o.getStatus(), o.getCreatedAt(), null))
                    .collect(Collectors.toList()));
        }
        return null;
    }

}



