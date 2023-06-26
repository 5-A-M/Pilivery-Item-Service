package com.fiveam.searchservice.review.entity;

import com.fiveam.searchservice.audit.Auditable;
import com.fiveam.searchservice.item.entity.Item;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    @OnDelete(action = OnDeleteAction.CASCADE) // 아이템 삭제시 해당 itemId 를 참조하는 리뷰 삭제
    private Item item;

    @Column
    private Long userId;

    private int quantity; // 리뷰를 작성하는 주문의 아이템 구매 수량

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int star;
}
