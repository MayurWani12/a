package com.example.service;


import com.example.entity.CartItem;
import com.example.entity.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServ {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public CartItem addToCart(Long userId, CartItem cartItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        cartItem.setUser(user);  
        return cartRepository.save(cartItem); 
    }
    
    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    
    
    
    public Optional<CartItem> getCartItemById(Long id) {
        return cartRepository.findById(id);
    }
    public CartItem updateCartItem(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }
    public void removeCartItem(Long id) {
        cartRepository.deleteById(id);
    }

}
