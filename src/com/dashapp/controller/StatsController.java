package com.dashapp.controller;

import java.util.HashMap;

import com.dashapp.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.dashapp.model.User;
import com.dashapp.model.UserRepository;
import com.dashapp.view.ViewNavigator;
import java.lang.StringBuilder;
import java.util.Map;

public class StatsController {

    @FXML
    private Label statsLabel;

    @FXML
    private Label statsContentLabel;
    
    @FXML
    private VBox userManagementSection;
    
    @FXML
    private VBox userListContainer;

    private UserRepository userRepository;
    private String currentUsername;
    
    @FXML
    public void initialize() {
        userRepository = Main.getUserRepository();
        User user = ViewNavigator.getAuthenticatedUser();

        if (user == null)
            return;
        
        if (user.isAdmin()) {
            // For admin users, show the user management interface
            statsLabel.setText("Admin Dashboard");
            statsContentLabel.setText("Welcome to the admin dashboard. You can manage users below.");
            
            // Make user management section visible
            userManagementSection.setVisible(true);
            userManagementSection.setManaged(true);
            
            // Populate the user list
            populateUserList();
        } else {
            // For regular users, just show the "coming soon" message
            statsLabel.setText("Stats");
            statsContentLabel.setText("Coming soon...");
        }
    }
    
    /**
     * Populate the user list with non-admin users
     */
    private void populateUserList() {
        // Clear the container first
        userListContainer.getChildren().clear();
        
        // Get all users
        Map<String, User> users = userRepository.getAllUsers();
        
        // Add a header row
        HBox headerRow = createHeaderRow();
        userListContainer.getChildren().add(headerRow);
        
        // Check if there are any non-admin users
        boolean hasNonAdminUsers = false;
        
        // Add a row for each non-admin user
        for (User user : users.values()) {
            // Skip admin users (they should not appear in this list)
            if (user.isAdmin()) {
                continue;
            }
            
            hasNonAdminUsers = true;
            HBox userRow = createUserRow(user);
            userListContainer.getChildren().add(userRow);
        }
        
        // If no non-admin users were found, add a message
        if (!hasNonAdminUsers) {
            Label noUsersLabel = new Label("No regular users found.");
            userListContainer.getChildren().add(noUsersLabel);
        }
    }
    
    /**
     * Create a header row for the user list
     */
    private HBox createHeaderRow() {
        HBox headerRow = new HBox();
        headerRow.setSpacing(10);
        headerRow.setPadding(new Insets(5));
        headerRow.setStyle("-fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0; -fx-padding: 5 5 10 5;");
        
        Label usernameHeader = new Label("Username");
        usernameHeader.setStyle("-fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label actionsHeader = new Label("Actions");
        actionsHeader.setStyle("-fx-font-weight: bold;");
        
        headerRow.getChildren().addAll(usernameHeader, spacer, actionsHeader);
        
        return headerRow;
    }
    
    /**
     * Create a row for a user in the list
     */
    private HBox createUserRow(User user) {
        HBox userRow = new HBox();
        userRow.setSpacing(10);
        userRow.setPadding(new Insets(5));
        userRow.setStyle("-fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        
        // Username label
        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setPrefWidth(150);
        
        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Make Admin button
        Button makeAdminButton = new Button("Make Admin");
        makeAdminButton.setOnAction(e -> handleMakeAdmin(user.getUsername()));
        
        // Delete button
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("button-secondary");
        deleteButton.setOnAction(e -> handleDeleteUser(user.getUsername()));
        
        userRow.getChildren().addAll(usernameLabel, spacer, makeAdminButton, deleteButton);
        
        return userRow;
    }
    
    /**
     * Handle making a user an admin
     */
    private void handleMakeAdmin(String username) {
        User userToPromote = userRepository.getUser(username);
        if (userToPromote != null) {
            // Create a new user with admin privileges
            User promotedUser = new User(username, userToPromote.getPassword(), true);
            userRepository.saveUser(promotedUser);
            
            // Refresh the user list
            populateUserList();
        }
    }
    
    /**
     * Handle deleting a user
     */
    private void handleDeleteUser(String username) {
        User userToDelete = userRepository.getUser(username);
        if (userToDelete != null && !userToDelete.isAdmin()) {
            // Delete the user
            userRepository.deleteUser(username);
            
            // Refresh the user list
            populateUserList();
        }
    }
    
    /**
     * Handle navigating back to the dashboard
     */
    @FXML
    private void handleBackToDashboard() {
        ViewNavigator.navigateToDashboard();
    }
}