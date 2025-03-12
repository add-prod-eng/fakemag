package ro.unibuc.hello.dto;

import java.util.List;

public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }
}
