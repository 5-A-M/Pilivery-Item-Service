package com.fiveam.searchservice.category.entity;

import lombok.*;
import com.fiveam.searchservice.item.entity.Item;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Category {

    @Id
    @Column(name = "CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;


    @Column
    private String categoryName;

    public void setItem(Item item) {
        this.item = item;
    }
}
