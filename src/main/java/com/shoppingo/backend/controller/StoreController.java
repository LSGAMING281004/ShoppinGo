package com.shoppingo.backend.controller;

import com.shoppingo.backend.dto.StoreRequest;
import com.shoppingo.backend.model.Store;
import com.shoppingo.backend.repository.StoreRepository;
import com.shoppingo.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getOwnerStores(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: No user found");
        }
        List<Store> userStores = storeRepository.findByOwnerId(uid);
        return ResponseEntity.ok(userStores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable Long id) {
        return storeRepository.findById(id)
                .map(store -> {
                    Long currentViews = store.getViewCount();
                    store.setViewCount((currentViews == null ? 0 : currentViews) + 1);
                    storeRepository.save(store);
                    return ResponseEntity.ok(store);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody StoreRequest request) {
        if (storeRepository.existsByName(request.getName())) {
            return ResponseEntity.badRequest().body("Store name already exists");
        }
        try {
            Store savedStore = storeService.createStore(request);
            return ResponseEntity.ok(savedStore);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating store: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStore(@PathVariable Long id, @RequestBody StoreRequest request,
            HttpServletRequest servletRequest) {
        String uid = (String) servletRequest.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in");
        }

        return storeRepository.findById(id)
                .map(store -> {
                    if (!store.getOwnerId().equals(uid)) {
                        return ResponseEntity.status(403).body("Forbidden: You don't own this store");
                    }
                    try {
                        Store updatedStore = storeService.updateStore(id, request);
                        return ResponseEntity.ok(updatedStore);
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError().body("Error updating store: " + e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id, HttpServletRequest servletRequest) {
        String uid = (String) servletRequest.getAttribute("uid");
        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in");
        }

        return storeRepository.findById(id)
                .map(store -> {
                    if (!store.getOwnerId().equals(uid)) {
                        return ResponseEntity.status(403).body("Forbidden: You don't own this store");
                    }
                    storeRepository.delete(store);
                    return ResponseEntity.ok("Store deleted successfully");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
