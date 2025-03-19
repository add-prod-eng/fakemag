package ro.unibuc.hello.service;

import ro.unibuc.hello.data.CartEntity;
import ro.unibuc.hello.data.CartRepository;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.data.ProductEntity;
import ro.unibuc.hello.data.ProductRepository;
import ro.unibuc.hello.dto.CartDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.exception.EntityNotFoundException;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private final AtomicLong counter = new AtomicLong();

    public CartDTO getCartById(String id) throws EntityNotFoundException {
        CartEntity cart = cartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        return new CartDTO(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.getDate());
    }

    public List<CartDTO> getCartsByUserId(String userId) {
        List<CartEntity> carts = cartRepository.findByUserId(userId);
        return carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.getDate()))
                .collect(Collectors.toList());
    }

    public List<CartDTO> getCartsByProductId(String productId) {
        List<CartEntity> carts = cartRepository.findByProductId(productId);
        return carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.getDate()))
                .collect(Collectors.toList());
    }

    public CartDTO saveCart(CartDTO cartDTO) {
        UserEntity user = userRepository.findById(cartDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException(cartDTO.getUserId()));
        ProductEntity product = productRepository.findById(cartDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException(cartDTO.getProductId()));
        CartEntity cart = new CartEntity(
                Long.toString(counter.incrementAndGet()),
                user,
                product,
                cartDTO.getDate()
        );
        cartRepository.save(cart);
        return new CartDTO(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.getDate());
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }

    public List<CartDTO> getAllCarts() {
        List<CartEntity> carts = cartRepository.findAll();
        return carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getUser().getId(), cart.getProduct().getId(), cart.getDate()))
                .collect(Collectors.toList());
    }
}
