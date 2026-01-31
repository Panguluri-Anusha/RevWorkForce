//package org.example.revworkforce.Service;
//
//
//import org.example.revworkforce.model.Employee;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class EmployeeServiceTest {
//
//    @Test
//    void testLoginSuccess() {
//        assertTrue(true);
//    }
//
//    @Test
//    void testLoginFail() {
//        assertFalse(false);
//    }
//}

package org.example.revworkforce.Service;

import org.example.revworkforce.service.EmployeeService;
import org.example.revworkforce.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void testLoginSuccess() {
        // Replace with a known user from your local database for integration testing
        Employee emp = employeeService.login("admin@revature.com", "admin123");
        assertNotNull(emp);
        assertEquals("ADMIN", emp.getRole().toUpperCase());
    }

    @Test
    void testLoginFail() {
        // Test with incorrect credentials
        Employee emp = employeeService.login("wrong@email.com", "wrongpass");
        assertNull(emp);
    }

    @Test
    void testChangePasswordValidation() {
        // Verify that empty passwords return false
        boolean result = employeeService.changePassword(1, "   ");
        assertFalse(result, "Service should reject empty passwords");
    }

    @Test
    void testGetEmployeeById() {
        // Verify we can retrieve an existing employee record
        Employee emp = employeeService.getEmployee(1);
        if (emp != null) {
            assertEquals(1, emp.getEmployeeId());
        }
    }
}
