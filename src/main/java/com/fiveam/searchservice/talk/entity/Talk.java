package com.fiveam.searchservice.talk.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiveam.searchservice.audit.Auditable;
import com.fiveam.searchservice.item.entity.Item;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Talk extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TALK_ID")
    private Long talkId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private boolean shopper; // true == 해당 아이템을 구매한 유저

    @JsonIgnore
    @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL)
    List<TalkComment> talkComments;
}
