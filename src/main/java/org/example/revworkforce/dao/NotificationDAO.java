package org.example.revworkforce.dao;


import org.example.revworkforce.model.Notification;
import org.example.revworkforce.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public boolean addNotification(Notification n) {
        String sql = "INSERT INTO notification(employee_id, message, type) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n.getEmployeeId());
            ps.setString(2, n.getMessage());
            ps.setString(3, n.getType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notification> getNotificationsByEmployee(int employeeId) {

        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notification " +
                "WHERE employee_id=? " +
                "ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Notification n = new Notification();

                n.setNotificationId(rs.getInt("notification_id"));
                n.setEmployeeId(rs.getInt("employee_id"));
                n.setMessage(rs.getString("message"));
                n.setType(rs.getString("type"));
                n.setIsRead(rs.getString("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notification SET is_read='READ' WHERE notification_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteNotification(int notificationId) {

        String sql = "DELETE FROM notification WHERE notification_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}