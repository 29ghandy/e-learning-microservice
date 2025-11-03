package org.example.enrollmentservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import org.example.enrollmentservice.enums.Status;

@Entity
@Table(name = "orders") // avoid reserved keyword conflict
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalPrice;

    private Long studentId;
    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    // Bidirectional one-to-one with Enrollment (mappedBy = "order")
    // This side is the inverse side. Cascade here is optional depending on which side you save.
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Enrollment enrollment;

    // One-to-many with OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}