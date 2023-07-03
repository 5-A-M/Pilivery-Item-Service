package com.fiveam.itemservice.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fiveam.itemservice.cart.entity.Cart;
import com.fiveam.itemservice.cart.mapper.CartMapper;
import com.fiveam.itemservice.cart.mapper.ItemCartMapper;
import com.fiveam.itemservice.cart.service.CartService;
import com.fiveam.itemservice.cart.service.ItemCartService;
import com.fiveam.itemservice.item.mapper.ItemMapper;
import com.fiveam.itemservice.response.SingleResponseDto;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final ItemCartService itemCartService;
    private final ItemMapper itemMapper;
    private final ItemCartMapper itemCartMapper;

    @GetMapping
    public ResponseEntity getCart(@RequestParam(value="subscription", defaultValue="false") boolean subscription) {

        Cart cart = cartService.findMyCart();

        return new ResponseEntity<>(new SingleResponseDto<>(cartMapper.cartToCartResponseDto(
                        cart, cartService, subscription, itemCartService, itemMapper, itemCartMapper)), HttpStatus.OK);
    }

    @PostMapping
    public Long createCart(@RequestBody Long userId) {
        log.info("Creating Cart For New User... User Id: " + userId);
        return cartService.createCart(userId);
    }

}
