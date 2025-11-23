package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseCfg {

  private static final String URL = "jdbc:h2:./data/guestbook_db";
  private static final String User = "sa";
  private static final String Password = "";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, User, Password);
  }
}
