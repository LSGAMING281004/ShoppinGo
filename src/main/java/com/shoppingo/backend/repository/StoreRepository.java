package com.shoppingo.backend.repository;

import com.shoppingo.backend.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByCategory(String category);

    List<Store> findByLocationContainingIgnoreCase(String location);

    List<Store> findTop10ByOrderByViewCountDesc();

    List<Store> findTop10ByOrderByAverageRatingDesc();

    boolean existsByName(String name);

    List<Store> findByOwnerId(String ownerId);
}
