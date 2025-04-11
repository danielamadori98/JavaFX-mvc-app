package com.dashapp.controller;

import com.dashapp.model.User;
import com.dashapp.view.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import com.dashapp.Main;

public class DashboardController {
    @FXML
    private Label welcomeLabel;
    
    @FXML
    public void initialize() {
        // This is a protected view, so we should always have an authenticated user
        User user = ViewNavigator.getAuthenticatedUser();
        if (user == null)
            return;

        String username = user.getUsername();
        welcomeLabel.setText("Welcome to your dashboard, " + username + "!");
    }

    @FXML
    private void handleStatDetails() {
        ViewNavigator.navigateToStats();
    }

    
    @FXML
    private void handleViewProfile() {
        ViewNavigator.navigateToProfile();
    }
    
    @FXML
    private void handleLogout() {
        ViewNavigator.logout();
    }
}