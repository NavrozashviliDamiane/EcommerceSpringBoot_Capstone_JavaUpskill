package com.damiane.orderservice.external.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(name = "ACCOUNT-SERVICE/accounts")
public interface AccountService {
    @PutMapping("/addOrdersId/{accountId}")
    public ResponseEntity<String> addOrderIdsToAccount(
            @PathVariable("accountId") Long accountId,
            @RequestParam Long orderIds
    );
}
