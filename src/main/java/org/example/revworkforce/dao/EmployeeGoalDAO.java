package org.example.revworkforce.dao;


import org.example.revworkforce.model.EmployeeGoal;
import org.example.revworkforce.config.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeGoalDAO {

public boolean updateGoal(int goalId,
                          String description,
                          String status,
                          LocalDate targetDate) {

    String sql = """
    UPDATE goal
    SET goal_description = ?,
        progress_status = ?,
        deadline = ?
    WHERE goal_id = ?
    """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, description);
        ps.setString(2, status);
        ps.setDate(3, Date.valueOf(targetDate));
        ps.setInt(4, goalId);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

// Method to fetch goals (Fixes line 120 in your stack trace)
public List<EmployeeGoal> getGoalsByEmployee(int employeeId) {
    List<EmployeeGoal> goals = new ArrayList<>();
    // CHANGE: Update 'goal' to 'employee_goal'
    String sql = "SELECT * FROM employee_goal WHERE employee_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, employeeId);
        ResultSet rs = pstmt.executeQuery();
        // ... rest of your rs.next() mapping logic
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return goals;
}

    // Inside EmployeeGoalDAO.java
    public boolean addGoal(EmployeeGoal goal) {
        // CHANGE: 'target_date' to 'deadline' to match your SQL schema
        String sql = "INSERT INTO employee_goal (employee_id, review_id, goal_description, deadline, priority, progress_status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, goal.getEmployeeId());
            pstmt.setInt(2, goal.getReviewId());
            pstmt.setString(3, goal.getGoalDescription());

            // Ensure this matches the correct column index now
            pstmt.setDate(4, java.sql.Date.valueOf(goal.getDeadline()));
            pstmt.setString(5, goal.getPriority());
            pstmt.setString(6, "IN_PROGRESS");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getLatestReviewId(int employeeId) {

        String sql = """
        SELECT review_id 
        FROM performance_review
        WHERE employee_id=?
        ORDER BY review_year DESC
        LIMIT 1
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("review_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

public List<EmployeeGoal> getGoalsByManager(int managerId) {
    List<EmployeeGoal> teamGoals = new ArrayList<>();
    // This query connects the manager to their team's goals
    String sql = "SELECT g.* FROM employee_goal g " +
            "JOIN employee e ON g.employee_id = e.employee_id " +
            "WHERE e.manager_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, managerId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            EmployeeGoal goal = new EmployeeGoal();
            goal.setGoalId(rs.getInt("goal_id"));
            goal.setEmployeeId(rs.getInt("employee_id"));
            goal.setGoalDescription(rs.getString("goal_description"));
            // Ensure your column names match your CREATE TABLE script
            goal.setTargetDate(rs.getDate("deadline").toLocalDate());
            goal.setStatus(rs.getString("progress_status"));
            teamGoals.add(goal);
        }
    } catch (SQLException e) {
        e.printStackTrace(); // This will help catch any remaining column mismatches
    }
    return teamGoals;
}
}


