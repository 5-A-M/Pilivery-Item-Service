package com.fiveam.itemservice.item.controller;

import com.fiveam.itemservice.client.UserServiceClient;
import com.fiveam.itemservice.item.dto.ItemDto;
import com.fiveam.itemservice.item.dto.SalesQuantityDto;
import com.fiveam.itemservice.item.entity.Item;
import com.fiveam.itemservice.item.mapper.ItemMapper;
import com.fiveam.itemservice.item.repository.ItemRepository;
import com.fiveam.itemservice.item.service.ItemService;
import com.fiveam.itemservice.response.ItemInfoResponseDto;
import com.fiveam.itemservice.response.MultiResponseDto;
import com.fiveam.itemservice.response.SingleResponseDto;
import com.fiveam.itemservice.review.mapper.ReviewMapper;
import com.fiveam.itemservice.review.service.ReviewService;
import com.fiveam.itemservice.talk.mapper.TalkMapper;
import com.fiveam.itemservice.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final ItemMapper mapper;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final TalkService talkService;
    private final TalkMapper talkMapper;
    private final UserServiceClient userService;

    @PostMapping("/items")
    public ResponseEntity postItem(@RequestBody ItemDto.post post) { // 아이템 등록을 위한 컨트롤러
        Item item = mapper.itemPostDtoToItem(post);
        log.info("item = {}",item);
        Item result = itemService.createItem(mapper.itemPostDtoToItem(post));
        return new ResponseEntity(new SingleResponseDto<>(mapper.itemToItemDetailResponseDto(result)), HttpStatus.OK);
    }


    @DeleteMapping("/items/{item-id}")
    public ResponseEntity deleteItem(@PathVariable("item-id") long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/items/{item-id}")
    public ResponseEntity getItem(@PathVariable("item-id") long itemId,
                                  @RequestParam(value="reviewPage", defaultValue="1") int reviewPage,
                                  @RequestParam(value="talkPage", defaultValue="1") int talkPage) {
        Item item = itemService.findItem(itemId);
        return new ResponseEntity(new SingleResponseDto<>(mapper.itemToItemDetailResponseDto(
                item, reviewService, reviewMapper, talkService, userService, talkMapper, reviewPage-1, 5, talkPage-1, 5)), HttpStatus.OK);
    }

    @GetMapping("/items/{item-id}/check")
    public ItemInfoResponseDto findVerifiedItem(@PathVariable("item-id") long itemId) {
        return ItemInfoResponseDto.fromEntity(itemService.findVerifiedItem(itemId));
    }

    @PostMapping("/items/list")
    public List<ItemInfoResponseDto> getItems(List<Long> itemIds) {
        List<ItemInfoResponseDto> items = new ArrayList<>();
        itemIds.forEach(itemId -> {
            try {
                items.add(ItemInfoResponseDto.fromEntity(
                    itemRepository.findById(itemId).orElseThrow(
                            () -> new Exception("존재하지 않는 상품입니다.")
                    ))
                );
            } catch (Exception e) {
                log.error("존재하지 않는 상품 찾기 시도... Item Id: " + itemId);
            }
        });
        return items;
    }



    @GetMapping("/main") // 메인화면에서 best 제품 9개 , 할인제품 9개 조회하기, MD pick(최신순) 9개 조회하기
    public ResponseEntity getMainItem() {
        return new ResponseEntity(new SingleResponseDto<>(mapper.itemToItemMainTop9ResponseDto(itemService)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchItems(@RequestParam("keyword") String keyword,
                                      @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                      @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                      @RequestParam(value = "sort", defaultValue = "sales") String sort) { // 키워드 검색
        Page<Item> itemPage = itemService.searchItems(keyword, page-1, size, sort);
        List<Item> itemList = itemPage.getContent();
        return new ResponseEntity(new MultiResponseDto<>(mapper.itemsToItemCategoryResponseDto(itemList), itemPage), HttpStatus.OK);
    }

    @GetMapping("/price")
    public ResponseEntity priceFilteredItems(@RequestParam("low") int low, @RequestParam("high") int high,
                                             @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                             @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                             @RequestParam(value = "sort", defaultValue = "sales") String sort) { // 가격 필터
        Page<Item> itemPage = itemService.pricefilteredItems(low, high, page-1, size, sort);
        List<Item> itemList = itemPage.getContent();
        return new ResponseEntity(new MultiResponseDto<>(mapper.itemsToItemCategoryResponseDto(itemList), itemPage), HttpStatus.OK);
    }

    @GetMapping("/search/price")
    public ResponseEntity searchPriceFilteredItems(@RequestParam("keyword") String keyword, @RequestParam("low") int low, @RequestParam("high") int high,
                                                   @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                   @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                                   @RequestParam(value = "sort", defaultValue = "sales") String sort) { // 키워드 검색 + 가격 필터
        Page<Item> itemPage = itemService.searchPriceFilteredItems(keyword, low, high, page-1, size, sort);
        List<Item> itemList = itemPage.getContent();
        return new ResponseEntity(new MultiResponseDto<>(mapper.itemsToItemCategoryResponseDto(itemList), itemPage), HttpStatus.OK);
    }

    @GetMapping("/search/sale")
    public ResponseEntity searchSaleItems(@RequestParam("keyword") String keyword,
                                          @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                          @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                          @RequestParam(value = "sort", defaultValue = "sales") String sort) { // 키워드 검색 + 세일
        Page<Item> itemPage = itemService.searchSaleItems(keyword, page-1, size, sort);
        List<Item> itemList = itemPage.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(mapper.itemsToItemCategoryResponseDto(itemList), itemPage), HttpStatus.OK);
    }

    @GetMapping("/search/sale/price")
    public ResponseEntity searchSalePriceFilteredItems(@RequestParam("keyword") String keyword, @RequestParam("low") int low, @RequestParam("high") int high,
                                                       @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                       @Positive @RequestParam(value = "size", defaultValue = "16") int size,
                                                       @RequestParam(value = "sort", defaultValue = "sales") String sort) { // 키워드 검색 + 세일 + 가격 필터
        Page<Item> itemPage = itemService.searchSalePriceFilteredItems(keyword, low, high, page-1, size, sort);
        List<Item> itemList = itemPage.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>(mapper.itemsToItemCategoryResponseDto(itemList), itemPage), HttpStatus.OK);
    }

    @PostMapping("/items/{itemId}/sales")
    public void plusSales(@PathVariable Long itemId, @RequestBody SalesQuantityDto salesQuantityDto) {
        itemService.plusSales(itemId, salesQuantityDto.getQuantity());
    }

    @DeleteMapping("/items/{itemId}/sales")
    public void minusSales(@PathVariable Long itemId, @RequestBody SalesQuantityDto salesQuantityDto) {
        itemService.minusSales(itemId, salesQuantityDto.getQuantity());
    }
}

