package service;

import dao.UserDAO;
import entity.User;
import exception.ValidationException;
import java.io.IOException;
import java.util.List;
import validator.UserValidator;
import validator.ValidationResult;

public class UserService {

    /**
     * Data Access Object для роботи з користувачами.
     */
    private UserDAO userDAO;

    /**
     * Конструктор. Ініціалізує UserDAO для роботи з даними.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Реєструє нового користувача з валідацією даних.
     *
     * @param name ім'я користувача
     * @param email email адреса користувача
     * @param password пароль користувача
     * @param phone номер телефону користувача
     * @return створений користувач або null, якщо реєстрація не вдалася
     * @throws ValidationException якщо дані невалідні
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public User registerUser(String name, String email, String password, String phone)
            throws ValidationException, IOException {

        ValidationResult validationResult = UserValidator.validate(name, email, password, phone);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult);
        }

        // Перевірка email
        if (userDAO.findByEmail(email) != null) {
            ValidationResult duplicateResult = new ValidationResult();
            duplicateResult.addError("email", "Користувач з таким email вже існує");
            throw new ValidationException(duplicateResult);
        }

        // Створення користувача
        User user = new User(0, name.trim(), email.trim().toLowerCase(), password, phone.trim());
        boolean created = userDAO.create(user);

        if (created) {
            return user;
        }

        return null;
    }

    /**
     * Автентифікує користувача за email та паролем.
     *
     * @param email email адреса користувача
     * @param password пароль користувача
     * @return користувач, якщо автентифікація успішна, null - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public User loginUser(String email, String password) throws IOException {
        if (email == null || password == null) {
            return null;
        }

        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user != null && user.checkPassword(password)) {
            return user;
        }

        return null;
    }

    /**
     * Отримує користувача за ідентифікатором.
     *
     * @param id ідентифікатор користувача
     * @return користувач або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public User getUserById(int id) throws IOException {
        return userDAO.findById(id);
    }

    /**
     * Отримує всіх користувачів.
     *
     * @return список всіх користувачів
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<User> getAllUsers() throws IOException {
        return userDAO.findAll();
    }
}
