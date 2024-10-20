package com.goodlisteners.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Repository {
    private static final String URL = "jdbc:sqlite:goodlisteners.db";

    public void testConnection() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                System.out.println("Conexão com o SQLite foi estabelecida.");

                stmt = conn.createStatement();
                String createTestTableSQL = "CREATE TABLE IF NOT EXISTS TestTable (" +
                                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                        "name TEXT NOT NULL," +
                                        "age INTEGER NOT NULL);";
                stmt.execute(createTestTableSQL);
                System.out.println("Tabela TestTable criada ou já existe.");

                String insertTestSQL = "INSERT INTO TestTable (name, age) VALUES ('John Doe', 30);";
                stmt.execute(insertTestSQL);
                System.out.println("Inserção realizada com sucesso.");


                String selectTestSQL = "SELECT * FROM TestTable;";
                ResultSet rs = stmt.executeQuery(selectTestSQL);

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    System.out.println("ID: " + id + ", Nome: " + name + ", Idade: " + age);
                }

                String deleteTestSQL = "DROP TABLE IF EXISTS TestTable";
                stmt.execute(deleteTestSQL);
                System.out.println("Tabela teste deletada");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados ou executar SQL: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.close();
                    System.out.println("Conexão com o SQLite foi fechada.");
                }
            } catch (SQLException ex) {
                System.out.println("Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }
}
