package com.kvolanski.delivery.delivery.tracking.domain.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface CourierPayoutCalculationService {
    BigDecimal calculatePayout(Double distanceInKm);

}
