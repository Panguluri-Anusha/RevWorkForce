package org.example.revworkforce.dao;
import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.model.Holiday;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class HolidayDAO {

    // ================= GET ALL HOLIDAYS =================
    public List<Holiday> getAllHolidays() {

        List<Holiday> list = new ArrayList<>();

        String sql = "SELECT * FROM holidays ORDER BY holiday_date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Holiday h = new Holiday();

                h.setHolidayId(rs.getInt("holiday_id"));
                h.setHolidayName(rs.getString("holiday_name"));

                Date sqlDate = rs.getDate("holiday_date");

                if (sqlDate != null) {
                    h.setHolidayDate(sqlDate.toLocalDate());
                }

                list.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    // ================= ADD HOLIDAY =================
    public boolean addHoliday(Holiday holiday) {

        String sql = "INSERT INTO holidays (holiday_name, holiday_date) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, holiday.getHolidayName());

            ps.setDate(2, Date.valueOf(holiday.getHolidayDate()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


//    // ================= DELETE HOLIDAY =================
//    public boolean deleteHoliday(int holidayId) {
//
//        String sql = "DELETE FROM holidays WHERE holiday_id = ?";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, holidayId);
//
//            int rows = ps.executeUpdate();
//
//            System.out.println("Rows deleted: " + rows); // Debug
//
//            return rows > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
// ================= DELETE HOLIDAY =================
public boolean removeHoliday(int holidayId) { // ✅ renamed to match service
    String sql = "DELETE FROM holidays WHERE holiday_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, holidayId);

        int rows = ps.executeUpdate();

        System.out.println("Rows deleted: " + rows); // Debug

        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}


    // ================= TRUNCATE HOLIDAYS =================
    public void truncateHolidayTable() {

        String sql = "TRUNCATE TABLE holidays";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
