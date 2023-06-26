package com.fiveam.searchservice.cart.mapper;

import org.mapstruct.Mapper;
import com.fiveam.searchservice.cart.dto.CartResponseDto;
import com.fiveam.searchservice.cart.entity.Cart;
import com.fiveam.searchservice.cart.entity.ItemCart;
import com.fiveam.searchservice.cart.service.CartService;
import com.fiveam.searchservice.cart.service.ItemCartService;
import com.fiveam.searchservice.item.mapper.ItemMapper;
import com.fiveam.searchservice.response.MultiResponseDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    default CartResponseDto cartToCartResponseDto(Cart cart, CartService cartService,
                                                  boolean subscription, ItemCartService itemCartService,
                                                  ItemMapper itemMapper, ItemCartMapper itemCartMapper) {
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setCartId(cart.getCartId());
        cartResponseDto.setSubscription(subscription);

        List<ItemCart> itemCarts = itemCartService.findItemCarts(cart, subscription); // 목록은 체크 + 언체크 모두 조회

        itemCartMapper.itemCartsToItemCartResponseDtos(itemMapper, itemCarts);
        cartResponseDto.setItemCarts(new MultiResponseDto<>(
                itemCartMapper.itemCartsToItemCartResponseDtos(itemMapper, itemCarts)));

        if(subscription) {
            cartResponseDto.setTotalPrice(cart.getSubTotalPrice() == null ? 0 : cart.getSubTotalPrice());
            cartResponseDto.setTotalItems(cart.getSubTotalItems() == null ? 0 : cart.getSubTotalItems());

        } else {
            cartResponseDto.setTotalPrice(cart.getTotalPrice() == null ? 0 : cart.getTotalPrice());
            cartResponseDto.setTotalItems(cart.getTotalItems() == null ? 0 : cart.getTotalItems());
        }

        cartResponseDto.setTotalDiscountPrice(cartService.countTotalDiscountPrice(cart.getCartId(), subscription));
        cartResponseDto.setExpectPrice(cartResponseDto.getTotalPrice() - cartResponseDto.getTotalDiscountPrice());

        return cartResponseDto;
    }
}
