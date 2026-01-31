package org.example.revworkforce.dao;

import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // Add Department
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO department(dept_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getDeptName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Get all departments
    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Department(rs.getInt("dept_id"), rs.getString("dept_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateEmployeeStatus(int empId, String status) {
        String sql = "UPDATE employee SET status = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, empId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

