package com.dashapp.model;

import com.dashapp.Main;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final DatabaseManager dbManager;
    private Map<String, User> userCache = new HashMap<>();
    
    public UserRepository() {
        this.dbManager = Main.getDatabaseManager();
        refreshUserCache();
    }
    
    /**
     * Refresh the user cache from the database
     */
    private void refreshUserCache() {
        userCache.clear();
        
        dbManager.executeQuery(
            "SELECT username, password, is_admin FROM users",
            rs -> {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    boolean isAdmin = rs.getInt("is_admin") == 1;
                    
                    User user = new User(username, password, isAdmin);
                    userCache.put(username, user);
                }
                return null;
            }
        );
    }

    public User getUserById(String userId) {
        return userCache.get(userId);
    }


    /**
     * Save a user to the repository
     */
    public void saveUser(User user) {
        boolean success = dbManager.executeUpdate(
            "INSERT OR REPLACE INTO users (username, password, is_admin) VALUES (?, ?, ?)",
            user.getUsername(),
            user.getPassword(),
            user.isAdmin() ? 1 : 0
        );
        
        if (success) {
            userCache.put(user.getUsername(), user);
        }
    }
    
    /**
     * Delete a user from the repository
     */
    public void deleteUser(String username) {
        User user = userCache.get(username);
        
        // Never delete admin users as a safety measure
        if (user != null && !user.isAdmin()) {
            boolean success = dbManager.executeUpdate(
                "DELETE FROM users WHERE username = ?",
                username
            );
            
            if (success) {
                userCache.remove(username);
            }
        }
    }
    
    /**
     * Get a user by username
     */
    public User getUser(String username) {
        return userCache.get(username);
    }
    
    /**
     * Check if a username exists
     */
    public boolean usernameExists(String username) {
        return userCache.containsKey(username);
    }
    
    /**
     * Get all users
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(userCache);
    }
}