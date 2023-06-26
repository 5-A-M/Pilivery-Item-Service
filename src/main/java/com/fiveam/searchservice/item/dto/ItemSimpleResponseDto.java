package com.fiveam.searchservice.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fiveam.searchservice.item.entity.Brand;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSimpleResponseDto {

    private long itemId;
    private Brand brand;
    private String thumbnail;
    private String title;
    private int capacity;
    private int price;
    private int discountRate;
    private int disCountPrice;
}
