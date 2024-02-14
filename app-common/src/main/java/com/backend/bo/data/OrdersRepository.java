package com.backend.bo.data;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    Page<OrdersEntity> findAll(Pageable pageable);

    OrdersEntity findByOrderId(String orderId);
}
