
package org.example.revworkforce.Service;
import org.example.revworkforce.model.Employee;
import org.example.revworkforce.service.AuthService;
import org.example.revworkforce.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    private AuthService authService;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        employeeService = new EmployeeService();
    }

    @Test
    @DisplayName("TC-AUTH-01: Login with Employee ID and Password")
    void testLoginSuccess() {
        // Requirement: Login to my account using employee ID and password
        Employee emp = authService.login(1, "password123");
        assertNotNull(emp, "Employee should be able to login with valid credentials");
        assertEquals(1, emp.getEmployeeId());
    }

    @Test
    @DisplayName("TC-PROFILE-01: View Reporting Manager Details")
    void testViewReportingManager() {
        // Requirement: View my reporting manager details
        int managerId = employeeService.getReportingManagerId(1);
        assertTrue(managerId > 0, "Employee should have a valid reporting manager assigned");
    }

    @Test
    @DisplayName("TC-PROFILE-02: Edit Profile Information")
    void testEditProfile() {
        // Requirement: Edit basic profile information (phone, address, emergency contact)
        Employee emp = new Employee();
        emp.setEmployeeId(1);
        emp.setPhone("9876543210");
        emp.setAddress("123 Java Lane");

        boolean updated = employeeService.updateProfile(emp);
        assertTrue(updated, "Employee should be able to update their basic profile info");
    }
}