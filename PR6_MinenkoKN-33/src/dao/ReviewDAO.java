package dao;

import entity.Review;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseCfg;
import util.DatabaseInit;
import util.JsonFileHandler;

public class ReviewDAO {
    /**
     * Шлях до JSON файлу з даними відгуків.
     */
    private static final String FILE_PATH = "data/reviews.json";

    /**
     * Об'єкт для роботи з JSON файлами.
     */
    private JsonFileHandler fileHandler;

    /**
     * Конструктор.
     * Ініціалізує об'єкт для роботи з файлами.
     */
    public ReviewDAO() {
        DatabaseInit.InitR();
    }

    /**
     * Створює новий відгук та зберігає його у файл.
     * 
     * @param review відгук для створення
     * @return true, якщо відгук успішно створений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean create(Review review) throws IOException {
        List<Review> reviews = findAll();
        review.setId(generateId(reviews));
        reviews.add(review);
        return saveAll(reviews);
    }

    /**
     * Знаходить відгук за ідентифікатором.
     * 
     * @param id ідентифікатор відгуку
     * @return відгук з вказаним ідентифікатором або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Review findById(int id) throws IOException {
        List<Review> reviews = findAll();
        for (Review review : reviews) {
            if (review.getId() == id) {
                return review;
            }
        }
        return null;
    }

    /**
     * Знаходить всі відгуки для конкретної послуги.
     * 
     * @param serviceId ідентифікатор послуги
     * @return список відгуків для вказаної послуги
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> findByServiceId(int serviceId) throws IOException {
        List<Review> allReviews = findAll();
        List<Review> serviceReviews = new ArrayList<>();
        for (Review review : allReviews) {
            if (review.getServiceId() == serviceId) {
                serviceReviews.add(review);
            }
        }
        return serviceReviews;
    }

    /**
     * Знаходить всі відгуки конкретного користувача.
     * 
     * @param userId ідентифікатор користувача
     * @return список відгуків вказаного користувача
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> findByUserId(int userId) throws IOException {
        List<Review> allReviews = findAll();
        List<Review> userReviews = new ArrayList<>();
        for (Review review : allReviews) {
            if (review.getUserId() == userId) {
                userReviews.add(review);
            }
        }
        return userReviews;
    }

    /**
     * Отримує всі відгуки з файлу.
     * 
     * @return список всіх відгуків
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> findAll() throws IOException {
        List<Review> reviews = new ArrayList<>();
        String sqlQuery = "SELECT * FROM reviews";
        try (Connection conn = DatabaseCfg.getConnection();
                Statement ps = conn.createStatement();) {
            ResultSet rs = ps.executeQuery(sqlQuery);

            while (rs.next()) {
                Integer ID = rs.getInt("id");
                Integer userID = rs.getInt("userID");
                Integer serviceID = rs.getInt("serviceID");
                String text = rs.getString("text");
                Integer rating = rs.getInt("rating");
                Review review = new Review(ID, userID, serviceID, text, rating);
                reviews.add(review);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reviews;
    }

    /**
     * Оновлює дані відгуку у файлі.
     * 
     * @param review відгук з оновленими даними
     * @return true, якщо відгук успішно оновлений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean update(Review review) throws IOException {
        List<Review> reviews = findAll();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId() == review.getId()) {
                reviews.set(i, review);
                return saveAll(reviews);
            }
        }
        return false;
    }

    /**
     * Видаляє відгук за ідентифікатором.
     * 
     * @param id ідентифікатор відгуку для видалення
     * @return true, якщо відгук успішно видалений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean delete(int id) throws IOException {
        List<Review> reviews = findAll();
        boolean removed = reviews.removeIf(review -> review.getId() == id);
        if (removed) {
            return saveAll(reviews);
        }
        return false;
    }

    /**
     * Зберігає список відгуків у базу даних.
     * 
     * @param reviews список відгуків для збереження
     * @return true, якщо дані успішно збережені
     * @throws IOException якщо виникла помилка при записі файлу
     */
    private boolean saveAll(List<Review> reviews) throws IOException {
        String sql = "INSERT INTO reviews (id, userID, serviceID, text, rating) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseCfg.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            conn.prepareStatement("DELETE FROM reviews").executeUpdate();
            for (Review review : reviews) {
                ps.setInt(1, review.getId());
                ps.setInt(2, review.getUserId());
                ps.setInt(3, review.getServiceId());
                ps.setString(4, review.getText());
                ps.setInt(5, review.getRating());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Генерує новий унікальний ідентифікатор для відгуку.
     * 
     * @param reviews список існуючих відгуків
     * @return новий унікальний ідентифікатор
     */
    private int generateId(List<Review> reviews) {
        if (reviews.isEmpty()) {
            return 1;
        }
        int maxId = reviews.stream().mapToInt(Review::getId).max().orElse(0);
        return maxId + 1;
    }
}
