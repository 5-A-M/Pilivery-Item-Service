package com.fiveam.searchservice.client;

import com.fiveam.searchservice.response.ItemOrderInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderServiceClient {

    @GetMapping("/orders/item-orders/{itemOrderId}")
    ItemOrderInfoResponseDto findItemOrder(@PathVariable Long itemOrderId);

    @GetMapping("/orders/check/{itemId}/{userId}")
    Boolean isShopper(@PathVariable Long itemId, @PathVariable Long userId);
}
