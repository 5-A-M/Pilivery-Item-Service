package com.fiveam.searchservice.item.entity;

import lombok.*;
import com.fiveam.searchservice.category.entity.Category;
import com.fiveam.searchservice.nutritionFact.entity.NutritionFact;
import com.fiveam.searchservice.review.entity.Review;
import com.fiveam.searchservice.talk.entity.Talk;
import com.fiveam.searchservice.wish.entity.Wish;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String thumbnail;

    @Column
    private String descriptionImage;

    @Column
    private String expiration;

    @Column
    private int discountPrice;

    @Column
    private int price;

    @Column
    private int discountRate;

    @Column
    private int view;

    @Column
    private int sales;

    @Column
    private int capacity;


    @Column
    private int servingSize;


    @Column
    private int totalWishes;


    @Enumerated(value = EnumType.STRING)
    private Brand brand;


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Wish> wishList = new ArrayList<>();


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();



    public void addCategories(Category category) {
        categories.add(category);
        category.setItem(this);
    }


    @Column
    private double starAvg;


    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Review> reviews = new ArrayList<>();


    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Talk> talks = new ArrayList<>();


    @OneToMany(mappedBy = "item", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<NutritionFact> nutritionFacts = new ArrayList<>();


}

