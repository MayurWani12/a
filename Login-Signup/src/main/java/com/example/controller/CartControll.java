package com.example.controller;

import com.example.entity.CartItem;
import com.example.service.CartServ;

import java.util.List;
import java.util.Optional;

//import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")  // Adjust CORS if necessary
public class CartControll {

    @Autowired
    private CartServ cartService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCartItemsByUserId(@PathVariable Long userId) {
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }
 

    @PostMapping("/add/{userId}")
    public ResponseEntity<?> addItemToCart(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        try {
            CartItem addedItem = cartService.addToCart(userId, cartItem);
            return ResponseEntity.ok(addedItem);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding item to cart: " + e.getMessage());
        }
    }
    
    

    
    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long itemId, @RequestBody CartItem cartItem) {
        Optional<CartItem> existingItem = cartService.getCartItemById(itemId);
        if (existingItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cartItem.setId(itemId);
        CartItem updatedItem = cartService.updateCartItem(cartItem);

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long itemId) {
        Optional<CartItem> existingItem = cartService.getCartItemById(itemId);
        if (existingItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cartService.removeCartItem(itemId);
        return ResponseEntity.noContent().build();
    }




}
