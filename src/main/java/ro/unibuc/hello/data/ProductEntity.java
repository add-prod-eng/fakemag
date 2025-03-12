package main.java.ro.unibuc.hello.data;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long price;
    private Long stock;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Override
    public String toString() {
        return String.format(
                "Product[id=%s, name='%s', description='%s', price=%s, stock=%s, category='%s']",
                id, name, description, price, stock, category != null ? category.getName() : "N/A");
    }
}
