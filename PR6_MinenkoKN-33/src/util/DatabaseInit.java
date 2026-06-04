package util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInit {

    public static void InsertTestData() {
        try (Connection conn = DatabaseCfg.getConnection(); Statement st = conn.createStatement()) {
            st.execute("DELETE FROM reviews;");
            st.execute("DELETE FROM services;");
            st.execute("DELETE FROM users;");

            st.execute("""
          INSERT INTO users (id, name, email, hashedPassword, phone) VALUES
          (1, 'Іван Петров', 'ivan.petrov@example.com', 'hashedpassword123', '+380501234567'),
          (2, 'Марія Сидорова', 'maria.sidorova@example.com', 'hashedpassword456', '+380671234567');
          """);

            st.execute("""
          INSERT INTO services (id, name, description) VALUES
          (1, 'Басейн', '4 години плавання в басейні'),
          (2, 'Дитячий басейн', '4 години плавання в басейні для дітей');
          """);

            st.execute("""
          INSERT INTO reviews (id, userID, serviceID, text, rating) VALUES
          (1, 1, 1, 'Відміний басейн чистий та великий.', 5),
          (2, 2, 2, 'Хороший басейн мілкий, через що не так страшно за дитину', 4);
          """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Init() {
        try (Connection conn = DatabaseCfg.getConnection(); Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS users;");

            st.execute("""
          CREATE TABLE IF NOT EXISTS users (
              id INT PRIMARY KEY AUTO_INCREMENT,
              name VARCHAR(255),
              email VARCHAR(255),
              hashedPassword VARCHAR(255),
              phone VARCHAR(255)
          );
          """);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void InitS() {
        try (Connection conn = DatabaseCfg.getConnection(); Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS services;");
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
        try (Connection conn = DatabaseCfg.getConnection(); Statement st = conn.createStatement()) {

            st.execute("DROP TABLE IF EXISTS reviews;");
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
