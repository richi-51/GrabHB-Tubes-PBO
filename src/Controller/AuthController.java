package Controller;

import Model.Class.User.Admin;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Enum.StatusAcc;
import Model.Enum.UserType;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.*;
import View.LoginForm;
import View.RegisterForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
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
        String usernameEmail = loginView.getUsernameEmail();
        String password = loginView.getPassword();

        try (Connection conn = DatabaseHandler.connect()) {
            String queryUser = "";
            if (usernameEmail.contains("@")) {
                queryUser = "SELECT * FROM Users WHERE email = ? AND password = ?";
            }else{
                queryUser = "SELECT * FROM Users WHERE username = ? AND password = ?";
            }

            // Periksa apakah user ada di tabel Users
            var preparedStatement = conn.prepareStatement(queryUser);
            preparedStatement.setString(1, usernameEmail);
            preparedStatement.setString(2, password);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("ID_User");
                int vehicleId = resultSet.getInt("vehicle_ID");
                String name = resultSet.getString("name");
                String username =  resultSet.getString("username");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phoneNumber");
                Date updateProfileAt = resultSet.getDate("updateProfileAt");
                String userType = resultSet.getString("userType");
                String statusAcc = resultSet.getString("statusAcc");
                Date createdAccAt = resultSet.getDate("createdAccAt");
                System.out.println(updateProfileAt);

                // Simpan data login di Singleton sesuai role
                if ("Admin".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Admin(
                            userId, username, name, password, phone, email, updateProfileAt, UserType.ADMIN
                    ));
                } else if ("Customer".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Customer(
                            userId, username, name, password, phone, email, updateProfileAt, UserType.CUSTOMER, getStatusAcc(statusAcc), createdAccAt, null, null
                    ));
                } else if ("Driver".equalsIgnoreCase(userType)) {
                    SingletonManger.getInstance().setLoggedInUser(new Driver(
                            userId, username, name, password, phone, email,updateProfileAt,  UserType.DRIVER, getStatusAcc(statusAcc), null, createdAccAt, vehicleId, null, null, 0.0
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

    private StatusAcc getStatusAcc(String accStatus){
        return accStatus.equalsIgnoreCase("Blocked") ? StatusAcc.BLOCKED : StatusAcc.UNBLOCKED;
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
