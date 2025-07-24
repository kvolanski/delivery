package com.kvolanski.delivery.delivery.tracking.api.controller;

import com.kvolanski.delivery.delivery.tracking.api.model.DeliveryInput;
import com.kvolanski.delivery.delivery.tracking.domain.model.Delivery;
import com.kvolanski.delivery.delivery.tracking.domain.service.DeliveryPreparationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryPreparationService deliveryPreparationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery draft(@RequestBody @Valid DeliveryInput deliveryInput){
        return deliveryPreparationService.draft(deliveryInput);
    }

    @PutMapping("/{deliveryId}")
    public Delivery edit(@PathVariable UUID deliveryId, @RequestBody @Valid DeliveryInput deliveryInput){
        return deliveryPreparationService.edit(deliveryId, deliveryInput);
    }

}
