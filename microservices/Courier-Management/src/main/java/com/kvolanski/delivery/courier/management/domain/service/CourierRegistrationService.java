package com.kvolanski.delivery.courier.management.domain.service;

import com.kvolanski.delivery.courier.management.api.model.CourierInput;
import com.kvolanski.delivery.courier.management.domain.model.Courier;
import com.kvolanski.delivery.courier.management.domain.repository.CourierRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CourierRegistrationService {

    private final CourierRepository courierRepository;
    
    public Courier create(@Valid CourierInput input) {
        Courier courier = Courier.brandNew(input.getName(), input.getPhone());
        return courierRepository.saveAndFlush(courier);
    }


    public Courier update(UUID courierId, @Valid CourierInput input) {
        Courier courier = courierRepository.findById(courierId).orElseThrow();
        courier.setPhone(input.getPhone());
        courier.setName(input.getName());
        return courierRepository.saveAndFlush(courier);
    }
}
