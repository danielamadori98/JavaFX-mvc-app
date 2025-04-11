package com.dashapp.model;

import java.time.LocalDate;

public class Project {
    private int id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String userId;

    public Project(int id, String name, String description, LocalDate startDate, LocalDate endDate, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getUserId() { return userId; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setUserId(String userId) { this.userId = userId; }
}