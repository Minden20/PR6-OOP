package dao;

import entity.Review;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

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
        this.fileHandler = new JsonFileHandler();
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
        
        if (!fileHandler.fileExists(FILE_PATH)) {
            return reviews;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);
            
            for (JsonObject jsonReview : jsonArray) {
                Review review = new Review();
                Object idObj = jsonReview.get("id");
                if (idObj instanceof Long) {
                    review.setId(((Long) idObj).intValue());
                } else if (idObj instanceof Integer) {
                    review.setId((Integer) idObj);
                }
                Object userIdObj = jsonReview.get("userId");
                if (userIdObj instanceof Long) {
                    review.setUserId(((Long) userIdObj).intValue());
                } else if (userIdObj instanceof Integer) {
                    review.setUserId((Integer) userIdObj);
                }
                Object serviceIdObj = jsonReview.get("serviceId");
                if (serviceIdObj instanceof Long) {
                    review.setServiceId(((Long) serviceIdObj).intValue());
                } else if (serviceIdObj instanceof Integer) {
                    review.setServiceId((Integer) serviceIdObj);
                }
                review.setText((String) jsonReview.get("text"));
                Object ratingObj = jsonReview.get("rating");
                if (ratingObj instanceof Long) {
                    review.setRating(((Long) ratingObj).intValue());
                } else if (ratingObj instanceof Integer) {
                    review.setRating((Integer) ratingObj);
                }
                review.setCreatedAt((String) jsonReview.get("createdAt"));
                reviews.add(review);
            }
        } catch (Exception e) {
            throw new IOException("Помилка парсингу JSON файлу", e);
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
     * Зберігає список відгуків у JSON файл.
     * 
     * @param reviews список відгуків для збереження
     * @return true, якщо дані успішно збережені
     * @throws IOException якщо виникла помилка при записі файлу
     */
    private boolean saveAll(List<Review> reviews) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();
        
        for (Review review : reviews) {
            JsonObject jsonReview = new JsonObject();
            jsonReview.put("id", (long) review.getId());
            jsonReview.put("userId", (long) review.getUserId());
            jsonReview.put("serviceId", (long) review.getServiceId());
            jsonReview.put("text", review.getText());
            jsonReview.put("rating", (long) review.getRating());
            jsonReview.put("createdAt", review.getCreatedAt());
            jsonArray.add(jsonReview);
        }
        
        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
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

