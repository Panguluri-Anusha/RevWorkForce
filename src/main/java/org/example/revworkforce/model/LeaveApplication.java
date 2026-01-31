package org.example.revworkforce.model;

import java.util.Date;

public class LeaveApplication {
    private int leaveId;
    private int employeeId;
    private int leaveTypeId;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private String status;
    private String managerComment;
    private int approvedBy;

    // Default Constructor
    public LeaveApplication() {}

    // Getters and Setters
    public int getLeaveId() { return leaveId; }
    public void setLeaveId(int leaveId) { this.leaveId = leaveId; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getLeaveTypeId() { return leaveTypeId; }
    public void setLeaveTypeId(int leaveTypeId) { this.leaveTypeId = leaveTypeId; }
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
    public int getApprovedBy() { return approvedBy; }
    public void setApprovedBy(int approvedBy) { this.approvedBy = approvedBy; }

    private String employeeName;
    private String leaveType;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

}