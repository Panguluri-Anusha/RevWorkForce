package org.example.revworkforce.service;

import org.example.revworkforce.dao.EmployeeDAO;
import org.example.revworkforce.model.Employee;



public class AuthService {
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public Employee login(int id, String password) {
        // Authenticate using the ID and password
        return employeeDAO.authenticate(id, password);
    }
}
