package com.shoppingo.backend.service;

import com.shoppingo.backend.model.Review;
import com.shoppingo.backend.model.Store;
import com.shoppingo.backend.repository.ReviewRepository;
import com.shoppingo.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public Review addReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        updateStoreRating(review.getStoreId());
        return savedReview;
    }

    private void updateStoreRating(Long storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        if (reviews.isEmpty())
            return;

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Store store = storeRepository.findById(storeId).orElse(null);
        if (store != null) {
            store.setAverageRating(average);
            // In a real app, you might want a reviewCount field in Store as well
            storeRepository.save(store);
        }
    }
}
