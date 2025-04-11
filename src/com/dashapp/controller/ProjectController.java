
package com.dashapp.controller;

import com.dashapp.model.Project;
import com.dashapp.model.User;
import com.dashapp.view.ViewNavigator;
import com.dashapp.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableRow;



import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProjectController {

    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> nameColumn;
    @FXML
    private TableColumn<Project, String> descriptionColumn;
    @FXML
    private TableColumn<Project, LocalDate> startDateColumn;
    @FXML
    private TableColumn<Project, LocalDate> endDateColumn;

    @FXML
    private Button completeButton;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextArea projectDescriptionField;


    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        startDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartDate()));
        endDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEndDate()));

        projectTable.setRowFactory(tv -> {
            TableRow<Project> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Project selectedProject = row.getItem();
                    ViewNavigator.setSelectedProject(selectedProject);
                    ViewNavigator.loadView("ProjectDetailView.fxml");
                }
            });
            return row;
        });

        refreshProjectList();
    }

    private void refreshProjectList() {
        User currentUser = ViewNavigator.getAuthenticatedUser();

        Project selected = projectTable.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.getUserId().equals(ViewNavigator.getAuthenticatedUser().getId())) {
            completeButton.setDisable(true);
        } else {
            completeButton.setDisable(false);
        }

        projectTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                completeButton.setDisable(newSelection.getEndDate() != null);
            } else {
                completeButton.setDisable(true);
            }
        });

        try {
            List<Project> projects = Main.getProjectRepository().getProjectsForUser(currentUser);
            projectTable.getItems().setAll(projects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMarkAsCompleted() {
        Project selectedProject = projectTable.getSelectionModel().getSelectedItem();
        if (selectedProject != null && selectedProject.getEndDate() == null) {
            selectedProject.setEndDate(LocalDate.now());
            try {
                Main.getProjectRepository().updateProjectEndDate(selectedProject.getId(), selectedProject.getEndDate());
                refreshProjectList();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    @FXML
    public void handleAddProject() {
        String name = projectNameField.getText();
        String description = projectDescriptionField.getText();

        if (name == null || name.isEmpty()) {
            System.out.println("Project name is required");
            return;
        }

        try {
            Project newProject = new Project(
                    0,
                    name,
                    description != null ? description : "",
                    LocalDate.now(),
                    null, // No end date initially
                    ViewNavigator.getAuthenticatedUser().getId()
            );
            Main.getProjectRepository().addProject(newProject);
            refreshProjectList();
            projectNameField.clear();
            projectDescriptionField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleDeleteProject() {
        Project selected = projectTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Main.getProjectRepository().deleteProject(selected.getId());
                refreshProjectList();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    private Node createProjectCard(Project project, User currentUser) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: lightgray; -fx-border-radius: 8; -fx-padding: 10;");

        Label title = new Label(project.getName());
        Label desc = new Label(project.getDescription());
        Label managerLabel = new Label("Manager ID: " + project.getUserId());

        box.getChildren().addAll(title, desc, managerLabel);

        if (project.getUserId().equals(currentUser.getId())) {
            Button manage = new Button("Gestisci");
            manage.setOnAction(e -> openDetailView(project));
            box.getChildren().add(manage);
        }

        return box;
    }

    private void openDetailView(Project project) {
        ViewNavigator.setSelectedProject(project);
        ViewNavigator.loadView("ProjectDetailView.fxml");
    }
}
