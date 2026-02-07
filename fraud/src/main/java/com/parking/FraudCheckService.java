package com.parking;

import org.springframework.stereotype.Service;

@Service
public record FraudCheckService(FraudCheckHistoryRepository fraudCheckHistoryRepository) {
    public Boolean isfraudulentCustomer(Integer customerId){
        FraudCheckHistory fraudulent = FraudCheckHistory.builder().
                customerId(customerId).
                isFrauder(false).
                build();
        fraudCheckHistoryRepository.save(fraudulent);
        return false;
    }
}
