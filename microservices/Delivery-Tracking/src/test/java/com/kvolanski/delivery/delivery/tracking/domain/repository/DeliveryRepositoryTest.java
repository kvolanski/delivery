package com.kvolanski.delivery.delivery.tracking.domain.repository;

import com.kvolanski.delivery.delivery.tracking.domain.model.ContactPoint;
import com.kvolanski.delivery.delivery.tracking.domain.model.Delivery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void shouldPersist(){
        Delivery delivery = Delivery.draft();

        delivery.editPreparationDetails(createdValidPreparationDetails());
        delivery.addItem("PC", 3);
        delivery.addItem("Notebook", 5);

        deliveryRepository.saveAndFlush(delivery);

        Delivery persistedDelivery = deliveryRepository.findById(delivery.getId()).orElseThrow();

        assertEquals(2, persistedDelivery.getItems().size());

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