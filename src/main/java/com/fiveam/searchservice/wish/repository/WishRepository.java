package com.fiveam.searchservice.wish.repository;

import com.fiveam.searchservice.item.entity.Item;
import com.fiveam.searchservice.wish.entity.Wish;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {


    Wish findByItemAndUserId(Item item, Long userId);


    List<Wish> findAllByItem(long itemId);


    @Query("SELECT sum(w.isWish) from Wish w where w.item.itemId = :itemId")
    int findWishValue(@Param("itemId") long itemId);


    @Query("SELECT w FROM Wish w JOIN Item i ON w.item.itemId = i.itemId where w.userId = :userId and w.isWish = 1")
    Page<Wish> findAllByUser(Pageable pageable, @Param("userId") long userId);

    @Query("SELECT i.itemId FROM Wish w JOIN Item i ON w.item.itemId = i.itemId where w.userId = :userId and w.isWish = 1")
    List<Long> findItemIdByUser(@Param("userId") long userId);

}
