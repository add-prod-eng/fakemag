package  main.java.ro.unibuc.hello.model;
 
import jakarta.persistence.*;
import lombok.*;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "cartId", referencedColumnName = "id", nullable = false)
    private Cart cart;
 
    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
    private ProductEntity product;
 
    private int quantity;
}
 
 
