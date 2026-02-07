package com.parking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("api/v1/fraud")
public record FraudCheckController(FraudCheckService fraudCheckService) {

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId){
        boolean isfraudulentCustomer = fraudCheckService.isfraudulentCustomer(customerId);
        return new FraudCheckResponse(isfraudulentCustomer);
    }

}
