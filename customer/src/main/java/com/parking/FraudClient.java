package com.parking;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "fraud")
public interface FraudClient{
    @GetMapping("/api/v1/fraud/{customerId}")
    FraudCheckResponse checkFraud(@PathVariable("customerId") Integer customerId);
}