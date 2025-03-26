package ro.unibuc.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.dto.CartDTO;
import ro.unibuc.hello.dto.UserLoginDTO;
import ro.unibuc.hello.service.CartService;
import ro.unibuc.hello.service.UserService;
import java.util.List;
import ro.unibuc.hello.exception.EntityNotFoundException;
import ro.unibuc.hello.exception.InvalidCredentialsException;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<CartDTO> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public CartDTO getCartById(@PathVariable String id) throws EntityNotFoundException {
        return cartService.getCartById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CartDTO cartDto, @RequestBody UserLoginDTO userDTO) {
        try{
            userService.authenticateUser(userDTO);
            userService.equalUser(userDTO, cartDto.getUserId());
            return ResponseEntity.ok(cartService.saveCart(cartDto));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartsByUserId(@PathVariable String userId, @RequestBody UserLoginDTO userDTO) {
        try {
            userService.authenticateUser(userDTO);
            userService.equalUser(userDTO, userId);
            return ResponseEntity.ok(cartService.getCartsByUserId(userId));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public List<CartDTO> getCartsByProductId(@PathVariable String productId) {
        return cartService.getCartsByProductId(productId);
    }

}