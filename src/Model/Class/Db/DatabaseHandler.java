package Model.Class.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    // URL, Username, dan Password Database
    private static final String URL = "jdbc:mysql://localhost:3306/grab_hb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            // Memuat driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Membuat koneksi
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil!");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
        return connection;
    }
}
