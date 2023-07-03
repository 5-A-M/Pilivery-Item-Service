package com.fiveam.itemservice.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalkResponseDto {

    private long talkId;
    private long itemId;
    private long userId;
    private String displayName;
    private String content;
    private boolean shopper;
    private String createdAt;
    private String updatedAt;
}
