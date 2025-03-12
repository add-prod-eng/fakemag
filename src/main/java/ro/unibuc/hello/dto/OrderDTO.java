package main.java.ro.unibuc.hello.dto;
 
import java.time.LocalDateTime;
import java.util.List;
 
public class OrderDTO {
    private Long id;
    private String userId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemDTO> items;
 
}