package com.fiveam.itemservice.review.controller;

import com.fiveam.itemservice.client.OrderServiceClient;
import com.fiveam.itemservice.client.UserServiceClient;
import com.fiveam.itemservice.item.mapper.ItemMapper;
import com.fiveam.itemservice.item.service.ItemService;
import com.fiveam.itemservice.response.MultiResponseDto;
import com.fiveam.itemservice.response.SingleResponseDto;
import com.fiveam.itemservice.response.UserInfoResponseDto;
import com.fiveam.itemservice.review.dto.ReviewDto;
import com.fiveam.itemservice.review.entity.Review;
import com.fiveam.itemservice.review.mapper.ReviewMapper;
import com.fiveam.itemservice.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final UserServiceClient userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final OrderServiceClient orderService;

    @PostMapping("/{itemOrder-id}") // 주문 상세 내역에서 작성
    public ResponseEntity postReview(@PathVariable("itemOrder-id") @Positive long itemOrderId,
                                     @RequestBody ReviewDto reviewDto) {

        Review review = reviewService.createReview(
                reviewMapper.reviewDtoToReview(itemOrderId, orderService, userService, itemService, reviewDto));

        return  new ResponseEntity<>(
                new SingleResponseDto<>(reviewMapper.reviewToReviewResponseDto(review, userService)), HttpStatus.CREATED);
    }

    @PatchMapping("/{review-id}") // 마이페이지 작성글 관리, 아이템 상세페이지
    public ResponseEntity updateReview(@PathVariable("review-id") @Positive long reviewId,
                                       @RequestBody ReviewDto reviewDto) {

        Review review = reviewMapper.reviewDtoToReview(reviewId, userService, reviewService, reviewDto);

        Review updatedReview = reviewService.updateReview(review);

        return  new ResponseEntity<>(new SingleResponseDto<>(
                reviewMapper.reviewToReviewResponseDto(updatedReview, userService)), HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity getUserReviews(@Positive @RequestParam(value="page", defaultValue="1") int page,
                                         @Positive @RequestParam(value="size", defaultValue="7") int size,
                                         @RequestParam(value="sort", defaultValue="reviewId") String sort) {

        Page<Review> pageReviews = reviewService.findReviews(userService.getLoginUser().getId(), page-1, size, sort);
        List<Review> reviews = pageReviews.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(
                reviewMapper.reviewsToReviewDetailResponseDtos(reviews, itemMapper), pageReviews), HttpStatus.OK);
    }

    @GetMapping("/{review-id}")
    public ResponseEntity getReview(@PathVariable("review-id") @Positive long reviewId) {

        Review review = reviewService.findReview(reviewId);

        return  new ResponseEntity<>(new SingleResponseDto<>(
                reviewMapper.reviewToReviewResponseDto(review, userService)), HttpStatus.OK);
    }

    @DeleteMapping("/{review-id}") // 마이페이지 작성글 관리, 아이템 상세페이지
    public ResponseEntity deleteReview(@PathVariable("review-id") @Positive long reviewId) {

        UserInfoResponseDto user = userService.getLoginUser();
        reviewService.deleteReview(reviewId, user.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
