package com.fiveam.searchservice.response;

import com.fiveam.searchservice.cart.entity.ItemCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartInfoResponseDto {

    private Long itemCartId;
    private Integer quantity;
    private Integer period;
    private boolean buyNow;
    private boolean subscription;
    private ItemSimpleResponseDto item;
    private Long itemId;;
    private String createdAt;
    private String updatedAt;

    public static ItemCartInfoResponseDto fromEntity(ItemCart itemcart) {
        return ItemCartInfoResponseDto.builder()
                .itemCartId(itemcart.getItemCartId())
                .quantity(itemcart.getQuantity())
                .period(itemcart.getPeriod())
                .buyNow(itemcart.isBuyNow())
                .subscription(itemcart.isSubscription())
                .item(ItemSimpleResponseDto.fromItemInfoResponse(
                        ItemInfoResponseDto.fromEntity(itemcart.getItem())
                ))
                .itemId(itemcart.getItem().getItemId())
                .createdAt(itemcart.getCreatedAt().toString())
                .updatedAt(itemcart.getUpdatedAt().toString())
                .build();
    }
}