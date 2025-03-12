package  main.java.ro.unibuc.hello.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
 
import ro.unibuc.hello.dto.CartProductDTO;
import ro.unibuc.hello.model.Cart;
import ro.unibuc.hello.model.CartProduct;
import ro.unibuc.hello.model.ProductEntity;
import ro.unibuc.hello.repository.CartProductRepository;
import ro.unibuc.hello.repository.CartRepository;
import ro.unibuc.hello.repository.ProductRepository;
 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Controller
@RequestMapping("/cart-products")
public class CartProductController {
 
    @Autowired
    private CartProductRepository cartProductRepository;
 
    @Autowired
    private CartRepository cartRepository;
 
    @Autowired
    private ProductRepository productRepository;
 
    @GetMapping("/cart/{cartId}")
    @ResponseBody
    public List<CartProductDTO> getProductsInCart(@PathVariable Long cartId) {
        return cartProductRepository.findByCartId(cartId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
 
    @PostMapping
    @ResponseBody
    public ResponseEntity<CartProductDTO> addProductToCart(@RequestBody CartProductDTO cartProductDTO) {
        Optional<Cart> cart = cartRepository.findById(cartProductDTO.getCartId());
        Optional<ProductEntity> product = productRepository.findById(cartProductDTO.getProductId());
 
        if (cart.isPresent() && product.isPresent()) {
            CartProduct cartProduct = new CartProduct();
            cartProduct.setCart(cart.get());
            cartProduct.setProduct(product.get());
            cartProduct.setQuantity(cartProductDTO.getQuantity());
 
            return ResponseEntity.ok(convertToDTO(cartProductRepository.save(cartProduct)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CartProductDTO> updateProductQuantity(@PathVariable Long id, @RequestBody CartProductDTO cartProductDTO) {
        Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(id);
 
        if (optionalCartProduct.isPresent()) {
            CartProduct cartProduct = optionalCartProduct.get();
            cartProduct.setQuantity(cartProductDTO.getQuantity());
 
            return ResponseEntity.ok(convertToDTO(cartProductRepository.save(cartProduct)));
        }
        return ResponseEntity.notFound().build();
    }
 
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> removeProductFromCart(@PathVariable Long id) {
        if (cartProductRepository.existsById(id)) {
            cartProductRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
 
    private CartProductDTO convertToDTO(CartProduct cartProduct) {
        CartProductDTO dto = new CartProductDTO();
        dto.setId(cartProduct.getId());
        dto.setCartId(cartProduct.getCart().getId());
        dto.setProductId(cartProduct.getProduct().getId());
        dto.setQuantity(cartProduct.getQuantity());
        return dto;
    }
}
