package com.kvolanski.delivery.delivery.tracking.infrastructure.fake;

import com.kvolanski.delivery.delivery.tracking.domain.model.ContactPoint;
import com.kvolanski.delivery.delivery.tracking.domain.service.DeliveryEstimate;
import com.kvolanski.delivery.delivery.tracking.domain.service.DeliveryTimeEstimationService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DeliveryTimeEstimationServiceFakeImp implements DeliveryTimeEstimationService {
    @Override
    public DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver) {
        return new DeliveryEstimate(Duration.ofHours(3), 3.1 );
    }
}
