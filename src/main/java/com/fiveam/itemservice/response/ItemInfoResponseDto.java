package com.fiveam.itemservice.response;

import com.fiveam.itemservice.category.entity.Category;
import com.fiveam.itemservice.item.entity.Item;
import com.fiveam.itemservice.nutritionFact.entity.NutritionFact;
import com.fiveam.itemservice.review.entity.Review;
import com.fiveam.itemservice.talk.entity.Talk;
import com.fiveam.itemservice.wish.entity.Wish;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ItemInfoResponseDto {

    private Long itemId;

    private String title;

    private String content;

    private String thumbnail;

    private String descriptionImage;

    private String expiration;

    private int discountPrice;

    private int price;

    private int discountRate;

    private int view;

    private int sales;

    private int capacity;

    private int servingSize;

    private int totalWishes;

    private String brand;

    private List<Wish> wishList = new ArrayList<>();

    private List<Category> categories = new ArrayList<>();

    private double starAvg;

    private List<Review> reviews = new ArrayList<>();

    private List<Talk> talks = new ArrayList<>();

    private List<NutritionFact> nutritionFacts = new ArrayList<>();

    public static ItemInfoResponseDto fromEntity(Item item) {
        return ItemInfoResponseDto.builder()
                .itemId(item.getItemId())
                .title(item.getTitle())
                .content(item.getContent())
                .thumbnail(item.getThumbnail())
                .descriptionImage(item.getDescriptionImage())
                .expiration(item.getExpiration())
                .discountPrice(item.getDiscountPrice())
                .price(item.getPrice())
                .discountRate(item.getDiscountRate())
                .view(item.getView())
                .sales(item.getSales())
                .capacity(item.getCapacity())
                .servingSize(item.getServingSize())
                .totalWishes(item.getTotalWishes())
                .brand(item.getBrand().name())
//                .wishList(item.getWishList().stream().map(wish -> wish.))
                .build();
    }
}