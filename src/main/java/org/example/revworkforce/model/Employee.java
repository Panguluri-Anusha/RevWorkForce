package org.example.revworkforce.model;

import java.util.Date;

public class Employee {
    private int employeeId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date dob;
    private int departmentId;
    private int designationId;
    private String designationName;
    private int managerId;
    private String role; // EMPLOYEE, MANAGER, ADMIN
    private String status; // ACTIVE, INACTIVE
    private Date joiningDate;
    private String alternatePhone;
    // Getters and Setters
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public int getDesignationId() { return designationId; }
    public void setDesignationId(int designationId) { this.designationId = designationId; }

    public String getDesignationName() { return designationName; }
    public void setDesignationName(String designationName) { this.designationName = designationName; }

    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getJoiningDate() { return joiningDate; }
    public void setJoiningDate(Date joiningDate) { this.joiningDate = joiningDate; }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }
    private String emergencyContact;
    public String getEmergencyContact() {

        return emergencyContact; // Ensure this field is defined in the class
    }
}

