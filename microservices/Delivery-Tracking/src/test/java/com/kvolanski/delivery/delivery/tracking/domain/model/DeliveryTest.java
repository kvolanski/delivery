package com.kvolanski.delivery.delivery.tracking.domain.model;

import com.kvolanski.delivery.delivery.tracking.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {

    @Test
    public void shouldChangeToPlaced(){
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());

        delivery.place();

        assertEquals(DeliveryStatus.WAITING_FOR_COURIER, delivery.getStatus());
        assertNotNull(delivery.getPlaceAt());

    }

    @Test
    public void shouldNotChangeToPlaced(){
        Delivery delivery = Delivery.draft();

        assertThrows(DomainException.class,() -> {
            delivery.place();
        });

        assertEquals(DeliveryStatus.DRAFT, delivery.getStatus());
        assertNull(delivery.getPlaceAt());

    }

    private Delivery.PreparationDetails createdValidPreparationDetails() {
        ContactPoint sender = ContactPoint.builder()
                .zipCode("83330290")
                .street("Rua Gerivaldo")
                .number("100")
                .complement("Casa 1")
                .name("João Silva")
                .phone("41 987245667")
                .build();

        ContactPoint recipient = ContactPoint.builder()
                .zipCode("321334665")
                .street("Rua Espanha")
                .number("232")
                .complement("")
                .name("Marina Silva")
                .phone("41 955245467")
                .build();

        return Delivery.PreparationDetails.builder()
                .sender(sender)
                .recipient(recipient)
                .distanceFee(new BigDecimal(15.00))
                .courierPayout(new BigDecimal(5.00))
                .expectedDeliveryTime(Duration.ofHours(5))
                .build();

    }

}