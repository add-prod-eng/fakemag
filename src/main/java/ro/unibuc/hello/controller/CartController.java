package ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.CartDTO;
import ro.unibuc.hello.service.CartService;
import java.util.List;
import ro.unibuc.hello.exception.EntityNotFoundException;



@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<CartDTO> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public CartDTO getCartById(@PathVariable String id) throws EntityNotFoundException {
        return cartService.getCartById(id);
    }

    @PostMapping
    public CartDTO createCart(@RequestBody CartDTO cartDto) {
        return cartService.saveCart(cartDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<CartDTO> getCartsByUserId(@PathVariable String userId) {
        return cartService.getCartsByUserId(userId);
    }

    @GetMapping("/product/{productId}")
    public List<CartDTO> getCartsByProductId(@PathVariable String productId) {
        return cartService.getCartsByProductId(productId);
    }

}