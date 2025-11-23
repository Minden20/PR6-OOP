package dao;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseCfg;
import util.DatabaseInit;

/**
 * Клас для доступу до даних користувачів, використовує H2 базу даних через JDBC.
 */
public class UserDAO {

  // Видаляємо FILE_PATH та fileHandler, оскільки ми використовуємо DB

  /**
   * Конструктор. Ініціалізує базу даних.
   */
  public UserDAO() {
    // Припускаємо, що цей метод створює таблицю, якщо вона не існує
    DatabaseInit.Init();
  }

  // Helper метод для конвертації ResultSet у об'єкт User
  private User extractUserFromResultSet(ResultSet rs) throws SQLException {
    Integer id = rs.getInt("id");
    String name = rs.getString("name");
    String email = rs.getString("email");
    String hashedPassword = rs.getString("hashedPassword");
    return new User(id, name, email, hashedPassword);
  }

  /**
   * Створює нового користувача та зберігає його у базу даних.
   *
   * @param user користувач для створення
   * @return true, якщо користувач успішно створений, false - інакше
   */
  public boolean create(User user) {
    String sql = "INSERT INTO users (name, email, hashedPassword) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getHashedPassword());

      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            user.setId(rs.getInt(1));
          }
        }
        return true;
      }
      return false;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Знаходить користувача за ідентифікатором.
   *
   * @param id ідентифікатор користувача
   * @return користувач з вказаним ідентифікатором або null, якщо не знайдено
   */
  public User findById(int id) {
    String sql = "SELECT id, name, email, hashedPassword FROM users WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return extractUserFromResultSet(rs);
        }
      }
      return null;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Знаходить користувача за email адресою.
   *
   * @param email email адреса користувача
   * @return користувач з вказаним email або null, якщо не знайдено
   */
  public User findByEmail(String email) {
    String sql = "SELECT id, name, email, hashedPassword FROM users WHERE email = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return extractUserFromResultSet(rs);
        }
      }
      return null;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Отримує всіх користувачів з бази даних.
   * (Цей метод був майже правильний, але використовує Statement, що нормально для запиту без параметрів)
   */
  public List<User> findAll() {
    List<User> users = new ArrayList<>();
    String sqlQuery = "SELECT id, name, email, hashedPassword FROM users";
    try (Connection conn = DatabaseCfg.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery)) {

      while (rs.next()) {
        users.add(extractUserFromResultSet(rs));
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return users;
  }

  /**
   * Оновлює дані користувача у базі даних.
   *
   * @param user користувач з оновленими даними
   * @return true, якщо користувач успішно оновлений, false - інакше
   */
  public boolean update(User user) {
    String sql = "UPDATE users SET name = ?, email = ?, hashedPassword = ? WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getHashedPassword());
      ps.setInt(4, user.getId());

      return ps.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Видаляє користувача за ідентифікатором.
   *
   * @param id ідентифікатор користувача для видалення
   * @return true, якщо користувач успішно видалений, false - інакше
   */
  public boolean delete(int id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, id);

      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }


}