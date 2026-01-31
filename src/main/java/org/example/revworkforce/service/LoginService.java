package org.example.revworkforce.service;

import org.example.revworkforce.dao.EmployeeDAO;
import org.example.revworkforce.model.Employee;

public class LoginService {

    private EmployeeDAO employeeDAO = new EmployeeDAO();

    /**
     * Authenticate user by email and password
     * @param email - employee email
     * @param password - employee password
     * @return Employee object if credentials are valid and active, else null
     */
    public Employee authenticate(String email, String password) {
        Employee emp = employeeDAO.login(email, password);

        if (emp != null) {
            if ("ACTIVE".equalsIgnoreCase(emp.getStatus())) {
                System.out.println("Login successful! Welcome " + emp.getName());
                return emp; // Successful login
            } else {
                System.out.println("Your account is inactive. Please contact admin.");
            }
        } else {
            System.out.println("Invalid email or password.");
        }
        return null;
    }

    /**
     * Change password for employee
     * @param employee_id - employee ID
     * @param oldPassword - current password
     * @param newPassword - new password
     * @return true if updated successfully
     */
    public boolean changePassword(int employee_id, String oldPassword, String newPassword) {
        Employee emp = employeeDAO.getEmployeeById(employee_id);

        if (emp != null) {
            if (emp.getPassword().equals(oldPassword)) {
                emp.setPassword(newPassword);
                boolean updated = employeeDAO.updateEmployee(emp);
                if (updated) {
                    System.out.println("Password updated successfully.");
                } else {
                    System.out.println("Failed to update password.");
                }
                return updated;
            } else {
                System.out.println("Old password does not match!");
            }
        } else {
            System.out.println("Employee not found.");
        }
        return false;
    }

    /**
     * Update employee profile (delegates to EmployeeDAO)
     * @param emp - Employee object with updated fields
     * @return true if update succeeds
     */
    public boolean updateProfile(Employee emp) {
        return employeeDAO.updateEmployee(emp);
    }
}
