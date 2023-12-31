package com.fiveam.itemservice.nutritionFact.entity;

import lombok.*;
import com.fiveam.itemservice.item.entity.Item;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class NutritionFact {

    @Id
    @GeneratedValue
    private Long nutritionFactId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private String ingredient;

    private String volume;
}
