package util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInit {

  public static void InsertTestData() {
    try (Connection conn = DatabaseCfg.getConnection();
        Statement st = conn.createStatement()) {
      st.execute("""
          INSERT INTO users (id, name, email, hashedPassword) VALUES
          (1, 'Іван Петров', 'ivan.petrov@example.com', 'hashedpassword123'),
          (2, 'Марія Сидорова', 'maria.sidorova@example.com', 'hashedpassword456');
          """);

      st.execute("""
          INSERT INTO services (id, name, description) VALUES
          (101, 'Консультація', 'Детальна консультація по проекту'),
          (102, 'Розробка ПЗ', 'Розробка програмного забезпечення на замовлення');
          """);

      st.execute("""
          INSERT INTO reviews (id, userID, serviceID, text, rating) VALUES
          (1001, 1, 101, 'Відмінна консультація, дуже задоволений!', 5),
          (1002, 2, 102, 'Швидка та якісна розробка.', 4);
          """);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void Init() {
    try (Connection conn = DatabaseCfg.getConnection();
        Statement st = conn.createStatement()) {

      st.execute("""
          CREATE TABLE IF NOT EXISTS users (
              id INT PRIMARY KEY AUTO_INCREMENT,
              name VARCHAR(255),
              email VARCHAR(255),
              hashedPassword VARCHAR(255)
          );
          """);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void InitS() {
    try (Connection conn = DatabaseCfg.getConnection();
        Statement st = conn.createStatement()) {

      st.execute("""
          CREATE TABLE IF NOT EXISTS services (
              id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255),
            description VARCHAR(255)
          );
          """);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void InitR() {
    try (Connection conn = DatabaseCfg.getConnection();
        Statement st = conn.createStatement()) {

      st.execute("""
          CREATE TABLE IF NOT EXISTS reviews (
              id INT PRIMARY KEY AUTO_INCREMENT,
              userID INT,
              serviceID INT,
              text VARCHAR(255),
              rating INT
          );
          """);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
