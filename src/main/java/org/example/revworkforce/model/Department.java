package org.example.revworkforce.model;


public class Department {
    private int departmentId;
    private String departmentName;
    private int designationId;

    // Constructors
    public Department() {}

    public Department(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
    public String getDeptName() {
        return departmentName; // return the same as getDepartmentName()
    }
    public void setDeptName(String deptName) {
        this.departmentName = deptName;
    }

    public int getDesignationId() {
        return designationId;
    }

    public void setDesignationId(int designationId) {
        this.designationId = designationId;
    }






}
