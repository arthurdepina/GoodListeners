package com.goodlisteners.controllers;

import com.goodlisteners.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public ResponseEntity<?> submitReview(@RequestBody ReviewRequest request) {
        try {
            reviewService.submitReview(request.getUserId(), request.getAlbumId(), request.getRating());
            return ResponseEntity.ok("Avaliação enviada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar avaliação: " + e.getMessage());
        }
    }

    @GetMapping("/albums/{albumId}/reviews")
    public ResponseEntity<?> getAlbumReviews(@PathVariable int albumId) {
        try {
            List<Map<String, Object>> reviews = reviewService.getAlbumReviews(albumId);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar avaliações: " + e.getMessage());
        }
    }

    @GetMapping("/albums/{albumId}/average")
    public ResponseEntity<?> getAlbumAverage(@PathVariable int albumId) {
        try {
            double average = reviewService.getAlbumAverageRating(albumId);
            return ResponseEntity.ok(Map.of("average", average));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar média: " + e.getMessage());
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Server is running!");
    }
}