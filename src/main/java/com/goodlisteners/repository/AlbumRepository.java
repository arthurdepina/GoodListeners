package com.goodlisteners.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class AlbumRepository {
    private final DataSource dataSource;

    @Autowired
    public AlbumRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void updateAlbumAverage(int albumId, double newAverage) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Albums SET average_score = ? WHERE album_id = ?")) {
            stmt.setDouble(1, newAverage);
            stmt.setInt(2, albumId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating album average: " + e.getMessage(), e);
        }
    }
}