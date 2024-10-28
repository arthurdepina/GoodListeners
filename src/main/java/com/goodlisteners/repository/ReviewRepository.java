package com.goodlisteners.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ReviewRepository {
    private final DataSource dataSource;

    @Autowired
    public ReviewRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean hasReview(int userId, int albumId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Reviews WHERE user_id = ? AND album_id = ?")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, albumId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking review existence: " + e.getMessage(), e);
        }
    }

    public void insertReview(int userId, int albumId, int rating) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reviews (user_id, album_id, rating) VALUES (?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, albumId);
            stmt.setInt(3, rating);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting review: " + e.getMessage(), e);
        }
    }

    public void updateReview(int userId, int albumId, int rating) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Reviews SET rating = ? WHERE user_id = ? AND album_id = ?")) {
            stmt.setInt(1, rating);
            stmt.setInt(2, userId);
            stmt.setInt(3, albumId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating review: " + e.getMessage(), e);
        }
    }

    public double calculateNewAverage(int albumId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT AVG(rating) AS average_rating FROM Reviews WHERE album_id = ?")) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average_rating");
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating average: " + e.getMessage(), e);
        }
    }
}