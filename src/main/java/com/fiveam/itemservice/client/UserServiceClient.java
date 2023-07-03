package com.fiveam.itemservice.client;

import com.fiveam.itemservice.response.UserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users")
    UserInfoResponseDto getLoginUser();
}
