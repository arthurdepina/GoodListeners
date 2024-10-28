package com.goodlisteners.service;

import com.goodlisteners.repository.ReviewRepository;
import com.goodlisteners.repository.AlbumRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AlbumRepository albumRepository;

    public ReviewService(ReviewRepository reviewRepository, AlbumRepository albumRepository) {
        this.reviewRepository = reviewRepository;
        this.albumRepository = albumRepository;
    }

    public void submitReview(int userId, int albumId, int rating) {
        boolean isExistingReview = reviewRepository.hasReview(userId, albumId);

        if (isExistingReview) {
            reviewRepository.updateReview(userId, albumId, rating);
        } else {
            reviewRepository.insertReview(userId, albumId, rating);
        }
        
        double newAverage = reviewRepository.calculateNewAverage(albumId);
        albumRepository.updateAlbumAverage(albumId, newAverage);
    }
}
