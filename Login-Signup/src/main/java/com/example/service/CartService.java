package com.example.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.CartItem;
import com.example.repository.CartRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

   
    public CartItem addToCart(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }

  
    public List<CartItem> getCartItems() {
        return cartRepository.findAll();
    }
    
    public Optional<CartItem> getCartItemById(Long id) {
        return cartRepository.findById(id);
    }
    

//    

    public CartItem updateCartItem(CartItem cartItem) {
        return cartRepository.save(cartItem);
    }
    public void removeCartItem(Long id) {
        cartRepository.deleteById(id);
    }

}
