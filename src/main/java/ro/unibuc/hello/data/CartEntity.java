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

    public CartEntity(String userId, String productId, Date date) {
        this.userId = userId;
        this.productId = productId;
        this.date = date;
    }

    public CartEntity(String userId, String id, String productId, Date date) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("CartEntity[id=%s, userId=%s, productId=%s, date=%s]", id, userId, productId, date);
    }
}
