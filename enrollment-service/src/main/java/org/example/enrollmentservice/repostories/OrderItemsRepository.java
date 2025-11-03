package org.example.enrollmentservice.repostories;

import org.example.enrollmentservice.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem,Long> {
}
