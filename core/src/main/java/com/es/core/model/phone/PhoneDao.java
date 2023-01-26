package com.es.core.model.phone;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);

    void save(Phone phone);

    List<Phone> findAll(int offset, int limit);

    List<Phone> findPhonesInStock(String query, String sortField, String sortOrder, int offset, int limit);

    int getInStockQuantity(long phoneId);

    long getQuantityOfPhonesInStock(String query);
}
