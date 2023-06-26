package com.fiveam.searchservice.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiveam.searchservice.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);
}
