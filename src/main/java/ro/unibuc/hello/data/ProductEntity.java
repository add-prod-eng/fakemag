package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import ro.unibuc.hello.data.CategoryEntity;

public class ProductEntity {

    @Id
    private String id;
    
    private String name;
    private String description;
    private Long price;
    private Long stock;

    @DBRef
    private CategoryEntity category;

    public ProductEntity() {}

    public ProductEntity(String name, String description, Long price, Long stock, CategoryEntity category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public ProductEntity(String name, String id, String description, Long price, Long stock, CategoryEntity category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format(
                "Product[id='%s', name='%s', description='%s', price='%s', stock='%s', category='%s']",
                id, name, description, price, stock, category != null ? category.getName() : "null");
    }
}
