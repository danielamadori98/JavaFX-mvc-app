package com.dashapp.view;

import com.dashapp.Main;
import com.dashapp.controller.MainController;
import com.dashapp.model.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import com.dashapp.model.User;
import java.io.IOException;
import java.net.URL;

/**
 * This class handles navigation between different views in the application.
 * It works as a bridge between controllers and views, allowing for simplified navigation.
 */
public class ViewNavigator {
    // Reference to the main controller
    private static MainController mainController;
    
    // Current authenticated username
    private static User authenticatedUser = null;

    private static Project selectedProject = null;

    public static void setSelectedProject(Project project) {
        selectedProject = project;
    }

    public static Project getSelectedProject() {
        return selectedProject;
    }


    /**
     * Set the main controller reference
     * @param controller The MainController instance
     */
    public static void setMainController(MainController controller) {
        mainController = controller;
    }
    
    /**
     * Load and switch to a view
     * @param fxml The name of the FXML file to load
     */
    public static void loadView(String fxml) {
        try {
            URL fxmlUrl = Main.class.getResource("/resources/fxml/" + fxml);
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node view = loader.load();
            mainController.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxml);
        }
    }

    
    
    /**
     * Navigate to the home view
     */
    public static void navigateToHome() {
        loadView("HomeView.fxml");
    }
    
    /**
     * Navigate to the login view
     */
    public static void navigateToLogin() {
        loadView("LoginView.fxml");
    }
    
    /**
     * Navigate to the register view
     */
    public static void navigateToRegister() {
        loadView("RegisterView.fxml");
    }
    
    /**
     * Navigate to the dashboard view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToDashboard() {
        if (isAuthenticated()) {
            loadView("DashboardView.fxml");
        } else {
            navigateToLogin();
        }
    }
    
    /**
     * Navigate to the profile view (protected)
     * Will redirect to login if not authenticated
     */
    public static void navigateToProfile() {
        if (isAuthenticated()) {
            loadView("ProfileView.fxml");
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToStats() {
        if (isAuthenticated()) {
            loadView("StatsView.fxml");
        } else {
            navigateToLogin();
        }
    }

    public static void navigateToProjects() {
        if (isAuthenticated()) {
            loadView("ProjectView.fxml");
        } else {
            navigateToLogin();
        }
    }
    
    /**
     * Set the authenticated user
     * @param username The username of the authenticated user
     */
    public static void setAuthenticatedUser(User user) {
        authenticatedUser = user;
        mainController.updateNavBar(isAuthenticated());
    }
    
    /**
     * Get the authenticated user
     * @return The username of the authenticated user, or null if not authenticated
     */
    public static User getAuthenticatedUser() {
        return authenticatedUser;
    }
    
    /**
     * Check if a user is authenticated
     * @return true if a user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        return authenticatedUser != null;
    }
    
    /**
     * Logout the current user
     */
    public static void logout() {
        authenticatedUser = null;
        mainController.updateNavBar(false);
        navigateToHome();
    }
}