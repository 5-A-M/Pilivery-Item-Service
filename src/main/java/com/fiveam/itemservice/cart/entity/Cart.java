package com.fiveam.itemservice.cart.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column
    @Setter
    private Integer totalItems;

    @Column
    @Setter
    private Integer totalPrice;

    @Column
    @Setter
    private Integer totalDiscountPrice;

    @Column
    @Setter
    private Integer subTotalItems;

    @Column
    @Setter
    private Integer subTotalPrice;

    @Column
    @Setter
    private Integer subTotalDiscountPrice;

    @Column
    @Setter
    private Long userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.PERSIST)
    List<ItemCart> itemCarts = new ArrayList<>();

    public void addItemCart(ItemCart itemCart) {
        this.itemCarts.add(itemCart);
        if(itemCart.getCart() != this) {
            itemCart.addCart(this);
        }
    }
    // 회원 한 명이 하나의 장바구니를 가지므로 회원당 1회만 장바구니 생성
    public static Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.userId = userId;
        return cart;
    }
}
