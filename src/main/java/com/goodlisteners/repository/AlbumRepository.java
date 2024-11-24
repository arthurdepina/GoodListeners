package com.goodlisteners.repository;

import com.goodlisteners.model.Album;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AlbumRepository {
    private final DataSource dataSource;

    @Autowired
    public AlbumRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Album> findById(int albumId) {
        String sql = "SELECT * FROM Albums WHERE album_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, albumId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Album album = new Album();
                    album.setAlbumId(rs.getInt("album_id"));
                    album.setName(rs.getString("name"));
                    album.setArtist(rs.getString("artist"));
                    album.setGenre(rs.getString("genre"));
                    album.setYear(rs.getInt("year"));
                    album.setLength(rs.getInt("length"));
                    album.setCoverUrl(rs.getString("cover_url"));
                    album.setAverageScore(rs.getDouble("average_score"));
                    album.setNumberFavorites(rs.getInt("number_favorites"));
                    album.setSpotifyId(rs.getString("spotify_id"));
                    return Optional.of(album);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding album: " + e.getMessage(), e);
        }
    }

    public List<Album> findAll() {
        String sql = "SELECT * FROM Albums";
        List<Album> albums = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setName(rs.getString("name"));
                album.setArtist(rs.getString("artist"));
                album.setGenre(rs.getString("genre"));
                album.setYear(rs.getInt("year"));
                album.setLength(rs.getInt("length"));
                album.setCoverUrl(rs.getString("cover_url"));
                album.setAverageScore(rs.getDouble("average_score"));
                album.setNumberFavorites(rs.getInt("number_favorites"));
                album.setSpotifyId(rs.getString("spotify_id"));

                albums.add(album);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving albums: " + e.getMessage(), e);
        }

        return albums;
    }

    public boolean updateAlbumAverage(int albumId, double newAverage) {
        String sql = "UPDATE Albums SET average_score = ROUND(?, 1) WHERE album_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            newAverage = Math.min(100, Math.max(0, newAverage));
            
            stmt.setDouble(1, newAverage);
            stmt.setInt(2, albumId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating album average: " + e.getMessage(), e);
        }
    }
}