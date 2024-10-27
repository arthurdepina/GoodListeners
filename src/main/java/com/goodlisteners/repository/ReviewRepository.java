package com.goodlisteners.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRepository {
    private static final String URL = "jdbc:sqlite:goodlisteners.db";

    public boolean hasReview(int userId, int albumId) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Reviews WHERE user_id = ? AND album_id = ?")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, albumId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao verificar avaliação: " + e.getMessage());
            return false;
        }
    }

    public void insertReview(int userId, int albumId, int rating) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reviews (user_id, album_id, rating) VALUES (?, ?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, albumId);
            stmt.setInt(3, rating);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir avaliação: " + e.getMessage());
        }
    }

    public void updateReview(int userId, int albumId, int rating) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Reviews SET rating = ? WHERE user_id = ? AND album_id = ?")) {
            stmt.setInt(1, rating);
            stmt.setInt(2, userId);
            stmt.setInt(3, albumId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar avaliação: " + e.getMessage());
        }
    }

    public double calculateNewAverage(int albumId) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT AVG(rating) AS average_rating FROM Reviews WHERE album_id = ?")) {
            stmt.setInt(1, albumId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average_rating");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular média: " + e.getMessage());
        }
        return 0.0;
    }
}
