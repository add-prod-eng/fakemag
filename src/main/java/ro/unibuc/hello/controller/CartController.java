package main.java.ro.unibuc.hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ro.unibuc.hello.dto.CartDTO;
import ro.unibuc.hello.model.Cart;
import ro.unibuc.hello.model.User;
import ro.unibuc.hello.repository.CartRepository;
import ro.unibuc.hello.repository.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        return (cart != null) ? ResponseEntity.ok(convertToDTO(cart)) : ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<CartDTO> createCart(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Cart cart = new Cart();
            cart.setUser(user.get());
            return ResponseEntity.ok(convertToDTO(cartRepository.save(cart)));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        return dto;
    }
}
