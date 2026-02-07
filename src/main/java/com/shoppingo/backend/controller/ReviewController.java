package com.shoppingo.backend.controller;

import com.shoppingo.backend.model.Review;
import com.shoppingo.backend.repository.ReviewRepository;
import com.shoppingo.backend.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/store/{storeId}")
    public List<Review> getReviewsByStore(@PathVariable Long storeId) {
        return reviewRepository.findByStoreId(storeId);
    }

    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Review review, HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        String email = (String) request.getAttribute("email");

        if (uid == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in to rate and comment");
        }

        review.setUserId(uid);
        if (review.getUserName() == null || review.getUserName().isEmpty()) {
            review.setUserName(email);
        }

        try {
            Review savedReview = reviewService.addReview(review);
            return ResponseEntity.ok(savedReview);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving review: " + e.getMessage());
        }
    }
}
