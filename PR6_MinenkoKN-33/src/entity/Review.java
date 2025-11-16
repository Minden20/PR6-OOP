package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    /**
     * Унікальний ідентифікатор відгуку.
     */
    private int id;
    
    /**
     * Ідентифікатор користувача, який написав відгук.
     */
    private int userId;
    
    /**
     * Ідентифікатор послуги, про яку написано відгук.
     */
    private int serviceId;
    
    /**
     * Текст відгуку.
     */
    private String text;
    
    /**
     * Оцінка послуги (від 1 до 5).
     */
    private int rating;
    
    /**
     * Дата та час створення відгуку.
     */
    private String createdAt;

    /**
     * Конструктор за замовчуванням.
     * Створює порожній об'єкт відгуку.
     */
    public Review() {
    }

    /**
     * Конструктор з параметрами.
     * Створює відгук з вказаними даними та автоматично встановлює поточну дату.
     * 
     * @param id унікальний ідентифікатор відгуку
     * @param userId ідентифікатор користувача, який написав відгук
     * @param serviceId ідентифікатор послуги, про яку відгук
     * @param text текст відгуку
     * @param rating оцінка послуги (від 1 до 5)
     */
    public Review(int id, int userId, int serviceId, String text, int rating) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
        this.text = text;
        this.rating = rating;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Отримує ідентифікатор відгуку.
     * 
     * @return ідентифікатор відгуку
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор відгуку.
     * 
     * @param id ідентифікатор відгуку
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Отримує ідентифікатор користувача, який написав відгук.
     * 
     * @return ідентифікатор користувача
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Встановлює ідентифікатор користувача, який написав відгук.
     * 
     * @param userId ідентифікатор користувача
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Отримує ідентифікатор послуги, про яку написано відгук.
     * 
     * @return ідентифікатор послуги
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Встановлює ідентифікатор послуги, про яку написано відгук.
     * 
     * @param serviceId ідентифікатор послуги
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Отримує текст відгуку.
     * 
     * @return текст відгуку
     */
    public String getText() {
        return text;
    }

    /**
     * Встановлює текст відгуку.
     * 
     * @param text текст відгуку
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Отримує оцінку послуги.
     * 
     * @return оцінка послуги (від 1 до 5)
     */
    public int getRating() {
        return rating;
    }

    /**
     * Встановлює оцінку послуги.
     * 
     * @param rating оцінка послуги (від 1 до 5)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Отримує дату та час створення відгуку.
     * 
     * @return дата та час створення відгуку
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Встановлює дату та час створення відгуку.
     * 
     * @param createdAt дата та час створення відгуку
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{id=" + id + ", userId=" + userId + ", serviceId=" + serviceId + 
               ", rating=" + rating + ", createdAt='" + createdAt + "'}";
    }
}

