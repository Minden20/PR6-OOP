package dao;

import entity.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseCfg;
import util.DatabaseInit;
import exception.EntityNotFoundException;

public class ReviewDAO {

    private static ReviewDAO instance;

    public ReviewDAO() {
        DatabaseInit.InitR();
    }

    public static synchronized ReviewDAO getInstance() {
        if (instance == null) {
            instance = new ReviewDAO();
        }
        return instance;
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Integer ID = rs.getInt("id");
        Integer userID = rs.getInt("userID");
        Integer serviceID = rs.getInt("serviceID");
        String text = rs.getString("text");
        Integer rating = rs.getInt("rating");
        return new Review(ID, userID, serviceID, text, rating);
    }

    public boolean create(Review review) {
        String sql = "INSERT INTO reviews (userID, serviceID, text, rating) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getServiceId());
            ps.setString(3, review.getText());
            ps.setInt(4, review.getRating());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        review.setId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка створення відгуку: " + e.getMessage(), e);
        }
    }

    public Review findById(int id) {
        String sql = "SELECT id, userID, serviceID, text, rating FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractReviewFromResultSet(rs);
                }
            }
            throw new EntityNotFoundException("Відгук з ID " + id + " не знайдено.");

        } catch (SQLException e) {
            throw new RuntimeException("Помилка пошуку відгуку за ID: " + e.getMessage(), e);
        }
    }

    public List<Review> findByServiceId(int serviceId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, userID, serviceID, text, rating FROM reviews WHERE serviceID = ?";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(extractReviewFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка пошуку відгуків за Service ID: " + e.getMessage(), e);
        }
        return reviews;
    }

    public List<Review> findByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, userID, serviceID, text, rating FROM reviews WHERE userID = ?";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(extractReviewFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка пошуку відгуків за User ID: " + e.getMessage(), e);
        }
        return reviews;
    }

    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        String sqlQuery = "SELECT id, userID, serviceID, text, rating FROM reviews";
        try (Connection conn = DatabaseCfg.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка отримання всіх відгуків: " + e.getMessage(), e);
        }
        return reviews;
    }

    public boolean update(Review review) {
        String sql = "UPDATE reviews SET userID = ?, serviceID = ?, text = ?, rating = ? WHERE id = ?";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getServiceId());
            ps.setString(3, review.getText());
            ps.setInt(4, review.getRating());
            ps.setInt(5, review.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка оновлення відгуку: " + e.getMessage(), e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseCfg.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Помилка видалення відгуку: " + e.getMessage(), e);
        }
    }
}
