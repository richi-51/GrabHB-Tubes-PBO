package Controller;

import Model.Class.User.Admin;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Enum.UserType;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.*;
import View.LoginForm;
import View.RegisterForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class AuthController {
    private LoginForm loginView;
    private RegisterForm registerView;

    public AuthController(LoginForm loginView, RegisterForm registerView) {
        this.loginView = loginView;
        this.registerView = registerView;

        this.loginView.addLoginListener(new LoginAction());
        this.loginView.addRegisterListener(e -> registerView.setVisible(true));
        this.registerView.addRegisterListener(new RegisterAction());
    }

    private class LoginAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        try (Connection conn = DatabaseHandler.connect()) {
            // Periksa apakah user ada di tabel Users
            String queryUser = "SELECT * FROM Users WHERE username = ? AND password = ?";
            var preparedStatement = conn.prepareStatement(queryUser);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("ID_User");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phoneNumber");

                // Periksa role berdasarkan relasi dengan tabel lainnya
                String userType = checkUserRole(conn, userId);

                // Simpan data login di Singleton sesuai role
                if ("Admin".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Admin(
                            username, name, password, phone, email, null, UserType.ADMIN, userId
                    ));
                } else if ("Customer".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Customer(
                            username, name, password, phone, email, null, UserType.CUSTOMER, userId, null, null, null, null
                    ));
                } else if ("Driver".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Driver(
                            username, name, password, phone, email, null, UserType.DRIVER, userId, null, null, null, null, null, 0.0
                    ));
                }

                JOptionPane.showMessageDialog(loginView, "Login successful as " + userType + "!");
            } else {
                JOptionPane.showMessageDialog(loginView, "Invalid username or password!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
        }
    }

    // Method untuk mengecek role user berdasarkan relasi tabel
    private String checkUserRole(Connection conn, int userId) throws SQLException {
        // Periksa apakah user adalah Admin
        String queryAdmin = "SELECT 1 FROM Admin WHERE ID_User = ?";
        try (var psAdmin = conn.prepareStatement(queryAdmin)) {
            psAdmin.setInt(1, userId);
            try (var rsAdmin = psAdmin.executeQuery()) {
                if (rsAdmin.next()) {
                    return "Admin";
                }
            }
        }

        // Periksa apakah user adalah Customer
        String queryCustomer = "SELECT 1 FROM Customer WHERE ID_User = ?";
        try (var psCustomer = conn.prepareStatement(queryCustomer)) {
            psCustomer.setInt(1, userId);
            try (var rsCustomer = psCustomer.executeQuery()) {
                if (rsCustomer.next()) {
                    return "Customer";
                }
            }
        }

        // Periksa apakah user adalah Driver
        String queryDriver = "SELECT 1 FROM Driver WHERE ID_User = ?";
        try (var psDriver = conn.prepareStatement(queryDriver)) {
            psDriver.setInt(1, userId);
            try (var rsDriver = psDriver.executeQuery()) {
                if (rsDriver.next()) {
                    return "Driver";
                }
            }
        }

        // Jika tidak ditemukan
        return "Unknown";
    }
}



private class RegisterAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = registerView.getName();
        String username = registerView.getUsername();
        String email = registerView.getEmail();
        String phone = registerView.getPhoneNumber();
        String password = registerView.getPassword();

        try (Connection conn = DatabaseHandler.connect()) {
            String query = "INSERT INTO Users (name, username, password, phoneNumber, email, updateProfileAt) VALUES (?, ?, ?, ?, ?, NOW())";
            var preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(registerView, "Registration successful!");
                registerView.dispose(); // Menutup form register
            } else {
                JOptionPane.showMessageDialog(registerView, "Registration failed!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(registerView, "Database error: " + ex.getMessage());
        }
    }
}

}
