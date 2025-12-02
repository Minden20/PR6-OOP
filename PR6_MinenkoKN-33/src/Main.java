
import dao.ReviewDAO;
import dao.ServiceDAO;
import dao.UserDAO;
import dao.DaoFactory;
import entity.Review;
import entity.Service;
import entity.User;
import util.DatabaseInit;
import presentation.GuestBookUI;

public class Main {
    public static void main(String[] args) {
        // Ініціалізація бази даних та вставка тестових даних
        DatabaseInit.Init();
        DatabaseInit.InitS();
        DatabaseInit.InitR();
        DatabaseInit.InsertTestData();

        GuestBookUI ui = new GuestBookUI();
        ui.start();
    }
}