package com.fiveam.itemservice.cart.service;

import com.fiveam.itemservice.cart.entity.Cart;
import com.fiveam.itemservice.cart.entity.ItemCart;
import com.fiveam.itemservice.cart.repository.CartRepository;
import com.fiveam.itemservice.client.UserServiceClient;
import com.fiveam.itemservice.exception.bussiness.BusinessLogicException;
import com.fiveam.itemservice.exception.bussiness.ExceptionCode;
import com.fiveam.itemservice.response.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemCartService itemCartService;
    private final UserServiceClient userService;

    public void refreshCart(long cartId, boolean subscription) { // 가격과 아이템 종류 갱신
        Cart cart = findVerifiedCart(cartId);

        if(subscription) {
            cart.setSubTotalPrice(countTotalPrice(cartId, subscription));
            cart.setSubTotalItems(countTotalItems(cartId, subscription));
        } else {
            cart.setTotalPrice(countTotalPrice(cartId, subscription));
            cart.setTotalItems(countTotalItems(cartId, subscription));
        }

        cartRepository.save(cart);
    }

    public Cart findMyCart() {
        // TODO: USERID 고치기
        UserInfoResponseDto user = userService.getLoginUser();
        return cartRepository.findByUserId(user.getId());
    }

    public Cart findCart(long cartId) {
        Cart findCart = findVerifiedCart(cartId);
        return findCart;
    }

    public Cart findVerifiedCart(long cartId) {
        Optional<Cart> optionalCart = cartRepository.findByCartId(cartId);
        Cart findCart = optionalCart.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.CART_NOT_FOUND));
        return findCart;
    }

    public int countTotalDiscountPrice(long cartId, boolean subscription) {
        Cart cart = findVerifiedCart(cartId);
        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart, subscription, true);

        if(itemCarts == null) return 0;

        int totalDiscountPrice = 0;

        for(ItemCart itemCart : itemCarts) {
            int quantity = itemCart.getItem().getPrice();
            int price = itemCart.getQuantity();
            int discountRate = itemCart.getItem().getDiscountRate();

            totalDiscountPrice += (quantity * price * discountRate/100);
        }

        return totalDiscountPrice;
    }

    private int countTotalPrice(long cartId, boolean subscription) {
        Cart cart = findVerifiedCart(cartId);
        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart, subscription, true);

        if(itemCarts == null) return 0;

        int totalPrice = 0;

        for(ItemCart itemCart : itemCarts) {
            int quantity = itemCart.getItem().getPrice();
            int price = itemCart.getQuantity();
            totalPrice += (quantity * price);
        }

        return totalPrice;
    }

    private int countTotalItems(long cartId, boolean subscription) {
        Cart cart = findVerifiedCart(cartId);
        return itemCartService.findItemCarts(cart, subscription, true).size();
    }

    public Long createCart(Long userId) {
        return cartRepository.save(Cart.createCart(userId)).getCartId();
    }

    public Cart findCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
