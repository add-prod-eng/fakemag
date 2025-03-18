package ro.unibuc.hello.dto;

public class CartDTO {

    private String id;
    private String userId;
    private String productId;
    private Date date;

    public CartDTO() {}

    public CartDTO(String userId, String productId, Date date) {
        this.userId = userId;
        this.productId = productId;
        this.date = date;
    }

    public CartDTO(String userId, String id, String productId, Date date) {
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
}
