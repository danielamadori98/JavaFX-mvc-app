package com.dashapp.model;

import com.dashapp.Main;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    private final DatabaseManager dbManager;

    public ProjectRepository() {
        this.dbManager = Main.getDatabaseManager();
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                projects.add(new Project(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    LocalDate.parse(rs.getString("start_date")),
                    LocalDate.parse(rs.getString("end_date")),
                    rs.getString("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void addProject(Project project) throws SQLException {
        String sql = "INSERT INTO Project (name, description, start_date, end_date, manager_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getStartDate().toString());
            if (project.getEndDate() != null) {
                stmt.setString(4, project.getEndDate().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.setString(5, project.getUserId());
            stmt.executeUpdate();
        }
    }

    public void updateProjectEndDate(int projectId, LocalDate endDate) throws SQLException {
        String sql = "UPDATE Project SET end_date = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, endDate.toString());
            stmt.setInt(2, projectId);
            stmt.executeUpdate();
        }
    }


    public void deleteProject(int projectId) throws SQLException {
        String sql = "DELETE FROM Project WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            stmt.executeUpdate();
        }
    }

    public List<Project> getProjectsForUser(User user) {
        List<Project> projects = new ArrayList<>();
        String sql = """
            SELECT DISTINCT p.*
            FROM Project p
            LEFT JOIN ProjectAssignment pa ON p.id = pa.project_id
            WHERE p.manager_id = ? OR pa.user_id = ?
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate endDate = null;
                String endDateStr = rs.getString("end_date");
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    endDate = LocalDate.parse(endDateStr);
                }

                projects.add(new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        LocalDate.parse(rs.getString("start_date")),
                        endDate,
                        rs.getString("manager_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }



    public List<User> getUsersForProject(int projectId) throws SQLException {
        String sql = "SELECT user_id FROM ProjectAssignment WHERE project_id = ?";
        List<User> users = new ArrayList<>();
        UserRepository userRepo = Main.getUserRepository();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String userId = rs.getString("user_id");
                User user = userRepo.getUserById(userId);
                if (user != null) {
                    users.add(user);
                }
            }
        }

        return users;
    }

    public void assignUser(int projectId, User user) throws SQLException {
        String sql = "INSERT INTO ProjectAssignment (project_id, user_id) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            pstmt.setString(2, user.getId());
            pstmt.executeUpdate();
        }
    }

    public void removeUser(int projectId, String userId) throws SQLException {
        String sql = "DELETE FROM ProjectAssignment WHERE project_id = ? AND user_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }


}