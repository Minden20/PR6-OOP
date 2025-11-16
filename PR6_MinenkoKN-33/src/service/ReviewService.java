package service;

import entity.Review;
import entity.User;
import entity.Service;
import dao.ReviewDAO;
import dao.UserDAO;
import dao.ServiceDAO;

import java.io.IOException;
import java.util.List;

public class ReviewService {
    /**
     * Data Access Object для роботи з відгуками.
     */
    private ReviewDAO reviewDAO;
    
    /**
     * Data Access Object для роботи з користувачами.
     */
    private UserDAO userDAO;
    
    /**
     * Data Access Object для роботи з послугами.
     */
    private ServiceDAO serviceDAO;

    /**
     * Конструктор.
     * Ініціалізує DAO об'єкти для роботи з даними.
     */
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
        this.userDAO = new UserDAO();
        this.serviceDAO = new ServiceDAO();
    }

    /**
     * Створює новий відгук з валідацією даних.
     * 
     * @param userId ідентифікатор користувача, який залишає відгук
     * @param serviceId ідентифікатор послуги, про яку відгук
     * @param text текст відгуку
     * @param rating оцінка послуги (від 1 до 5)
     * @return створений відгук або null, якщо створення не вдалося
     * @throws IllegalArgumentException якщо дані невалідні
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Review createReview(int userId, int serviceId, String text, int rating) 
            throws IllegalArgumentException, IOException {
        // Перевірка існування користувача
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Користувач з таким ID не існує");
        }
        
        // Перевірка існування послуги
        Service service = serviceDAO.findById(serviceId);
        if (service == null) {
            throw new IllegalArgumentException("Послуга з таким ID не існує");
        }
        
        // Валідація тексту відгуку
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст відгуку не може бути порожнім");
        }
        
        if (text.trim().length() < 10) {
            throw new IllegalArgumentException("Текст відгуку повинен містити мінімум 10 символів");
        }
        
        // Валідація оцінки
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Оцінка повинна бути від 1 до 5");
        }
        
        // Створення відгуку
        Review review = new Review(0, userId, serviceId, text.trim(), rating);
        boolean created = reviewDAO.create(review);
        
        if (created) {
            return review;
        }
        
        return null;
    }

    /**
     * Отримує відгук за ідентифікатором.
     * 
     * @param id ідентифікатор відгуку
     * @return відгук або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Review getReviewById(int id) throws IOException {
        return reviewDAO.findById(id);
    }

    /**
     * Отримує всі відгуки для конкретної послуги.
     * 
     * @param serviceId ідентифікатор послуги
     * @return список відгуків для вказаної послуги
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> getReviewsByService(int serviceId) throws IOException {
        return reviewDAO.findByServiceId(serviceId);
    }

    /**
     * Отримує всі відгуки конкретного користувача.
     * 
     * @param userId ідентифікатор користувача
     * @return список відгуків вказаного користувача
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> getReviewsByUser(int userId) throws IOException {
        return reviewDAO.findByUserId(userId);
    }

    /**
     * Отримує всі відгуки.
     * 
     * @return список всіх відгуків
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Review> getAllReviews() throws IOException {
        return reviewDAO.findAll();
    }

    /**
     * Обчислює середню оцінку для послуги.
     * 
     * @param serviceId ідентифікатор послуги
     * @return середня оцінка послуги або 0, якщо відгуків немає
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public double getAverageRating(int serviceId) throws IOException {
        List<Review> reviews = reviewDAO.findByServiceId(serviceId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        
        return (double) sum / reviews.size();
    }
}

