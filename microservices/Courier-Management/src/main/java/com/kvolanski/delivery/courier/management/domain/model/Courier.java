package com.kvolanski.delivery.courier.management.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PRIVATE)
public class Courier {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter(AccessLevel.PUBLIC)
    private String name;

    @Setter(AccessLevel.PUBLIC)
    private String phone;

    private Integer fulFilledDeliveriesQuantity;

    private Integer pendingDeliviriesQuantity;

    private OffsetDateTime lastFulfilledDeliveryAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "courier")
    private List<AssignedDelivery> pendingDeliveries = new ArrayList<>();

    public List<AssignedDelivery> getPendingDeliveries(){
        return Collections.unmodifiableList(this.pendingDeliveries);
    }
    
    public static Courier brandNew(String name, String phone){
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID());
        courier.setName(name);
        courier.setPhone(phone);
        courier.setPendingDeliviriesQuantity(0);
        courier.setFulFilledDeliveriesQuantity(0);
        return courier;
    }

    public void assign(UUID deliveryId){
        this.getPendingDeliveries().add(AssignedDelivery.pending(deliveryId, this));
        this.pendingDeliviriesQuantity++;
    }

    public void fulfill(UUID deliveryId){
        AssignedDelivery delivery = this.pendingDeliveries.stream().filter(d -> d.getId().equals(deliveryId)).findFirst().orElseThrow();
        this.pendingDeliveries.remove(delivery);

        this.pendingDeliviriesQuantity--;
        this.fulFilledDeliveriesQuantity++;
        this.lastFulfilledDeliveryAt = OffsetDateTime.now();

    }

}
