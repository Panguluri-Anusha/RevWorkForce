package org.example.revworkforce.service;
import org.example.revworkforce.dao.*;
import org.example.revworkforce.model.*;
import org.example.revworkforce.dao.LeaveDAO;
import org.example.revworkforce.dao.PerformanceDAO;
import org.example.revworkforce.model.LeaveApplication;
import org.example.revworkforce.model.PerformanceReview;

import java.util.ArrayList;
import java.util.List;

public class ManagerService {

    private LeaveDAO leaveDAO = new LeaveDAO();
    private PerformanceDAO performanceDAO = new PerformanceDAO();

    // Get all leaves of the team
    public List<LeaveApplication> viewTeamLeaves(int empId) {
        return leaveDAO.getPendingLeavesByManager(empId); // Only pending leaves for team
    }

    // Approve / Reject leave
    public boolean managerDecideLeave(int leaveId, int managerId, boolean approve, String comment) {
        String status = approve ? "APPROVED_BY_MANAGER" : "REJECTED_BY_MANAGER";
        return leaveDAO.updateLeaveStatus(leaveId, status, comment, managerId);
    }

    // Get performance reviews of the team
    public List<PerformanceReview> viewTeamReviews(int empId) {
        return performanceDAO.getReviewsByEmpId(empId);
    }

    // Add performance review
    public boolean addPerformanceReview(PerformanceReview review) {
        return performanceDAO.addReview(review);
    }

    public List<PerformanceReview> getAllPerformanceReviews() {
        return performanceDAO.getAllReviews();
    }
    // Inside ManagerService.java at line 129
    public boolean updateReview(PerformanceReview review) {
        // Check if the date is null and initialize it if necessary
        if (review.getReviewDate() == null) {
            review.setReviewDate(new java.util.Date());
        }
        return performanceDAO.updateReview(review);
    }
    //public boolean updateReview(PerformanceReview r) {
       // return performanceDAO.updateReview(r);
    //}
//    public boolean updateReview(PerformanceReview r) {
//        if (r == null) return false;
//
//        // Fix for NullPointerException in PerformanceDAO
//        if (r.getReviewDate() == null) {
//            r.setReviewDate(new java.util.Date());
//        }
//
//        return performanceDAO.updateReview(r);
//    }
//    public boolean updateReview(PerformanceReview r) {
//        if (r == null) return false;
//        // Fix for the NullPointerException seen in logs
//        if (r.getReviewDate() == null) {
//            r.setReviewDate(new java.util.Date());
//        }
//        return performanceDAO.updateReview(r);
//    }


    public PerformanceReview getReview(int empId, int year) {
        return performanceDAO.getReview(empId, year);
    }

    public List<EmployeeGoal> getTeamGoals(int managerId) {
        // Basic validation to ensure a valid manager ID is provided
        if (managerId <= 0) {
            return new ArrayList<>();
        }

        // Create an instance of the DAO to fetch data
        EmployeeGoalDAO goalDAO = new EmployeeGoalDAO();

        // Call the specific DAO method that joins employee and goal tables
        return goalDAO.getGoalsByManager(managerId);
    }

}
