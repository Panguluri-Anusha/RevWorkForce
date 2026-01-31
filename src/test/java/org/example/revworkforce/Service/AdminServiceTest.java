package org.example.revworkforce.Service;


import org.example.revworkforce.model.Holiday;
import org.example.revworkforce.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class AdminServiceTest {
    private AdminService adminService;
    @BeforeEach
    void setUp() {
        adminService = new AdminService();
        // This calls the method you just wrote
        adminService.clearHolidays();
    }

    @Test
    void testChangeEmployeeStatusToInactive() {
        // Assuming ID 3 exists in your test DB
        // This triggers the DAO update logic you added to the menu
        boolean result = adminService.updateStatusLogic(3, "INACTIVE");
        assertTrue(result, "Admin should be able to deactivate an employee");
    }

    @Test
    void testAddHolidaySuccess() {
        Holiday holiday = new Holiday();
        holiday.setHolidayName("Republic Day");
        holiday.setHolidayDate(LocalDate.of(2026, 1, 26));

        boolean result = adminService.validateAndAddHoliday(holiday);
        assertTrue(result, "Holiday should be added successfully if date is unique");
    }

    @Test
    void testAddHolidayDuplicateDateFailure() {
        // Testing the constraint violation you encountered earlier
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(LocalDate.of(2026, 2, 15)); // Existing date

        boolean result = adminService.validateAndAddHoliday(holiday);
        assertFalse(result, "Should return false or handle exception when date is duplicate");
    }
}
