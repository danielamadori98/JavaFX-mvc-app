package com.dashapp.controller;

import com.dashapp.view.ViewNavigator;
import com.dashapp.view.components.NavBar;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import com.dashapp.model.User;

public class MainController {
    @FXML
    private BorderPane mainContainer;
    
    @FXML
    private VBox navBarContainer;
    
    private NavBar navBar;
    
    @FXML
    public void initialize() {
        // Set up the navigation bar
        navBar = new NavBar();
        navBarContainer.getChildren().add(navBar);
        
        // Register this controller with the ViewNavigator
        ViewNavigator.setMainController(this);
        
        // Load the home view by default
        ViewNavigator.navigateToHome();
    }
    
    /**
     * Set the content of the main area
     */
    public void setContent(Node content) {
        mainContainer.setCenter(content);
    }
    

    public void updateNavBar(boolean isAuthenticated) {
        navBar.updateAuthStatus(isAuthenticated);
    }
}