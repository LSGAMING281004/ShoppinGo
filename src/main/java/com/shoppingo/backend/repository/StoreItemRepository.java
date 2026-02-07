package com.shoppingo.backend.repository;

import com.shoppingo.backend.model.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {
    List<StoreItem> findByStoreId(Long storeId);

    void deleteByStoreId(Long storeId);
}
