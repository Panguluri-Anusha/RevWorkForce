package org.example.revworkforce.dao;


import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.model.Designation;
import org.example.revworkforce.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.revworkforce.model.Department;

public class EmployeeDAO {

    // ---------------- LOGIN ----------------
    public Employee login(String email, String password) {
        String sql = "SELECT * FROM employee WHERE email=? AND password=? AND status='ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setRole(rs.getString("role"));
                emp.setManagerId(rs.getInt("manager_id"));
                return emp;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT employee_id, name, email, role, status FROM employee";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                e.setRole(rs.getString("role"));
                e.setStatus(rs.getString("status"));
                list.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public boolean updatePassword(int employeeId, String newPassword) {
        String sql = "UPDATE employee SET password = ? WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employeeId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//    // ---------------- ADD EMPLOYEE ----------------
//    public boolean addEmployee(Employee employee) {
//        String sql = "INSERT INTO employee(name, email, password, phone, address, dob, department_id, " +
//                "designation_id, manager_id, role, status, joining_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, employee.getName());
//            ps.setString(2, employee.getEmail());
//            ps.setString(3, employee.getPassword());
//            ps.setString(4, employee.getPhone());
//            ps.setString(5, employee.getAddress());
//
//            if (employee.getDob() != null) {
//                ps.setDate(6, new java.sql.Date(employee.getDob().getTime()));
//            } else {
//                ps.setDate(6, null);
//            }
//
//            ps.setInt(7, employee.getDepartmentId());
//            ps.setInt(8, employee.getDesignationId());
//
//            if (employee.getManagerId() != 0) {
//                ps.setInt(9, employee.getManagerId());
//            } else {
//                ps.setNull(9, Types.INTEGER);
//            }
//
//            ps.setString(10, employee.getRole() != null ? employee.getRole() : "EMPLOYEE");
//            ps.setString(11, employee.getStatus() != null ? employee.getStatus() : "ACTIVE");
//
//            if (employee.getJoiningDate() != null) {
//                ps.setDate(12, new java.sql.Date(employee.getJoiningDate().getTime()));
//            } else {
//                ps.setDate(12, null);
//            }
//
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    // ---------------- ADD EMPLOYEE ----------------
    public boolean addEmployee(Employee employee) {

        String sql = """
        INSERT INTO employee(
            name, email, password, phone, alternate_phone, address, dob,
            department_id, designation_id, manager_id, role, status, joining_date
        )
        VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 1. Basic Info
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setString(3, employee.getPassword());

            // 2. Phone Numbers
            ps.setString(4, employee.getPhone());                 // Main phone
            ps.setString(5, employee.getAlternatePhone());        // Alternate phone ✅

            // 3. Address
            ps.setString(6, employee.getAddress());

            // 4. Date of Birth
            if (employee.getDob() != null) {
                ps.setDate(7, new java.sql.Date(employee.getDob().getTime()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            // 5. Department & Designation
            ps.setInt(8, employee.getDepartmentId());
            ps.setInt(9, employee.getDesignationId());

            // 6. Manager
            if (employee.getManagerId() != 0) {
                ps.setInt(10, employee.getManagerId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }

            // 7. Role & Status
            ps.setString(11,
                    employee.getRole() != null ? employee.getRole() : "EMPLOYEE");

            ps.setString(12,
                    employee.getStatus() != null ? employee.getStatus() : "ACTIVE");

            // 8. Joining Date
            if (employee.getJoiningDate() != null) {
                ps.setDate(13,
                        new java.sql.Date(employee.getJoiningDate().getTime()));
            } else {
                ps.setNull(13, Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ---------------- GET EMPLOYEE BY ID ----------------
    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT * FROM employee WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- UPDATE EMPLOYEE ----------------
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employee SET name=?, email=?, phone=?, address=?, dob=?, department_id=?, " +
                "designation_id=?, manager_id=?, role=?, status=?, joining_date=? WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPhone());
            ps.setString(4, emp.getAddress());

            if (emp.getDob() != null) {
                ps.setDate(5, new java.sql.Date(emp.getDob().getTime()));
            } else {
                ps.setDate(5, null);
            }

            ps.setInt(6, emp.getDepartmentId());
            ps.setInt(7, emp.getDesignationId());

            if (emp.getManagerId() != 0) {
                ps.setInt(8, emp.getManagerId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setString(9, emp.getRole() != null ? emp.getRole() : "EMPLOYEE");
            ps.setString(10, emp.getStatus() != null ? emp.getStatus() : "ACTIVE");

            if (emp.getJoiningDate() != null) {
                ps.setDate(11, new java.sql.Date(emp.getJoiningDate().getTime()));
            } else {
                ps.setDate(11, null);
            }

            ps.setInt(12, emp.getEmployeeId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- GET TEAM BY MANAGER ----------------
    public List<Employee> getTeamByManager(int managerId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE manager_id=? AND status='ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToEmployee(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ---------------- SET EMPLOYEE STATUS ----------------
    public boolean setEmployeeStatus(int employeeId, String status) {
        String sql = "UPDATE employee SET status=? WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status != null ? status.toUpperCase() : "INACTIVE");
            ps.setInt(2, employeeId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- GET ALL DEPARTMENTS ----------------
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setDepartmentName(rs.getString("department_name"));
                departments.add(dept);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    // ---------------- GET ALL MANAGERS ----------------
    public List<Employee> getAllManagers() {
        List<Employee> managers = new ArrayList<>();
        String sql = "SELECT * FROM employee e JOIN designation d ON e.designation_id = d.designation_id " +
                "WHERE d.designation_name='Manager'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee emp = mapResultSetToEmployee(rs);
                managers.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return managers;
    }

    // ---------------- UTILITY METHOD ----------------
//    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
//        Employee emp = new Employee();
//        emp.setEmployeeId(rs.getInt("employee_id"));
//        emp.setName(rs.getString("name"));
//        emp.setEmail(rs.getString("email"));
//        emp.setPhone(rs.getString("phone"));
//        emp.setAddress(rs.getString("address"));
//        emp.setDob(rs.getDate("dob"));
//        emp.setDepartmentId(rs.getInt("department_id"));
//        emp.setDesignationId(rs.getInt("designation_id"));
//        emp.setManagerId(rs.getInt("manager_id"));
//        emp.setRole(rs.getString("role"));
//        emp.setStatus(rs.getString("status"));
//        emp.setJoiningDate(rs.getDate("joining_date"));
//        return emp;
//    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {

        Employee emp = new Employee();

        emp.setEmployeeId(rs.getInt("employee_id"));
        emp.setName(rs.getString("name"));
        emp.setEmail(rs.getString("email"));
        emp.setPassword(rs.getString("password"));

        emp.setPhone(rs.getString("phone"));
        emp.setAlternatePhone(rs.getString("alternate_phone")); //

        emp.setAddress(rs.getString("address"));
        emp.setDob(rs.getDate("dob"));

        emp.setDepartmentId(rs.getInt("department_id"));
        emp.setDesignationId(rs.getInt("designation_id"));
        emp.setManagerId(rs.getInt("manager_id"));

        emp.setRole(rs.getString("role"));
        emp.setStatus(rs.getString("status"));

        emp.setJoiningDate(rs.getDate("joining_date")); //

        return emp;
    }


    public List<Designation> getAllDesignations() {

        List<Designation> list = new ArrayList<>();

        String sql = "SELECT designation_id, designation_name FROM designation";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Designation d = new Designation();

                d.setDesignationId(rs.getInt("designation_id"));
                d.setDesignationName(rs.getString("designation_name"));

                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // ---------------- AUTHENTICATE ----------------
    // FIX: Changed table name from 'employees' to 'employee'
    public Employee authenticate(int id, String password) {
        String sql = "SELECT employee_id, role FROM employee WHERE employee_id = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setRole(rs.getString("role"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------- GET MANAGER ID ----------------
    public int getManagerIdByEmployee(int empId) {
        String sql = "SELECT manager_id FROM employee WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("manager_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------------- UPDATE PROFILE ----------------
    // FIX: The SQL string was replaced by a SELECT string in your snippet.
    // This fix matches your ps.setString parameters (phone, address, emergency, id).
    public boolean updateEmployeeProfile(Employee emp) {
        String sql = "UPDATE employee SET phone = ?, address = ?, emergency_contact = ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getPhone());
            ps.setString(2, emp.getAddress());
            ps.setString(3, emp.getEmergencyContact());
            ps.setInt(4, emp.getEmployeeId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
