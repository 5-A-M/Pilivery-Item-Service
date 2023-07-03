package com.fiveam.itemservice.wish.entity;

import com.fiveam.itemservice.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;


    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column
    private Long userId;

    @Column
    private int isWish;


    public void addItem(Item item) {
        if (this.item == null && item != null) {
            this.item = item;
        }
    }

    public void addUser(Long userId) {
        if (this.userId == null && userId != null) {
            this.userId = userId;
        }
    }


}
