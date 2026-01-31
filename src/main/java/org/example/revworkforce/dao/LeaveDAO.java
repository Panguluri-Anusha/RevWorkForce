package org.example.revworkforce.dao;



import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.model.LeaveApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

import java.sql.*;


public class LeaveDAO {

    public boolean applyLeave(LeaveApplication leave) {
        String sql = "INSERT INTO leave_application(employee_id, leave_type_id, from_date, to_date, reason, status, approved_by, manager_comment) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leave.getEmployeeId());
            ps.setInt(2, leave.getLeaveTypeId());
            ps.setDate(3, new java.sql.Date(leave.getFromDate().getTime()));
            ps.setDate(4, new java.sql.Date(leave.getToDate().getTime()));
            ps.setString(5, leave.getReason());
            ps.setString(6, leave.getStatus());

            // FIX: If approvedBy is 0, set the SQL column to NULL
            if (leave.getApprovedBy() == 0) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, leave.getApprovedBy());
            }

            ps.setString(8, leave.getManagerComment());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateLeaveStatus(int leaveId, String status, String comment, int approvedBy) {
        String sql = "UPDATE leave_application SET status=?, manager_comment=?, approved_by=? WHERE leave_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, approvedBy);
            ps.setInt(4, leaveId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }


    // ADMIN Logic: Sees PENDING leaves from MANAGERS
    public List<LeaveApplication> getPendingLeavesForAdmin() {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT l.* FROM leave_application l " +
                "JOIN employee e ON l.employee_id = e.employee_id " +
                "WHERE e.role = 'MANAGER' AND l.status = 'PENDING'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(mapResultSetToLeave(rs)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<LeaveApplication> getLeavesByEmployee(int employeeId) {
        List<LeaveApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_application WHERE employee_id=? ORDER BY from_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { list.add(mapResultSetToLeave(rs)); }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private LeaveApplication mapResultSetToLeave(ResultSet rs) throws SQLException {
        LeaveApplication leave = new LeaveApplication();
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getInt("employee_id"));
        leave.setLeaveTypeId(rs.getInt("leave_type_id"));
        leave.setFromDate(rs.getDate("from_date"));
        leave.setToDate(rs.getDate("to_date"));
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));
        leave.setManagerComment(rs.getString("manager_comment"));
        leave.setApprovedBy(rs.getInt("approved_by"));
        return leave;
    }

    // Inside LeaveDAO.java
    public List<String> getAllLeaveTypes() {
        List<String> types = new ArrayList<>();
        // Updated to match your SQL: 'leave_name' instead of 'leave_type_name'
        String sql = "SELECT leave_name FROM leave_type";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                types.add(rs.getString("leave_name")); // Match here too
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

public void getTeamLeaveBalance(int managerId) {
    // We use 'leave_application' based on your schema,
    // but better yet, we use the 'leave_balance' table you created!
    String sql = """
        SELECT 
            e.name, 
            lt.leave_name, 
            lb.total_leaves, 
            lb.used_leaves, 
            (lb.total_leaves - lb.used_leaves) AS remaining
        FROM employee e
        JOIN leave_balance lb ON e.employee_id = lb.employee_id
        JOIN leave_type lt ON lb.leave_type_id = lt.leave_type_id
        WHERE e.manager_id = ?
        ORDER BY e.name, lt.leave_name
        """;

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, managerId);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n==================== Team Leave Balance ====================");
        System.out.printf("%-20s | %-15s | %-6s | %-6s | %-9s\n",
                "Employee", "Leave Type", "Total", "Used", "Remaining");
        System.out.println("------------------------------------------------------------");

        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            System.out.printf("%-20s | %-15s | %-6d | %-6d | %-9d\n",
                    rs.getString("name"),
                    rs.getString("leave_name"),
                    rs.getInt("total_leaves"),
                    rs.getInt("used_leaves"),
                    rs.getInt("remaining")
            );
        }

        if (!hasData) {
            System.out.println("No leave balance records found for your team.");
        }

    } catch (Exception e) {
        System.err.println("Error fetching team leave balance: " + e.getMessage());
        e.printStackTrace();
    }
}
    public List<LeaveApplication> getPendingLeavesByManager(int managerId) {

        List<LeaveApplication> list = new ArrayList<>();

        String sql = """
    SELECT 
        l.leave_id,
        e.name,
        lt.leave_name,
        l.from_date,
        l.to_date,
        l.reason

    FROM leave_application l
    JOIN employee e ON l.employee_id = e.employee_id
    JOIN leave_type lt ON l.leave_type_id = lt.leave_type_id

    WHERE e.manager_id = ?
    AND l.status = 'PENDING'
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LeaveApplication l = new LeaveApplication();

                l.setLeaveId(rs.getInt("leave_id"));
                l.setEmployeeName(rs.getString("name")); // create this field if missing
                l.setLeaveType(rs.getString("leave_name"));
                l.setFromDate(rs.getDate("from_date"));
                l.setToDate(rs.getDate("to_date"));
                l.setReason(rs.getString("reason"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void initializeLeaveBalance(int employeeId) {
        String sql = """
        INSERT INTO leave_balance (employee_id, leave_type_id, total_leaves, used_leaves)
        SELECT ?, leave_type_id, default_quota, 0 FROM leave_type
    """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getEmployeeLeaveBalance(int employeeId) {

        String sql = """
        SELECT 
            lt.leave_name,
            lb.total_leaves,
            lb.used_leaves,
            (lb.total_leaves - lb.used_leaves) AS remaining
        FROM leave_balance lb
        JOIN leave_type lt ON lb.leave_type_id = lt.leave_type_id
        WHERE lb.employee_id = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n========= My Leave Balance =========");
            System.out.printf("%-15s | %-6s | %-6s | %-9s\n",
                    "Leave Type", "Total", "Used", "Remaining");
            System.out.println("-----------------------------------");

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                System.out.printf("%-15s | %-6d | %-6d | %-9d\n",
                        rs.getString("leave_name"),
                        rs.getInt("total_leaves"),
                        rs.getInt("used_leaves"),
                        rs.getInt("remaining"));
            }

            if (!hasData) {
                System.out.println("No leave balance found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}