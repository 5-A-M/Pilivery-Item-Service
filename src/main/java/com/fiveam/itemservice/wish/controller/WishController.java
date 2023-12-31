package com.fiveam.itemservice.wish.controller;


import com.fiveam.itemservice.client.UserServiceClient;
import com.fiveam.itemservice.item.mapper.ItemMapper;
import com.fiveam.itemservice.response.MultiResponseDto;
import com.fiveam.itemservice.response.SingleResponseDto;
import com.fiveam.itemservice.wish.entity.Wish;
import com.fiveam.itemservice.wish.mapper.WishMapper;
import com.fiveam.itemservice.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/wishes")
public class WishController {

    private final WishMapper wishMapper;
    private final UserServiceClient userService;
    private final WishService wishService;

    private final ItemMapper itemMapper;


    @PostMapping("/{item-id}") // 로그인한 유저의 찜하기 기능
    public ResponseEntity wishItem(@PathVariable("item-id") @Positive @NotNull long itemId,
                                   @RequestParam(value = "wish", defaultValue = "1") int wish) {

        Wish wishItem = wishService.wishItem(itemId, wish);

        return new ResponseEntity<>(new SingleResponseDto<>(wishMapper.wishToWishDto(wishItem)), HttpStatus.OK);

    }

    @GetMapping // 로그인한 유저의 찜한 상품 목록 불러오기
    public ResponseEntity getWishItems(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                    @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                       @RequestParam(value = "sort", defaultValue = "wishId") String sort) {

        Page<Wish> pageWishes = wishService.findWishes(userService.getLoginUser().getId(), page-1, size, sort);
        List<Wish> wishes = pageWishes.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(wishMapper.wishesToWishItemResponse(wishes, itemMapper), pageWishes), HttpStatus.OK);
    }

    @GetMapping("/item") // 로그인한 유저의 찜한 상품(itemId) 불러오기
    public ResponseEntity getWishItemIdList() {
        Long[] itemId = wishService.findItemId(userService.getLoginUser().getId());
        return new ResponseEntity<>(new SingleResponseDto<>(itemId), HttpStatus.OK);
    }




}
