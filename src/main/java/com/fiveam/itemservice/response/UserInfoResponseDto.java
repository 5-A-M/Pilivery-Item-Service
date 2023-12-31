package com.fiveam.itemservice.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Builder
public class UserInfoResponseDto implements Serializable {
    private Long id;

    private String email;

    private String displayName;

    private String address;

    private String detailAddress;

    private String realName;

    private String phone;

    private String password;

    private Long cartId;

    private boolean social;

    private String sid;

    private ZonedDateTime updatedAt;
}
