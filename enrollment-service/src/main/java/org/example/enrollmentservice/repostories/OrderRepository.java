package org.example.enrollmentservice.repostories;

import org.example.enrollmentservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByStudentId(Long studentId);
}
