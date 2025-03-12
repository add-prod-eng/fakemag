package main.java.ro.unibuc.hello.data;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "id", nullable = false)
    private OrderEntity order;
 
    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
    private ProductEntity product;
 
    private int quantity;
    private double price; // Pretul produsului la momentul comenzii
}
