package com.example.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {
	 List<CartItem> findByUserId(Long userId);

}
