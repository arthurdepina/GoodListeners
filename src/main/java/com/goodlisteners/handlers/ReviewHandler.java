package com.goodlisteners.handlers;

import com.goodlisteners.service.ReviewService;

public class ReviewHandler {
    private final ReviewService reviewService;

    public ReviewHandler(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void handleReview(int userId, int albumId, int rating) {

        if (rating < 0 || rating > 100) {
            System.out.println("Nota inválida! Deve estar entre 0 e 100.");
            return;
        }
        
        reviewService.submitReview(userId, albumId, rating);
    }
}
