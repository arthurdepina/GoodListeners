package com.goodlisteners.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlbumRepository {
    private static final String URL = "jdbc:sqlite:goodlisteners.db";

    public void updateAlbumAverage(int albumId, double newAverage) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Albums SET average_score = ? WHERE album_id = ?")) {
            stmt.setDouble(1, newAverage);
            stmt.setInt(2, albumId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar média do álbum: " + e.getMessage());
        }
    }
}
