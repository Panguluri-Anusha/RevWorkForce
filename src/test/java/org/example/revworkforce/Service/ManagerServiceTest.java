package org.example.revworkforce.Service;
// Match your actual package name

import org.example.revworkforce.dao.PerformanceDAO;

import org.example.revworkforce.model.PerformanceReview;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.revworkforce.dao.PerformanceDAO;
import org.junit.jupiter.api.AfterEach;
import org.example.revworkforce.service.ManagerService;

public class ManagerServiceTest {
    private ManagerService managerService;
    private PerformanceDAO performanceDAO = new PerformanceDAO(); // Needed for cleanup

    @BeforeEach
    void setUp() {
        managerService = new ManagerService();
    }

    @AfterEach
    void tearDown() {
        // Clean up the test data so the test can be run again
        // You might need to add a simple delete method to your DAO
        // performanceDAO.deleteReview(2, 2024);
    }

    @Test
    void testUpdatePerformanceReview() {
        int testEmpId = 2;
        int testYear = 2024;

        // 1. Setup: Create a review for a specific employee and year
        PerformanceReview newReview = new PerformanceReview();
        newReview.setEmployeeId(testEmpId);
        newReview.setReviewYear(testYear);
        newReview.setReviewDate(new java.util.Date());
        newReview.setStatus("PENDING");

        managerService.addPerformanceReview(newReview);

        // 2. Prepare the Update: Create a review object with the MATCHING keys
        PerformanceReview updateInfo = new PerformanceReview();
        updateInfo.setEmployeeId(testEmpId);
        updateInfo.setReviewYear(testYear);
        updateInfo.setManagerRating(5);
        updateInfo.setManagerFeedback("Excellent work!");
        updateInfo.setStatus("COMPLETED");

        // 3. Execute
        boolean result = managerService.updateReview(updateInfo);

        // 4. Assert
        assertTrue(result, "Manager should be able to update review using EmployeeID and Year");
    }
}