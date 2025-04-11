package com.dashapp.model;

import com.dashapp.config.AppConfig;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final String dbUrl;
    
    public DatabaseManager() {
        this(AppConfig.DATABASE_PATH);
    }
    
    public DatabaseManager(String dbPath) {
        // Create parent directories if they don't exist
        File dbFile = new File(dbPath);
        if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
        
        this.dbUrl = "jdbc:sqlite:" + dbPath;
        initializeDatabase();
    }
    
    /**
     * Initialize the database and create tables if they don't exist
     */
    private void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create users table if it doesn't exist
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                         "username TEXT PRIMARY KEY, " +
                         "password TEXT, " +
                         "is_admin INTEGER" +
                         ")");

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS Project (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT,
                start_date TEXT,
                end_date TEXT,
                manager_id TEXT NOT NULL,
                FOREIGN KEY (manager_id) REFERENCES users(id)
            )""");

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS ProjectAssignment (
                assignment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                project_id TEXT NOT NULL,
                user_id INTEGER NOT NULL,
                FOREIGN KEY(project_id) REFERENCES Project(project_id) ON DELETE CASCADE,
                FOREIGN KEY(user_id) REFERENCES User(user_id) ON DELETE CASCADE,
                UNIQUE(project_id, user_id)
            )""");
            
            // Create other tables as needed
            // Check if admin user exists, create if it doesn't
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'")) {
                if (!rs.next()) {
                    // Admin user doesn't exist, create it
                    try (PreparedStatement pstmt = conn.prepareStatement(
                            "INSERT INTO users (username, password, is_admin) VALUES (?, ?, ?)")) {
                        pstmt.setString(1, "admin");
                        pstmt.setString(2, "admin");
                        pstmt.setInt(3, 1);
                        pstmt.executeUpdate();
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    /**
     * Get a database connection
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
    
    /**
     * Execute an update query (INSERT, UPDATE, DELETE)
     */
    public boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            // Execute the query
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute a query and process the results with a ResultSetProcessor
     */
    public <T> T executeQuery(String sql, ResultSetProcessor<T> processor, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            // Execute the query and process results
            try (ResultSet rs = pstmt.executeQuery()) {
                return processor.process(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Interface for processing a ResultSet into a specific type
     */
    public interface ResultSetProcessor<T> {
        T process(ResultSet rs) throws SQLException;
    }
}