package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import ro.unibuc.hello.data.ProductEntity;
import ro.unibuc.hello.data.UserEntity;

import java.util.Date;


@Document(collection = "carts")
public class CartEntity {
    
    @Id
    private String id;

    @DBRef
    private UserEntity user;

    @DBRef
    private ProductEntity product;

    private Date date;

    public CartEntity() {}

    public CartEntity(UserEntity userId, ProductEntity productId, Date date) {
        this.user = userId;
        this.product = productId;
        this.date = date;
    }

    public CartEntity(String id, UserEntity userId, ProductEntity productId, Date date) {
        this.id = id;
        this.user = userId;
        this.product = productId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity userId){
        this.user = userId;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity productId) {
        this.product = productId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("CartEntity[id=%s, userId=%s, productId=%s, date=%s]", id, user, product, date);
    }
}
