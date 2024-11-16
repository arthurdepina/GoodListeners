package com.goodlisteners.service;

import com.goodlisteners.repository.ReviewRepository;
import com.goodlisteners.repository.AlbumRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AlbumRepository albumRepository;

    public ReviewService(ReviewRepository reviewRepository, AlbumRepository albumRepository) {
        this.reviewRepository = reviewRepository;
        this.albumRepository = albumRepository;
    }

    public void submitReview(int userId, int albumId, int rating) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (albumId <= 0) {
            throw new IllegalArgumentException("Invalid album ID");
        }
        if (rating < 0 || rating > 100) {
            throw new IllegalArgumentException("Rating must be between 0 and 100");
        }

        if (albumRepository.findById(albumId).isEmpty()) {
            throw new IllegalArgumentException("Album not found");
        }

        boolean isExistingReview = reviewRepository.hasReview(userId, albumId);

        try {
            if (isExistingReview) {
                reviewRepository.updateReview(userId, albumId, rating);
            } else {
                reviewRepository.insertReview(userId, albumId, rating);
            }
            
            double newAverage = reviewRepository.calculateNewAverage(albumId);
            boolean updated = albumRepository.updateAlbumAverage(albumId, newAverage);
            
            if (!updated) {
                throw new RuntimeException("Failed to update album average");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing review: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getAlbumReviews(int albumId) {
        if (albumId <= 0) {
            throw new IllegalArgumentException("Invalid album ID");
        }

        if (albumRepository.findById(albumId).isEmpty()) {
            throw new IllegalArgumentException("Album not found");
        }

        return reviewRepository.getAlbumReviews(albumId);
    }

    public double getAlbumAverageRating(int albumId) {
        if (albumId <= 0) {
            throw new IllegalArgumentException("Invalid album ID");
        }

        return reviewRepository.calculateNewAverage(albumId);
    }
}