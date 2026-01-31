package org.example.revworkforce.service;

import org.example.revworkforce.dao.NotificationDAO;
import org.example.revworkforce.model.Notification;

import java.util.List;


public class NotificationService {

    private NotificationDAO dao = new NotificationDAO();

    // Send notification
    public boolean sendNotification(Notification n) {
        return dao.addNotification(n);
    }

    // Get all notifications of an employee
    public List<Notification> getNotifications(int employeeId) {
        return dao.getNotificationsByEmployee(employeeId);
    }

    // Delete notification
    public boolean removeNotification(int notificationId) {
        return dao.deleteNotification(notificationId);
    }

    // Mark notification as read
    public boolean markNotificationRead(int notificationId) {
        return dao.markAsRead(notificationId);
    }

    public void addNotification(int employeeId, String message, String type) {

        Notification notification = new Notification();

        notification.setEmployeeId(employeeId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus("UNREAD");

        dao.addNotification(notification);
    }

        public boolean markAsRead(int notificationId) {
        return dao.markAsRead(notificationId);
    }

}
