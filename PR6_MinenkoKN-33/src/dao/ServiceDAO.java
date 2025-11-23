package dao;

import entity.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseCfg;
import util.DatabaseInit;

public class ServiceDAO {

  // Видалено FILE_PATH та fileHandler

  public ServiceDAO() {
    DatabaseInit.InitS();
  }

  // Приватний helper-метод для створення об'єкта Service з результату запиту
  private Service extractServiceFromResultSet(ResultSet rs) throws SQLException {
    Integer id = rs.getInt("id");
    String name = rs.getString("name");
    String description = rs.getString("description");
    return new Service(id, name, description);
  }

  // --- C.R.U.D. Операції ---

  public boolean create(Service service) {
    String sql = "INSERT INTO services (name, description) VALUES (?, ?)";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, service.getName());
      ps.setString(2, service.getDescription());

      int affectedRows = ps.executeUpdate();

      if (affectedRows > 0) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            service.setId(rs.getInt(1)); // Встановлення згенерованого ID
          }
        }
        return true;
      }
      return false;

    } catch (SQLException e) {
      throw new RuntimeException("Помилка створення послуги: " + e.getMessage(), e);
    }
  }

  public Service findById(int id) {
    String sql = "SELECT id, name, description FROM services WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return extractServiceFromResultSet(rs);
        }
      }
      return null;

    } catch (SQLException e) {
      throw new RuntimeException("Помилка пошуку послуги за ID: " + e.getMessage(), e);
    }
  }

  public List<Service> findAll() {
    List<Service> services = new ArrayList<>();
    String sqlQuery = "SELECT id, name, description FROM services";

    try (Connection conn = DatabaseCfg.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery)) {

      while (rs.next()) {
        services.add(extractServiceFromResultSet(rs));
      }

    } catch (SQLException e) {
      throw new RuntimeException("Помилка отримання всіх послуг: " + e.getMessage(), e);
    }

    return services;
  }

  public boolean update(Service service) {
    String sql = "UPDATE services SET name = ?, description = ? WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, service.getName());
      ps.setString(2, service.getDescription());
      ps.setInt(3, service.getId());

      return ps.executeUpdate() > 0;

    } catch (SQLException e) {
      throw new RuntimeException("Помилка оновлення послуги: " + e.getMessage(), e);
    }
  }

  public boolean delete(int id) {
    String sql = "DELETE FROM services WHERE id = ?";
    try (Connection conn = DatabaseCfg.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Помилка видалення послуги: " + e.getMessage(), e);
    }
  }

}