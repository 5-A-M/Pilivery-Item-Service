package com.fiveam.itemservice.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiveam.itemservice.cart.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartId(Long cartId);
    Cart findByUserId(Long userId);
}
