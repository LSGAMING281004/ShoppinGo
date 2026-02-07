package com.shoppingo.backend.controller;

import com.shoppingo.backend.model.Favorite;
import com.shoppingo.backend.model.Store;
import com.shoppingo.backend.repository.FavoriteRepository;
import com.shoppingo.backend.repository.StoreRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public ResponseEntity<?> getUserFavorites(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in");
        }

        List<Favorite> favorites = favoriteRepository.findByUserId(uid);
        List<Long> storeIds = favorites.stream().map(Favorite::getStoreId).collect(Collectors.toList());
        List<Store> userFavoriteStores = storeRepository.findAllById(storeIds);

        return ResponseEntity.ok(userFavoriteStores);
    }

    @GetMapping("/ids")
    public ResponseEntity<?> getUserFavoriteIds(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.ok(List.of());
        }

        List<Favorite> favorites = favoriteRepository.findByUserId(uid);
        List<Long> storeIds = favorites.stream().map(Favorite::getStoreId).collect(Collectors.toList());

        return ResponseEntity.ok(storeIds);
    }

    @PostMapping("/toggle/{storeId}")
    @Transactional
    public ResponseEntity<?> toggleFavorite(@PathVariable Long storeId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in to favorite stores");
        }

        if (favoriteRepository.existsByUserIdAndStoreId(uid, storeId)) {
            favoriteRepository.deleteByUserIdAndStoreId(uid, storeId);
            return ResponseEntity.ok("Removed from favorites");
        } else {
            Favorite favorite = new Favorite(uid, storeId);
            favoriteRepository.save(favorite);
            return ResponseEntity.ok("Added to favorites");
        }
    }
}
