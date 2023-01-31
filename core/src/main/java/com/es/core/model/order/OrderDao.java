package com.es.core.model.order;

import java.util.Optional;

public interface OrderDao {

    Optional<Order> findById(Long id);

    Optional<Order> findBySecureId(String secureId);

    void save(Order order);

}
