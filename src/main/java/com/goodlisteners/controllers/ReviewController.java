package com.goodlisteners.controllers;

import com.goodlisteners.handlers.ReviewHandler;
import com.goodlisteners.controllers.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewHandler reviewHandler;

    @Autowired
    public ReviewController(ReviewHandler reviewHandler) {
        this.reviewHandler = reviewHandler;
    }

    @PostMapping("/review")
    public ResponseEntity<String> submitReview(@RequestBody ReviewRequest request) {
        // Passa a avaliação para o ReviewHandler
        reviewHandler.handleReview(request.getUserId(), request.getAlbumId(), request.getRating());
        return ResponseEntity.ok("Avaliação enviada com sucesso!");
    }
}
