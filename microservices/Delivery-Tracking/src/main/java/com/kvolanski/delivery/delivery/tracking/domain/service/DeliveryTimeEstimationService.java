package com.kvolanski.delivery.delivery.tracking.domain.service;

import com.kvolanski.delivery.delivery.tracking.domain.model.ContactPoint;

public interface DeliveryTimeEstimationService {
    DeliveryEstimate estimate(ContactPoint sender, ContactPoint receiver);
}
