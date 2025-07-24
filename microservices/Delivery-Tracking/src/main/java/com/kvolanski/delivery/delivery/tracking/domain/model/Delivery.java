package com.kvolanski.delivery.delivery.tracking.domain.model;

import com.kvolanski.delivery.delivery.tracking.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Delivery {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private UUID courierId;

    private DeliveryStatus status;

    private OffsetDateTime placeAt;
    private OffsetDateTime assignAt;
    private OffsetDateTime expectedDeliveryAt;
    private OffsetDateTime fulfilledAt;

    private BigDecimal distanceFee;
    private BigDecimal courierPayout;
    private BigDecimal totalCost;

    private Integer totalItems;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "sender_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
            @AttributeOverride(name = "number", column = @Column(name = "sender_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "sender_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "sender_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "sender_phone"))
    })
    private ContactPoint sender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "recipient_zip_code")),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street")),
            @AttributeOverride(name = "number", column = @Column(name = "recipient_number")),
            @AttributeOverride(name = "complement", column = @Column(name = "recipient_complement")),
            @AttributeOverride(name = "name", column = @Column(name = "recipient_name")),
            @AttributeOverride(name = "phone", column = @Column(name = "recipient_phone"))
    })
    private ContactPoint recipient;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "delivery")
    private List<Item> items = new ArrayList<>();


    public static Delivery draft(){
         Delivery delivery = new Delivery();
         delivery.setId(UUID.randomUUID());
         delivery.setStatus(DeliveryStatus.DRAFT);
         delivery.setTotalItems(0);
         delivery.setTotalCost(BigDecimal.ZERO);
         delivery.setCourierPayout(BigDecimal.ZERO);
         delivery.setDistanceFee(BigDecimal.ZERO);
         return delivery;
    }

    public void removeItem(UUID itemId){
        items.removeIf(item -> item.getId().equals(itemId));
        calculateTotamItems();
    }

    public void removeItems(){
        items.clear();
        calculateTotamItems();
    }

    public void editPreparationDetails(PreparationDetails details){
        verifyIfCanEdited();
        setSender(details.getSender());
        setRecipient(details.getRecipient());
        setDistanceFee(details.getDistanceFee());
        setCourierPayout(details.getCourierPayout());

        setExpectedDeliveryAt(OffsetDateTime.now().plus(details.getExpectedDeliveryTime()));
        setTotalCost(this.getDistanceFee().add(this.getCourierPayout()));
    }

    public void place(){
        verifyIfCanPlaced();
        this.changeStatusTo(DeliveryStatus.WAITING_FOR_COURIER);
        this.setPlaceAt(OffsetDateTime.now());
    }

    public void pickUp(UUID courierId){
        this.setCourierId(courierId);
        this.changeStatusTo(DeliveryStatus.IN_TRANSIT);
        this.setAssignAt(OffsetDateTime.now());
    }

    public void markAsDelivered(){
        this.changeStatusTo(DeliveryStatus.DELIVERED);
        this.setFulfilledAt(OffsetDateTime.now());
    }

    public void changeItemQuantity(UUID itemId, int quantity){
        Item item = getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow();

        item.setQuantity(quantity);
        calculateTotamItems();
    }

    public UUID addItem(String name, int quantity){
        Item item = Item.brandNew(name, quantity, this);
        items.add(item);
        calculateTotamItems();
        return item.getId();
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    private void calculateTotamItems(){
        int totalItems = items.stream().mapToInt(Item::getQuantity).sum();
        setTotalItems(totalItems);
    }

    private void verifyIfCanPlaced(){
        if (!isFilled()){
            throw new DomainException();
        }
        if (!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

    private void verifyIfCanEdited(){
        if (!getStatus().equals(DeliveryStatus.DRAFT)){
            throw new DomainException();
        }
    }

    private boolean isFilled(){
        return this.getSender() != null && this.getRecipient() != null && this.totalCost != null;
    }

    private void changeStatusTo(DeliveryStatus newStatus) throws DomainException {
        if (newStatus != null && this.getStatus().canNotChangeTo(newStatus)){
            throw new DomainException("Invalid status transition from " + this.getStatus() + " to " + newStatus);
        }
        this.setStatus(newStatus);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class PreparationDetails{
        private ContactPoint sender;
        private ContactPoint recipient;
        private BigDecimal distanceFee;
        private BigDecimal courierPayout;
        private Duration expectedDeliveryTime;

    }


}
