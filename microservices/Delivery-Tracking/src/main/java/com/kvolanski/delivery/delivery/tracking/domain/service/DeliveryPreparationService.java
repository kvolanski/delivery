package com.kvolanski.delivery.delivery.tracking.domain.service;

import com.kvolanski.delivery.delivery.tracking.api.model.ContactPointInput;
import com.kvolanski.delivery.delivery.tracking.api.model.DeliveryInput;
import com.kvolanski.delivery.delivery.tracking.api.model.ItemInput;
import com.kvolanski.delivery.delivery.tracking.domain.exception.DomainException;
import com.kvolanski.delivery.delivery.tracking.domain.model.ContactPoint;
import com.kvolanski.delivery.delivery.tracking.domain.model.Delivery;
import com.kvolanski.delivery.delivery.tracking.domain.repository.DeliveryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryPreparationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public Delivery draft(DeliveryInput input){
        Delivery delivery = Delivery.draft();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    @Transactional
    public Delivery edit(UUID deliveryId, DeliveryInput input){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new DomainException());
        delivery.removeItems();
        handlePreparation(input, delivery);
        return deliveryRepository.saveAndFlush(delivery);
    }

    private void handlePreparation(DeliveryInput input, Delivery delivery) {
        ContactPointInput senderInput = input.getSender();
        ContactPointInput recipientInput = input.getRecipient();

        ContactPoint sender = ContactPoint.builder()
                .phone(senderInput.getPhone())
                .name(senderInput.getName())
                .complement(senderInput.getComplement())
                .number(senderInput.getNumber())
                .zipCode(senderInput.getZipCode())
                .street(senderInput.getStreet())
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .phone(recipientInput.getPhone())
                .name(recipientInput.getName())
                .complement(recipientInput.getComplement())
                .number(recipientInput.getNumber())
                .zipCode(recipientInput.getZipCode())
                .street(recipientInput.getStreet())
                .build();

        Duration expectedDeliveryTime = Duration.ofHours(3);
        BigDecimal payout = new BigDecimal("10");

        var preparationDetails = Delivery.PreparationDetails.builder()
                .recipient(recipient)
                .sender(sender)
                .expectedDeliveryTime(expectedDeliveryTime)
                .courierPayout(payout)
                .distanceFee(new BigDecimal("10"))
                .build();

        delivery.editPreparationDetails(preparationDetails);

        for (ItemInput itemInput : input.getItems()) {
            delivery.addItem(itemInput.getName(), itemInput.getQuantity());
        }

    }

}
