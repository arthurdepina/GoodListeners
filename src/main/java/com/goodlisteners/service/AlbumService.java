package com.goodlisteners.service;

import com.goodlisteners.model.Album;
import com.goodlisteners.repository.AlbumRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Optional<Album> getAlbumById(int albumId) {
        if (albumId <= 0) {
            return Optional.empty();
        }
        return albumRepository.findById(albumId);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
}