package com.fiveam.searchservice.cart.mapper;

import com.fiveam.searchservice.cart.entity.Cart;
import com.fiveam.searchservice.cart.service.CartService;
import com.fiveam.searchservice.client.UserServiceClient;
import com.fiveam.searchservice.response.UserInfoResponseDto;
import org.mapstruct.Mapper;
import com.fiveam.searchservice.cart.dto.ItemCartDto;
import com.fiveam.searchservice.cart.entity.ItemCart;
import com.fiveam.searchservice.item.mapper.ItemMapper;
import com.fiveam.searchservice.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemCartMapper {

    default ItemCart itemCartPostDtoToItemCart(long itemId, UserServiceClient userService,
                                               ItemService itemService, CartService cartService,
                                               ItemCartDto.Post itemCartPostDto) {
        UserInfoResponseDto user = userService.getLoginUser();
        Cart cart = cartService.findCartByUserId(user.getId());
        return ItemCart.builder()
                .quantity(itemCartPostDto.getQuantity())
                .period(itemCartPostDto.getPeriod() == null ? 0 : itemCartPostDto.getPeriod())
                .buyNow(true)
                .subscription(itemCartPostDto.isSubscription())
                .cart(cart)
                .item(itemService.findVerifiedItem(itemId))
                .build();
    }

    default ItemCartDto.Response itemCartToItemCartResponseDto(ItemMapper itemMapper, ItemCart itemCart) {
        return ItemCartDto.Response.builder()
                .itemCartId(itemCart.getItemCartId())
                .quantity(itemCart.getQuantity())
                .period(itemCart.getPeriod())
                .buyNow(itemCart.isBuyNow())
                .subscription(itemCart.isSubscription())
                .item(itemMapper.itemToItemSimpleResponseDto(itemCart.getItem()))
                .createdAt(itemCart.getCreatedAt().toLocalDate().toString())
                .updatedAt(itemCart.getUpdatedAt().toLocalDate().toString())
                .build();
    }

    default List<ItemCartDto.Response> itemCartsToItemCartResponseDtos(ItemMapper itemMapper, List<ItemCart> itemCarts) {
        if(itemCarts == null) return null;

        List<ItemCartDto.Response> itemCartResponseDtos = new ArrayList<>(itemCarts.size());

        for(ItemCart itemCart : itemCarts) {
            itemCartResponseDtos.add(itemCartToItemCartResponseDto(itemMapper, itemCart));
        }

        return itemCartResponseDtos;
    }
}
