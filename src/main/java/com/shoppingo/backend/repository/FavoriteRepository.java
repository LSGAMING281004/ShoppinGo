package com.shoppingo.backend.repository;

import com.shoppingo.backend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(String userId);

    Optional<Favorite> findByUserIdAndStoreId(String userId, Long storeId);

    boolean existsByUserIdAndStoreId(String userId, Long storeId);

    void deleteByUserIdAndStoreId(String userId, Long storeId);
}
