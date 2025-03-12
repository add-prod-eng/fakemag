package main.java.ro.unibuc.hello.data;
 
import jakarta.persistence.*;
import lombok.*;
 
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private UserEntity user;
 
    private LocalDateTime orderDate;
 
    private String status; // PENDING, SHIPPED, DELIVERED, CANCELLED
 
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
}
