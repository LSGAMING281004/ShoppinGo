package com.shoppingo.backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.shoppingo.backend.dto.StoreRequest;
import com.shoppingo.backend.model.Store;
import com.shoppingo.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public Store createStore(StoreRequest request) throws ExecutionException, InterruptedException {
        // 1. Save to MySQL
        Store store = new Store();
        store.setName(request.getName());
        store.setCategory(request.getCategory());
        store.setLocation(request.getLocation());
        store.setOwnerId(request.getOwnerId());
        store.setImageUrl(request.getImageUrl());
        store.setDescription(request.getDescription()); // Saving description to MySQL too for basic view

        Store savedStore = storeRepository.save(store);

        // 2. Save detailed info to Firestore
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("storeId", savedStore.getId());
        docData.put("name", request.getName());
        docData.put("contactEmail", request.getContactEmail());
        docData.put("contactMobile", request.getContactMobile());
        docData.put("openTime", request.getOpenTime());
        docData.put("closeTime", request.getCloseTime());
        docData.put("paymentMethods", request.getPaymentMethods());
        docData.put("fullDescription", request.getDescription());

        // Use Store ID as Document ID for easy lookup
        ApiFuture<WriteResult> future = db.collection("stores").document(String.valueOf(savedStore.getId()))
                .set(docData);
        future.get(); // Wait for result

        return savedStore;
    }

    public Store updateStore(Long id, StoreRequest request) throws ExecutionException, InterruptedException {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Update core fields
        store.setName(request.getName());
        store.setCategory(request.getCategory());
        store.setLocation(request.getLocation());
        store.setImageUrl(request.getImageUrl());
        store.setDescription(request.getDescription());
        store.setLocationUrl(request.getLocationUrl());
        store.setWebsiteUrl(request.getWebsiteUrl());
        store.setOpenTime(request.getOpenTime());
        store.setCloseTime(request.getCloseTime());
        store.setDaysOpen(request.getDaysOpen());

        // Update items (Clear and re-add for simplicity)
        store.getItems().clear();
        if (request.getItems() != null) {
            for (StoreRequest.StoreItemRequest itemReq : request.getItems()) {
                com.shoppingo.backend.model.StoreItem item = new com.shoppingo.backend.model.StoreItem(
                        itemReq.getName(),
                        itemReq.getPrice(),
                        store);
                store.getItems().add(item);
            }
        }

        Store savedStore = storeRepository.save(store);

        // Update Firestore
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("storeId", savedStore.getId());
        docData.put("name", request.getName());
        docData.put("contactEmail", request.getContactEmail());
        docData.put("contactMobile", request.getContactMobile());
        docData.put("openTime", request.getOpenTime());
        docData.put("closeTime", request.getCloseTime());
        docData.put("paymentMethods", request.getPaymentMethods());
        docData.put("fullDescription", request.getDescription());
        docData.put("locationUrl", request.getLocationUrl());
        docData.put("websiteUrl", request.getWebsiteUrl());
        docData.put("daysOpen", request.getDaysOpen());

        db.collection("stores").document(String.valueOf(savedStore.getId())).set(docData).get();

        return savedStore;
    }
}
