package com.fiveam.searchservice.cart.controller;

import com.fiveam.searchservice.cart.dto.ItemCartDto;
import com.fiveam.searchservice.cart.entity.ItemCart;
import com.fiveam.searchservice.cart.mapper.ItemCartMapper;
import com.fiveam.searchservice.cart.service.CartService;
import com.fiveam.searchservice.cart.service.ItemCartService;
import com.fiveam.searchservice.client.UserServiceClient;
import com.fiveam.searchservice.item.mapper.ItemMapper;
import com.fiveam.searchservice.item.service.ItemService;
import com.fiveam.searchservice.response.ItemCartInfoResponseDto;
import com.fiveam.searchservice.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class ItemCartController {

    private final ItemCartService itemCartService;
    private final ItemCartMapper itemCartMapper;
    private final ItemMapper itemMapper;
    private final ItemService itemService;
    private final CartService cartService;
    private final UserServiceClient userService;

    @PostMapping("/{item-id}") // 장바구니 담기
    public ResponseEntity postItemCart(@Valid @RequestBody ItemCartDto.Post itemCartPostDto,
        @PathVariable("item-id") @Positive long itemId) {

        ItemCart itemCart = itemCartService.addItemCart(itemCartMapper.
                itemCartPostDtoToItemCart(itemId, userService, itemService, cartService, itemCartPostDto));
        cartService.refreshCart(itemCart.getCart().getCartId(), itemCart.isSubscription());

        return new ResponseEntity<>(
                new SingleResponseDto<>(itemCartMapper.itemCartToItemCartResponseDto(
                        itemMapper, itemCart)), HttpStatus.CREATED);
    }

    @PatchMapping("/itemcarts/{itemcart-id}") // 장바구니 아이템 수량 변경
    public ResponseEntity upDownItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                          @RequestParam(value="upDown") int upDown) {

        ItemCart upDownItemCart = itemCartService.updownItemCart(itemCartId, upDown);
        cartService.refreshCart(upDownItemCart.getCart().getCartId(), upDownItemCart.isSubscription());

        return new ResponseEntity<>(new SingleResponseDto<>(
                itemCartMapper.itemCartToItemCartResponseDto(itemMapper, upDownItemCart)), HttpStatus.OK);
    }

    @PatchMapping("/itemcarts/period/{itemcart-id}") // 장바구니에서 정기구독 주기 변경
    public ResponseEntity periodItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                         @RequestParam(value="period") int period) {

        ItemCart itemCart = itemCartService.periodItemCart(itemCartId, period);

        return new ResponseEntity<>(new SingleResponseDto<>(
                itemCartMapper.itemCartToItemCartResponseDto(itemMapper, itemCart)), HttpStatus.OK);
    }

    @PatchMapping("/itemcarts/exclude/{itemcart-id}") // 장바구니 아이템 체크/해제 - 디폴트는 해제 요청
    public ResponseEntity excludeItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                        @RequestParam(value="buyNow", defaultValue = "false") Boolean buyNow) {
        ItemCart itemCart = itemCartService.excludeItemCart(itemCartId, buyNow);
        cartService.refreshCart(itemCart.getCart().getCartId(), itemCart.isSubscription());

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/itemcarts/{itemcart-id}") // 장바구니에서 특정 아이템 삭제
    public ResponseEntity deleteItemCart(@PathVariable("itemcart-id") @Positive long itemCartId,
                                         @RequestParam(value = "subscription") Boolean subscription) {
        long cartId = itemCartService.deleteItemCart(itemCartId);
        cartService.refreshCart(cartId, subscription);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{cartId}/itemcarts/{subscription}/buyNow/{buyNow}")
    public List<ItemCartInfoResponseDto> findItemCarts(
            @PathVariable Long cartId,
            @PathVariable boolean subscription,
            @PathVariable boolean buyNow
    ) {

        log.info("Find Item Cart: " + cartId + ", is Subscription Item?: " + subscription);
        System.out.println(cartId + ", " + subscription + ", " + buyNow);
        return itemCartService.findItemCarts(cartService.findCart(cartId), subscription, buyNow)
                .stream().map(ItemCartInfoResponseDto::fromEntity).collect(Collectors.toList());
    }

    @PostMapping("/itemcarts/refresh/{cartId}/{subscription}")
    public void refreshCart(@PathVariable Long cartId, @PathVariable Boolean subscription) {
        cartService.refreshCart(cartId, subscription);
    }

}
