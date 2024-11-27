package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL do JawsDB Maria
    private static final String URL = "jdbc:mariadb://dcrhg4kh56j13bnu.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/qu6tazf43atxvz69";
    private static final String USER = "h68gnuptrh1xn09o"; // Usuário do JawsDB
    private static final String PASSWORD = "e0ph575wcia2beq6"; // Senha do JawsDB

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
