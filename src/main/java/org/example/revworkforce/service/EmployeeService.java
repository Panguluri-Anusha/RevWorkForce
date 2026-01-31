package org.example.revworkforce.service;
import org.example.revworkforce.dao.PerformanceDAO;
import org.example.revworkforce.model.PerformanceReview;

import org.example.revworkforce.dao.EmployeeDAO;
import org.example.revworkforce.model.Department;
import org.example.revworkforce.model.Designation;
import org.example.revworkforce.model.Employee;
import org.example.revworkforce.model.PerformanceReview;

import java.util.List;

public class EmployeeService {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private PerformanceDAO performanceDAO = new PerformanceDAO();


    // ---------------- LOGIN ----------------
    public Employee login(String email, String password) {
        return employeeDAO.login(email, password);
    }


    // ---------------- ADD EMPLOYEE ----------------
    public boolean addEmployee(Employee employee) {
        return employeeDAO.addEmployee(employee);
    }

    // ---------------- GET ALL EMPLOYEES ----------------
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    // ---------------- GET TEAM MEMBERS ----------------
    public List<Employee> getTeam(int managerId) {
        return employeeDAO.getTeamByManager(managerId);
    }

    // ---------------- UPDATE EMPLOYEE ----------------
    public boolean updateEmployee(Employee employee) {
        return employeeDAO.updateEmployee(employee);
    }

    // ---------------- DEACTIVATE / REACTIVATE EMPLOYEE ----------------
    public boolean deactivateEmployee(int employeeId) {
        return employeeDAO.setEmployeeStatus(employeeId, "INACTIVE");
    }

    public boolean reactivateEmployee(int employeeId) {
        return employeeDAO.setEmployeeStatus(employeeId, "ACTIVE");
    }

    // ---------------- GET EMPLOYEE BY ID ----------------
    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    public Employee getEmployee(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    // ---------------- GET ALL DEPARTMENTS ----------------
    public List<Department> getAllDepartments() {
        return employeeDAO.getAllDepartments();
    }

    // ---------------- GET ALL MANAGERS ----------------
    public List<Employee> getAllManagers() {
        return employeeDAO.getAllManagers();
    }


    public List<Designation> getAllDesignations() {
        return employeeDAO.getAllDesignations();
    }
    public boolean submitSelfReview(PerformanceReview review) {
        return performanceDAO.addReview(review); // DAO handles database insert
    }

public boolean changePassword(int empId, String newPassword) {
    // Validation to reject empty or blank passwords
    if (newPassword == null || newPassword.trim().isEmpty()) {
        return false;
    }

    // Call the DAO to update the password in the database
    EmployeeDAO employeeDAO = new EmployeeDAO();
    return employeeDAO.updatePassword(empId, newPassword);
}
    public boolean addManagerReview(PerformanceReview r) {
        return performanceDAO.addManagerReview(r);
    }
    // Inside EmployeeService.java
    public int getReportingManagerId(int empId) {
        // This should call EmployeeDAO to get the manager_id column
        return employeeDAO.getManagerIdByEmployee(empId);
    }

    public boolean updateProfile(Employee emp) {
        // This should call EmployeeDAO to run an UPDATE query
        return employeeDAO.updateEmployeeProfile(emp);
    }

}
