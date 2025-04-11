package com.dashapp.controller;

import com.dashapp.Main;
import com.dashapp.model.Project;
import com.dashapp.model.User;
import com.dashapp.view.ViewNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectDetailController {
    @FXML private Label projectManagerLabel;
    @FXML private Label projectNameLabel;
    @FXML private Label projectDescriptionLabel;
    @FXML private ComboBox<User> userComboBox;
    @FXML private TableView<User> assignedUsersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private Button assignButton;
    @FXML private Button removeButton;

    private Project selectedProject;
    private User currentUser;

    @FXML
    public void initialize() {
        selectedProject = ViewNavigator.getSelectedProject();
        currentUser = ViewNavigator.getAuthenticatedUser();

        if (selectedProject == null || currentUser == null) {
            System.err.println("No project or user selected.");
            return;
        }

        projectNameLabel.setText(selectedProject.getName());
        projectDescriptionLabel.setText(selectedProject.getDescription());

        User manager = Main.getUserRepository().getUserById(selectedProject.getUserId());
        if (manager != null) {
            projectManagerLabel.setText("Manager: " + manager.getUsername());
        }

        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        boolean isManager = currentUser.getId().equals(selectedProject.getUserId());
        assignButton.setDisable(!isManager);
        removeButton.setDisable(!isManager);
        userComboBox.setDisable(!isManager);

        try {
            refreshUserLists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAssignUser() {
        User selected = userComboBox.getValue();
        if (selected != null) {
            try {
                Main.getProjectRepository().assignUser(selectedProject.getId(), selected);
                refreshUserLists();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleRemoveUser() {
        User selected = assignedUsersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Main.getProjectRepository().removeUser(selectedProject.getId(), selected.getId());
                refreshUserLists();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshUserLists() throws SQLException {
        List<User> assigned = Main.getProjectRepository().getUsersForProject(selectedProject.getId());
        List<User> allUsers = new ArrayList<>(Main.getUserRepository().getAllUsers().values());

        userComboBox.setItems(FXCollections.observableArrayList(
                allUsers.stream()
                        .filter(u -> !assigned.contains(u) && !u.getId().equals(selectedProject.getUserId()))
                        .collect(Collectors.toList())
        ));

        userComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getUsername());
            }
        });
        userComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getUsername());
            }
        });

        assignedUsersTable.setItems(FXCollections.observableArrayList(assigned));
    }
}
