package com.fiveam.searchservice.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fiveam.searchservice.response.MultiResponseDto;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class CartResponseDto {

    @Positive
    private Long cartId;
    private boolean subscription;
    private MultiResponseDto<ItemCartDto.Response> itemCarts;
    private int totalItems;
    private int totalPrice;
    private int totalDiscountPrice;
    private int expectPrice; //
    // 결제 예상 금액 (totalPrice - totalDiscountPrice)
}
