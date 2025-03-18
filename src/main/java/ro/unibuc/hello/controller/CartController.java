package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.CartDto;
import ro.unibuc.hello.service.CartService;
import java.util.List;



@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartDto> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public CartDto getCartById(@PathVariable Long id) throws EntityNotFoundException {
        return cartService.getCartById(id);
    }

    @PostMapping
    public CartDto createCart(@RequestBody CartDto cartDto) {
        return cartService.saveCart(createdCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<CartDto> getCartsByUserId(@PathVariable Long userId) {
        return cartService.getCartsByUserId(userId);
    }

    @GetMapping("/product/{productId}")
    public List<CartDto> getCartsByProductId(@PathVariable Long productId) {
        return cartService.getCartsByProductId(productId);
    }

}