package main.java.ro.unibuc.hello.dto;
 
public class OrderProductDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private double priceAtOrderTime;
 
    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
 
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
 
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
 
    public double getPriceAtOrderTime() { return priceAtOrderTime; }
    public void setPriceAtOrderTime(double priceAtOrderTime) { this.priceAtOrderTime = priceAtOrderTime; }
}
