package presentation;

import entity.User;
import entity.Service;
import entity.Review;
import service.UserService;
import service.ServiceService;
import service.ReviewService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class GuestBookUI {
    /**
     * Сканер для читання введення користувача.
     */
    private Scanner scanner;
    
    /**
     * Сервіс для роботи з користувачами.
     */
    private UserService userService;
    
    /**
     * Сервіс для роботи з послугами.
     */
    private ServiceService serviceService;
    
    /**
     * Сервіс для роботи з відгуками.
     */
    private ReviewService reviewService;
    
    /**
     * Поточний авторизований користувач.
     */
    private User currentUser;

    /**
     * Конструктор.
     * Ініціалізує сервіси та сканер.
     */
    public GuestBookUI() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService();
        this.serviceService = new ServiceService();
        this.reviewService = new ReviewService();
    }

    /**
     * Запускає головне меню програми.
     */
    public void start() {
        printWelcomeMessage();
        
        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    /**
     * Виводить привітальне повідомлення.
     */
    private void printWelcomeMessage() {
        System.out.println("========================================");
        System.out.println("   Гостьова книга басейну");
        System.out.println("========================================");
        System.out.println();
    }

    /**
     * Показує головне меню для неавторизованих користувачів.
     */
    private void showMainMenu() {
        System.out.println("\n--- Головне меню ---");
        System.out.println("1. Реєстрація");
        System.out.println("2. Вхід");
        System.out.println("3. Переглянути послуги");
        System.out.println("4. Переглянути відгуки");
        System.out.println("0. Вихід");
        System.out.print("Виберіть опцію: ");
        
        int choice = readInt();
        
        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            case 3:
                showAllServices();
                break;
            case 4:
                showAllReviews();
                break;
            case 0:
                System.out.println("До побачення!");
                System.exit(0);
                break;
            default:
                System.out.println("Невірний вибір!");
        }
    }

    /**
     * Показує меню для авторизованих користувачів.
     */
    private void showUserMenu() {
        System.out.println("\n--- Меню користувача (" + currentUser.getName() + ") ---");
        System.out.println("1. Переглянути послуги");
        System.out.println("2. Залишити відгук");
        System.out.println("3. Переглянути відгуки");
        System.out.println("4. Мої відгуки");
        System.out.println("5. Вихід з акаунту");
        System.out.println("0. Вихід з програми");
        System.out.print("Виберіть опцію: ");
        
        int choice = readInt();
        
        switch (choice) {
            case 1:
                showAllServices();
                break;
            case 2:
                createReview();
                break;
            case 3:
                showAllReviews();
                break;
            case 4:
                showMyReviews();
                break;
            case 5:
                currentUser = null;
                System.out.println("Ви вийшли з акаунту.");
                break;
            case 0:
                System.out.println("До побачення!");
                System.exit(0);
                break;
            default:
                System.out.println("Невірний вибір!");
        }
    }

    /**
     * Реєструє нового користувача.
     */
    private void registerUser() {
        System.out.println("\n--- Реєстрація ---");
        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine();
        
        System.out.print("Введіть email: ");
        String email = scanner.nextLine();
        
        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();
        
        try {
            User user = userService.registerUser(name, email, password);
            if (user != null) {
                System.out.println("Реєстрація успішна! Ваш ID: " + user.getId());
                currentUser = user;
            } else {
                System.out.println("Помилка реєстрації.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Помилка збереження даних: " + e.getMessage());
        }
    }

    /**
     * Авторизує користувача.
     */
    private void loginUser() {
        System.out.println("\n--- Вхід ---");
        System.out.print("Введіть email: ");
        String email = scanner.nextLine();
        
        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();
        
        try {
            User user = userService.loginUser(email, password);
            if (user != null) {
                System.out.println("Вхід успішний! Вітаємо, " + user.getName() + "!");
                currentUser = user;
            } else {
                System.out.println("Невірний email або пароль.");
            }
        } catch (IOException e) {
            System.out.println("Помилка читання даних: " + e.getMessage());
        }
    }

    /**
     * Показує всі послуги басейну.
     */
    private void showAllServices() {
        System.out.println("\n--- Послуги басейну ---");
        try {
            List<Service> services = serviceService.getAllServices();
            if (services.isEmpty()) {
                System.out.println("Послуг поки що немає.");
            } else {
                for (Service service : services) {
                    System.out.println("ID: " + service.getId());
                    System.out.println("Назва: " + service.getName());
                    System.out.println("Опис: " + service.getDescription());
                    System.out.println("---");
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання даних: " + e.getMessage());
        }
    }

    /**
     * Створює новий відгук.
     */
    private void createReview() {
        if (currentUser == null) {
            System.out.println("Спочатку увійдіть в систему.");
            return;
        }
        
        System.out.println("\n--- Залишити відгук ---");
        
        try {
            // Показуємо послуги
            List<Service> services = serviceService.getAllServices();
            if (services.isEmpty()) {
                System.out.println("Послуг поки що немає.");
                return;
            }
            
            System.out.println("Доступні послуги:");
            for (Service service : services) {
                System.out.println(service.getId() + ". " + service.getName());
            }
            
            System.out.print("Виберіть ID послуги: ");
            int serviceId = readInt();
            
            System.out.print("Введіть текст відгуку (мінімум 10 символів): ");
            String text = scanner.nextLine();
            
            System.out.print("Введіть оцінку (1-5): ");
            int rating = readInt();
            
            Review review = reviewService.createReview(currentUser.getId(), serviceId, text, rating);
            if (review != null) {
                System.out.println("Відгук успішно додано!");
            } else {
                System.out.println("Помилка створення відгуку.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Помилка збереження даних: " + e.getMessage());
        }
    }

    /**
     * Показує всі відгуки.
     */
    private void showAllReviews() {
        System.out.println("\n--- Всі відгуки ---");
        try {
            List<Review> reviews = reviewService.getAllReviews();
            if (reviews.isEmpty()) {
                System.out.println("Відгуків поки що немає.");
            } else {
                for (Review review : reviews) {
                    User user = userService.getUserById(review.getUserId());
                    Service service = serviceService.getServiceById(review.getServiceId());
                    System.out.println("Відгук #" + review.getId());
                    System.out.println("Користувач: " + (user != null ? user.getName() : "Невідомий"));
                    System.out.println("Послуга: " + (service != null ? service.getName() : "Невідома"));
                    System.out.println("Оцінка: " + review.getRating() + "/5");
                    System.out.println("Текст: " + review.getText());
                    System.out.println("Дата: " + review.getCreatedAt());
                    System.out.println("---");
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання даних: " + e.getMessage());
        }
    }

    /**
     * Показує відгуки поточного користувача.
     */
    private void showMyReviews() {
        if (currentUser == null) {
            System.out.println("Спочатку увійдіть в систему.");
            return;
        }
        
        System.out.println("\n--- Мої відгуки ---");
        try {
            List<Review> reviews = reviewService.getReviewsByUser(currentUser.getId());
            if (reviews.isEmpty()) {
                System.out.println("У вас поки що немає відгуків.");
            } else {
                for (Review review : reviews) {
                    Service service = serviceService.getServiceById(review.getServiceId());
                    System.out.println("Відгук #" + review.getId());
                    System.out.println("Послуга: " + (service != null ? service.getName() : "Невідома"));
                    System.out.println("Оцінка: " + review.getRating() + "/5");
                    System.out.println("Текст: " + review.getText());
                    System.out.println("Дата: " + review.getCreatedAt());
                    System.out.println("---");
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання даних: " + e.getMessage());
        }
    }

    /**
     * Читає ціле число з консолі.
     * 
     * @return введене число або -1 при помилці
     */
    private int readInt() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

