package com.fiveam.searchservice.review.mapper;

import com.fiveam.searchservice.client.OrderServiceClient;
import com.fiveam.searchservice.client.UserServiceClient;
import com.fiveam.searchservice.response.ItemOrderInfoResponseDto;
import com.fiveam.searchservice.response.UserInfoResponseDto;
import org.mapstruct.Mapper;
import com.fiveam.searchservice.exception.bussiness.BusinessLogicException;
import com.fiveam.searchservice.exception.bussiness.ExceptionCode;
import com.fiveam.searchservice.item.mapper.ItemMapper;
import com.fiveam.searchservice.item.service.ItemService;
import com.fiveam.searchservice.review.dto.ReviewDetailResponseDto;
import com.fiveam.searchservice.review.dto.ReviewDto;
import com.fiveam.searchservice.review.dto.ReviewResponseDto;
import com.fiveam.searchservice.review.entity.Review;
import com.fiveam.searchservice.review.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    default Review reviewDtoToReview(
            long itemOrderId,
            OrderServiceClient orderService,
            UserServiceClient userService,
            ItemService itemService,
            ReviewDto reviewDto) {

        // 등록

        Review review = new Review();
        UserInfoResponseDto user = userService.getLoginUser();
        review.setUserId(user.getId());

        ItemOrderInfoResponseDto itemOrder = orderService.findItemOrder(itemOrderId);

        if (!orderService.isShopper(itemOrder.getItemId(), review.getUserId())) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        } // 아이템 구매자만 리뷰를 작성할 수 있음

        review.setQuantity(itemOrder.getQuantity());
        review.setItem(itemService.findVerifiedItem(itemOrder.getItemId()));
        review.setContent(reviewDto.getContent());
        review.setStar(reviewDto.getStar());

        return review;
    }

    default Review reviewDtoToReview(long reviewId, UserServiceClient userService,
                                     ReviewService reviewService, ReviewDto reviewDto) { // 수정

        UserInfoResponseDto user = userService.getLoginUser();

        if(user.getId() != reviewService.findReviewWriter(reviewId)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED_USER);
        } // 리뷰 작성자만 리뷰를 수정할 수 있음

        Review review = new Review();
        review.setReviewId(reviewId);
        review.setUserId(user.getId());
        review.setContent(reviewDto.getContent());
        review.setStar(reviewDto.getStar());

        return review;
    }

    default ReviewResponseDto reviewToReviewResponseDto(Review review, UserServiceClient userService) {
        UserInfoResponseDto user = userService.getLoginUser();

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setReviewId(review.getReviewId());
        reviewResponseDto.setItemId(review.getItem().getItemId());
        reviewResponseDto.setUserId(user.getId());
        reviewResponseDto.setDisplayName(user.getDisplayName());
        reviewResponseDto.setContent(review.getContent());
        reviewResponseDto.setStar(review.getStar());
        reviewResponseDto.setCreatedAt(review.getCreatedAt().toLocalDate().toString());
        reviewResponseDto.setUpdatedAt(review.getUpdatedAt().toLocalDate().toString());

        return reviewResponseDto;
    }

    default List<ReviewResponseDto> reviewsToReviewResponseDtos(List<Review> reviews, UserServiceClient userService) {

        if(reviews == null) return null;

        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

        for(Review review : reviews) {
            reviewResponseDtos.add(reviewToReviewResponseDto(review, userService));
        }

        return reviewResponseDtos;
    }

    default ReviewDetailResponseDto reviewToReviewDetailResponseDto(Review review, ItemMapper itemMapper) {


        ReviewDetailResponseDto reviewResponseDto = new ReviewDetailResponseDto();
        reviewResponseDto.setReviewId(review.getReviewId());
        reviewResponseDto.setItem(itemMapper.itemToItemSimpleResponseDto(review.getItem()));
        reviewResponseDto.setQuantity(review.getQuantity());
        reviewResponseDto.setUserId(review.getUserId());
        reviewResponseDto.setContent(review.getContent());
        reviewResponseDto.setStar(review.getStar());
        reviewResponseDto.setCreatedAt(review.getCreatedAt().toLocalDate().toString());
        reviewResponseDto.setUpdatedAt(review.getUpdatedAt().toLocalDate().toString());

        return reviewResponseDto;
    }

    default List<ReviewDetailResponseDto> reviewsToReviewDetailResponseDtos(List<Review> reviews, ItemMapper itemMapper) {

        if(reviews == null) return null;

        List<ReviewDetailResponseDto> reviewResponseDtos = new ArrayList<>();

        for(Review review : reviews) {
            reviewResponseDtos.add(reviewToReviewDetailResponseDto(review, itemMapper));
        }

        return reviewResponseDtos;
    }
}
