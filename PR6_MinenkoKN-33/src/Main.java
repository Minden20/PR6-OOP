import dao.ReviewDAO;
import dao.ServiceDAO;
import dao.UserDAO;
import entity.Review;
import entity.Service;
import entity.User;
import java.io.IOException;
import util.DatabaseInit;

public class Main {
    public static void main(String[] args) {
        UserDAO user = new UserDAO();
        ServiceDAO service = new ServiceDAO();
        ReviewDAO review = new ReviewDAO();
        
        // ... (Ініціалізація та вставка даних)
        DatabaseInit.Init();
        DatabaseInit.InitS();
        DatabaseInit.InitR();
        DatabaseInit.InsertTestData();

        int targetUserId = 1;
        int targetServiceId = 101;
        int targetReviewId = 1001;

        // 1. Виведення одного користувача
        User singleUser = user.findById(targetUserId);
        if (singleUser != null) {
            System.out.println("--- Один Користувач (ID " + targetUserId + ") ---");
            System.out.println("Ім'я: " + singleUser.getName());
            System.out.println("Email: " + singleUser.getEmail());
        } else {
            System.out.println("Користувача з ID " + targetUserId + " не знайдено.");
        }

        // 2. Виведення однієї послуги
        Service singleService = service.findById(targetServiceId);
        if (singleService != null) {
            System.out.println("\n--- Одна Послуга (ID " + targetServiceId + ") ---");
            System.out.println("Назва: " + singleService.getName());
            System.out.println("Опис: " + singleService.getDescription());
        }

        // 3. Виведення одного відгуку
        try {
            Review singleReview = review.findById(targetReviewId);
            if (singleReview != null) {
                System.out.println("\n--- Один Відгук (ID " + targetReviewId + ") ---");
                System.out.println("Рейтинг: " + singleReview.getRating());
                System.out.println("Текст: " + singleReview.getText());
            }
        } catch (IOException e) {
            System.err.println("Помилка при отриманні відгуку: " + e.getMessage());
        }
    }
}