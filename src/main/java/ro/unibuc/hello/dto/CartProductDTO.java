package ro.unibuc.hello.dto;
 
public class CartProductDTO {
    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
 
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
 
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}