package org.example.revworkforce.model;

import org.example.revworkforce.util.InputUtil;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EmployeeGoal {

    private int goalId;
    private int employeeId;
    private int reviewId; // Required for your FOREIGN KEY
    private String goalDescription;
    private LocalDate deadline; // Renamed from targetDate to match SQL
    private String priority;    // Added to match your SQL ENUM
    private String status;

    public EmployeeGoal() {}

    public EmployeeGoal(int goalId, String goalDescription, LocalDate deadline, String status) {
        this.goalId = goalId;
        this.goalDescription = goalDescription;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and setters
    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public String getGoalDescription() { return goalDescription; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }

    // Synchronized with SQL column 'deadline'
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    // Synchronized with SQL column 'priority'
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Legacy support for your InputUtil
    public void inputTargetDateFromUser() {
        Date utilDate = InputUtil.getDate("Enter target date (yyyy-MM-dd): ");
        if (utilDate != null) {
            this.deadline = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
    public void setTargetDate(LocalDate deadline) {
        // This updates the internal deadline field used by the database
        this.deadline = deadline;
    }

    public LocalDate getTargetDate() {
        // Returns the deadline field to match your SQL column 'deadline'
        return this.deadline;
    }
}