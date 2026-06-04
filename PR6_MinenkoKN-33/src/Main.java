
import presentation.GuestBookUI;
import util.DatabaseInit;

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
