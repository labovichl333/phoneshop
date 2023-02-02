package com.es.core.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> findById(Long id);

    Optional<Order> findBySecureId(String secureId);

    void save(Order order);

    List<Order> findAll();

    void updateStatus(long id, OrderStatus status);

}
