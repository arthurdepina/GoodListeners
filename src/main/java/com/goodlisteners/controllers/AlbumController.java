package com.goodlisteners.controllers;

import com.goodlisteners.model.Album;
import com.goodlisteners.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class AlbumController {
    
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable int id) {
        return albumService.getAlbumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // For testing purposes
    @GetMapping("/albums/ping")
    public ResponseEntity<String> pingAlbums() {
        return ResponseEntity.ok("Albums endpoint is working!");
    }
}