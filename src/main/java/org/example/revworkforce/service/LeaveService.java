package org.example.revworkforce.service;


import org.example.revworkforce.dao.LeaveDAO;
import org.example.revworkforce.model.LeaveApplication;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.example.revworkforce.model.LeaveStatus;
public class LeaveService {

    private LeaveDAO leaveDAO = new LeaveDAO();
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    // 1. Apply Leave: Admin is auto-approved, others are pending
    public boolean applyLeave(LeaveApplication leave, String role) {
        if (ROLE_ADMIN.equalsIgnoreCase(role)) {
            // Rule: Admin leaves are self-approved
            leave.setStatus(LeaveStatus.APPROVED);
            leave.setApprovedBy(leave.getEmployeeId());
            leave.setManagerComment("System: Auto-approved for Admin");
        } else {
            // Rule: Employees and Managers stay PENDING for their respective bosses
            leave.setStatus(LeaveStatus.PENDING);
        }
        return leaveDAO.applyLeave(leave);
    }

    // 2. Get Pending Leaves (Hierarchy: Admin sees Managers, Manager sees Employees)
    public List<LeaveApplication> getPendingLeavesForUser(int currentUserId, String currentUserRole) {
        if (ROLE_ADMIN.equalsIgnoreCase(currentUserRole)) {
            // Admin sees all leaves submitted by users with ROLE_MANAGER
            return leaveDAO.getPendingLeavesForAdmin();
        } else if (ROLE_MANAGER.equalsIgnoreCase(currentUserRole)) {
            // Manager sees only PENDING leaves from their direct reports (ROLE_EMPLOYEE)
            return leaveDAO.getPendingLeavesByManager(currentUserId);
        }
        return new ArrayList<>();
    }

    // 3. Process Leave Decision
    public boolean decideLeave(int leaveId, int approverId, String status, String comment) {
        return leaveDAO.updateLeaveStatus(leaveId, status, comment, approverId);
    }

    // 4. Leave History
    public List<LeaveApplication> getEmployeeLeaveHistory(int employeeId) {
        return leaveDAO.getLeavesByEmployee(employeeId);
    }

    // 5. Utility: Date Formatting
    public String formatDate(Date date) {
        if (date == null) return "N/A";
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public List<String> getLeaveTypes() {
        List<String> leaveTypes = new ArrayList<>();
        try {
            // Fetch the list of leave type names from the DAO layer
            leaveTypes = leaveDAO.getAllLeaveTypes();
        } catch (Exception e) {
            // Log the error for debugging purposes in the console
            System.err.println("Error fetching leave types: " + e.getMessage());
        }
        return leaveTypes;
    }
    public Date formatDateOnly(Date date) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(date);
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LeaveApplication> getEmployeeLeaves(int employeeId) {
        // Retrieve the list of leaves from the database via the DAO
        return leaveDAO.getLeavesByEmployee(employeeId);
    }

    public List<LeaveApplication> getTeamPendingLeaves(int employeeId, String role) {
        if (ROLE_ADMIN.equalsIgnoreCase(role)) {
            // Rule: Admin sees all leaves submitted by Managers
            return leaveDAO.getPendingLeavesForAdmin();
        } else if (ROLE_MANAGER.equalsIgnoreCase(role)) {
            // Rule: Manager sees only PENDING leaves from their direct reports
            return leaveDAO.getPendingLeavesByManager(employeeId);
        }
        // Regular employees do not have a team to manage
        return new ArrayList<>();
    }

    public void viewMyLeaveBalance(int employeeId) {
        leaveDAO.getEmployeeLeaveBalance(employeeId);
    }

}