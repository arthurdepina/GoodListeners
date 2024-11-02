package com.goodlisteners.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;

@Configuration
public class DatabaseConfig {
    
    @Value("${DATABASE_PATH:/data/goodlisteners.db}")
    private String databasePath;
    
    @Bean
    public DataSource dataSource() {
        // Ensure the directory exists
        File dbFile = new File(databasePath);
        dbFile.getParentFile().mkdirs();
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + databasePath);
        return dataSource;
    }
}