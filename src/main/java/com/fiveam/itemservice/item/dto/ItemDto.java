package com.fiveam.itemservice.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fiveam.itemservice.category.dto.CategoryDto;
import com.fiveam.itemservice.item.entity.Brand;
import com.fiveam.itemservice.nutritionFact.dto.NutritionFactDto;
import com.fiveam.itemservice.response.MultiResponseDto;
import com.fiveam.itemservice.review.dto.ReviewResponseDto;
import com.fiveam.itemservice.talk.dto.TalkAndCommentDto;

import java.util.List;

public class ItemDto {


    @Getter
    @Setter
    @NoArgsConstructor
    public static class post {

        private String thumbnail;
        private String descriptionImage;
        private String title;
        private String content;
        private String expiration;
        private Brand brand;
        private int sales;
        private int price;
        private int capacity;
        private int servingSize;
        private int discountRate;
        private int discountPrice;
        private List<CategoryDto.Post> categories;
        private List<NutritionFactDto.Post> nutritionFacts;
        private double starAvg;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemDetailResponse { // 아이템 상세 조회
        private Long itemId;
        private String thumbnail;
        private String descriptionImage;
        private String title;
        private String content;
        private String expiration;
        private Brand brand;
        private int sales;
        private int price;
        private int capacity;
        private int servingSize;
        private int discountRate;
        private int discountPrice;
        private List<String> categories;
        private List<NutritionFactDto.Response> nutritionFacts;
        private double starAvg;
        private MultiResponseDto<ReviewResponseDto> reviews;
        private MultiResponseDto<TalkAndCommentDto> talks;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemCategoryResponse { // 목록 조회
        private Long itemId;
        private String thumbnail;
        private String title;
        private String content;
        private int capacity;
        private int price;
        private int discountRate;
        private int discountPrice;
        private double starAvg;
        private int reviewSize;
        private Brand brand;
        private List<NutritionFactDto.Response> nutritionFacts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemMainTop9Response {
        private MultiResponseDto<ItemCategoryResponse> bestItem;
        private MultiResponseDto<ItemCategoryResponse> saleItem;
        private MultiResponseDto<ItemCategoryResponse> MdPickItem;
    }
}
