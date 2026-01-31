package org.example.revworkforce.model;




import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int employeeId;
    private String message;
    private String type;
    private String isRead; // READ or UNREAD
    private Timestamp createdAt;

    // Constructors
    public Notification() {}

    public Notification(int employeeId, String message, String type) {
        this.employeeId = employeeId;
        this.message = message;
        this.type = type;
        this.isRead = "UNREAD";
    }

    // Getters & Setters
    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getIsRead() { return isRead; }
    public void setIsRead(String isRead) { this.isRead = isRead; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Notification [id=" + notificationId + ", employeeId=" + employeeId +
                ", message=" + message + ", type=" + type +
                ", isRead=" + isRead + ", createdAt=" + createdAt + "]";
    }

    public void setReadStatus(String status) {
        this.isRead = status;
    }

}
