package org.example.revworkforce.dao;

import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.model.PerformanceReview;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceDAO {

    // ================= ADD REVIEW =================
    public boolean addReview(PerformanceReview r) {

        String sql =
                "INSERT INTO performance_review " +
                        "(employee_id, review_year, key_deliverables, accomplishments, " +
                        "areas_of_improvement, self_rating, status, review_date) " +
                        "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getEmployeeId());
            ps.setInt(2, r.getReviewYear());

            ps.setString(3, r.getKeyDeliverables());
            ps.setString(4, r.getAccomplishments());
            ps.setString(5, r.getAreasOfImprovement());

            // Handle nullable rating
            if (r.getSelfRating() != null) {
                ps.setInt(6, r.getSelfRating());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setString(7, r.getStatus());

            ps.setDate(8, new java.sql.Date(r.getReviewDate().getTime()));

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate review
            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

public boolean updateReview(PerformanceReview r) {
    // Correct SQL syntax for updating specific records
    String sql = "UPDATE performance_review SET manager_rating = ?, manager_feedback = ?, " +
            "status = ?, review_date = ? WHERE employee_id = ? AND review_year = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        // Handle potentially null Manager Rating to prevent unboxing errors
        if (r.getManagerRating() != null) {
            ps.setInt(1, r.getManagerRating());
        } else {
            ps.setNull(1, java.sql.Types.INTEGER);
        }

        ps.setString(2, r.getManagerFeedback());
        ps.setString(3, r.getStatus());

        // FIX: The null-check prevents java.util.Date.getTime() from throwing NPE
        if (r.getReviewDate() != null) {
            ps.setDate(4, new java.sql.Date(r.getReviewDate().getTime()));
        } else {
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Fallback to current date
        }

        ps.setInt(5, r.getEmployeeId());
        ps.setInt(6, r.getReviewYear());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    // ================= GET ALL (ADMIN) =================
    public List<PerformanceReview> getAllReviews() {

        List<PerformanceReview> list = new ArrayList<>();

        String sql =
                "SELECT * FROM performance_review ORDER BY review_year DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ================= GET BY EMPLOYEE =================
    public List<PerformanceReview> getByEmployee(int empId) {

        List<PerformanceReview> list = new ArrayList<>();

        String sql =
                "SELECT * FROM performance_review " +
                        "WHERE employee_id=? ORDER BY review_year DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // ================= GET ONE =================
    public PerformanceReview getReview(int empId, int year) {

        String sql =
                "SELECT * FROM performance_review " +
                        "WHERE employee_id=? AND review_year=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, year);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // ================= MAPPER =================
    private PerformanceReview mapRow(ResultSet rs) throws SQLException {

        PerformanceReview r = new PerformanceReview();

        r.setReviewId(rs.getInt("review_id"));
        r.setEmployeeId(rs.getInt("employee_id"));
        r.setReviewYear(rs.getInt("review_year"));

        r.setKeyDeliverables(rs.getString("key_deliverables"));
        r.setAccomplishments(rs.getString("accomplishments"));
        r.setAreasOfImprovement(rs.getString("areas_of_improvement"));

        int self = rs.getInt("self_rating");
        if (!rs.wasNull()) r.setSelfRating(self);

        int manager = rs.getInt("manager_rating");
        if (!rs.wasNull()) r.setManagerRating(manager);

        r.setManagerFeedback(rs.getString("manager_feedback"));
        r.setStatus(rs.getString("status"));

        r.setReviewDate(rs.getDate("review_date"));
        r.setReviewedBy(rs.getInt("reviewed_by"));

        return r;
    }

public int getLatestReviewId(int employeeId) {

    String sql =
            "SELECT review_id FROM performance_review " +
                    "WHERE employee_id = ? " +
                    "ORDER BY review_year DESC LIMIT 1";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, employeeId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("review_id"); // ✅ MUST return valid id
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0; // means "no review found"
}


    public boolean addManagerReview(PerformanceReview r) {

        String sql =
                "UPDATE performance_review SET " +
                        "manager_rating = ?, " +
                        "manager_feedback = ?, " +
                        "reviewed_by = ?, " +
                        "status = ?, " +
                        "review_date = ? " +
                        "WHERE employee_id = ? AND review_year = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getManagerRating());
            ps.setString(2, r.getManagerFeedback());
            ps.setInt(3, r.getReviewedBy());
            ps.setString(4, "REVIEWED");

            ps.setDate(5,
                    new java.sql.Date(r.getReviewDate().getTime()));

            ps.setInt(6, r.getEmployeeId());
            ps.setInt(7, r.getReviewYear());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public int getLatestReviewIdByEmployee(int empId) {

        String sql = "SELECT review_id FROM performance_review " +
                "WHERE employee_id=? ORDER BY review_date DESC LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("review_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // not found
    }

    public List<PerformanceReview> getReviewsByEmpId(int empId) {
        List<PerformanceReview> reviews = new ArrayList<>();

        String sql = "SELECT * FROM performance_review WHERE employee_id = ? ORDER BY review_year DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PerformanceReview r = new PerformanceReview();

                r.setReviewId(rs.getInt("review_id"));
                r.setEmployeeId(rs.getInt("employee_id"));
                r.setReviewYear(rs.getInt("review_year"));
                r.setKeyDeliverables(rs.getString("key_deliverables"));
                r.setAccomplishments(rs.getString("accomplishments"));
                r.setAreasOfImprovement(rs.getString("areas_of_improvement"));
                r.setSelfRating(rs.getInt("self_rating"));
                r.setManagerRating(rs.getInt("manager_rating"));
                r.setManagerComment(rs.getString("manager_comments"));
                r.setStatus(rs.getString("status"));
                r.setReviewDate(rs.getDate("review_date"));

                reviews.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

public boolean submitSelfReview(PerformanceReview review) {
    // 1. Ensure these column names match your DB EXACTLY
    String sql = "INSERT INTO performance_review (employee_id, review_year, key_deliverables, " +
            "accomplishments, areas_of_improvement, self_rating, status, review_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, review.getEmployeeId());
        ps.setInt(2, review.getReviewYear());
        ps.setString(3, review.getKeyDeliverables());
        ps.setString(4, review.getAccomplishments());
        ps.setString(5, review.getAreasOfImprovement());
        ps.setInt(6, review.getSelfRating());
        ps.setString(7, "PENDING");
        ps.setDate(8, new java.sql.Date(System.currentTimeMillis()));

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        // THIS LINE WILL TELL YOU WHY IT FAILED
        System.out.println("DEBUG: SQL Error -> " + e.getMessage());
        return false;
    }
}
}
