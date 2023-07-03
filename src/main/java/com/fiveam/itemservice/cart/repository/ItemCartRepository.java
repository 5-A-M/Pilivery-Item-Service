package com.fiveam.itemservice.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiveam.itemservice.cart.entity.Cart;
import com.fiveam.itemservice.cart.entity.ItemCart;
import com.fiveam.itemservice.item.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCartRepository extends JpaRepository<ItemCart, Long> {

    ItemCart findByCartAndItemAndSubscription(Cart cart, Item item, boolean subscription);

    List<ItemCart> findAllByCartAndSubscription(Cart cart, boolean subscription);

    List<ItemCart> findAllByCartAndSubscriptionAndBuyNow(Cart cart, boolean subscription, boolean buyNow);

    void deleteByItemAndCart(Item item, Cart cart); // 주문시 장바구니에서 아이템 삭제
}
