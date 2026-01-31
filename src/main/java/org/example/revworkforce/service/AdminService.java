package org.example.revworkforce.service;

import org.example.revworkforce.dao.AdminDAO;
import org.example.revworkforce.dao.EmployeeDAO;
import org.example.revworkforce.dao.HolidayDAO;
import org.example.revworkforce.model.Department;
import org.example.revworkforce.model.Employee;
import org.example.revworkforce.model.Holiday;
import org.example.revworkforce.util.InputUtil;
import java.time.LocalDate;
import java.util.List;

public class AdminService {
    private AdminDAO adminDAO = new AdminDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private HolidayDAO holidayDAO = new HolidayDAO(); // Initialize at class level

    public boolean addDepartment(Department dept) {
        return adminDAO.addDepartment(dept);
    }

    public List<Department> getAllDepartments() {
        return adminDAO.getAllDepartments();
    }

    public boolean addEmployee(Employee emp) {
        return employeeDAO.addEmployee(emp);
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    // Workflow for Admin Menu interaction
    public void addHoliday() {
        String name = InputUtil.getString("Enter Holiday Name: ");
        LocalDate date = InputUtil.getLocalDate("Enter Holiday Date (yyyy-MM-dd): ");

        Holiday h = new Holiday();
        h.setHolidayName(name);
        h.setHolidayDate(date);

        // Uses validation logic before adding
        if (validateAndAddHoliday(h)) {
            System.out.println("Holiday added successfully!");
        } else {
            System.out.println("Failed to add holiday. Date might already exist.");
        }
    }

    // Logic for Dashboard Menu
    public void changeEmployeeStatus(String status) {
        int empId = InputUtil.getInt("Enter Employee ID to " + status.toLowerCase() + ": ");
        if (updateStatusLogic(empId, status)) {
            System.out.println("Employee status updated to " + status + " successfully!");
        } else {
            System.out.println("Failed to update status. Please verify the Employee ID.");
        }
    }

     public boolean updateStatusLogic(int empId, String status) {
        // Validation to prevent invalid database calls
        if (empId <= 0 || status == null) return false;

        // Bridge to the AdminDAO to update the employee table
        return adminDAO.updateEmployeeStatus(empId, status);
    }

    public boolean validateAndAddHoliday(Holiday holiday) {
        // Check for null or incomplete holiday data
        if (holiday == null || holiday.getHolidayDate() == null ||
                holiday.getHolidayName() == null || holiday.getHolidayName().trim().isEmpty()) {
            return false;
        }

        // Check for existing holidays to prevent duplicate entries
        List<Holiday> existingHolidays = holidayDAO.getAllHolidays();
        for (Holiday existing : existingHolidays) {
            if (existing.getHolidayDate().equals(holiday.getHolidayDate())) {
                return false;
            }
        }

        return holidayDAO.addHoliday(holiday);
    }

public void clearHolidays() {
    holidayDAO.truncateHolidayTable(); //
}

    // New method to delete a specific holiday
    public boolean removeHoliday(int holidayId) {
        return holidayDAO.removeHoliday(holidayId); // matches DAO method
    }



}
