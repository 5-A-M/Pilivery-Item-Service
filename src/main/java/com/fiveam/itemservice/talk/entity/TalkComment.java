package com.fiveam.itemservice.talk.entity;

import com.fiveam.itemservice.audit.Auditable;
import com.fiveam.itemservice.item.entity.Item;
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
public class TalkComment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long talkCommentId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "TALK_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Talk talk;

    @Column
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private boolean shopper;
}
