package com.fiveam.searchservice.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiveam.searchservice.cart.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartId(Long cartId);
    Cart findByUserId(Long userId);
}
