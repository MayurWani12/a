package com.example.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.entity.CartItem;
//import com.example.entity.User;
import com.example.service.CartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/car")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;
 
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }



    @GetMapping
    public List<CartItem> getCartItems() {
        List<CartItem> items = cartService.getCartItems();
        System.out.println("Fetched cart items: " + items);
        return items;
    }

@PostMapping
public CartItem addToCart(@RequestBody CartItem cartItem) {
	return cartService.addToCart(cartItem);
}

   

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long id, @RequestBody CartItem cartItem) {
        Optional<CartItem> existingItem = cartService.getCartItemById(id);
        if (existingItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

      
        cartItem.setId(id); 
        CartItem updatedItem = cartService.updateCartItem(cartItem);

        return ResponseEntity.ok(updatedItem);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long id) {
        Optional<CartItem> existingItem = cartService.getCartItemById(id);
        if (existingItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cartService.removeCartItem(id);
        return ResponseEntity.noContent().build();
    }


}
