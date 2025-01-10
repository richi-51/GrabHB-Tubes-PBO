package Controller;

import Model.Class.User.Customer;
import Model.Enum.UserType;
import Model.Class.Db.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProfileCustomer {
    public boolean updateCustomerProfile(String name, String phoneNumber, String email) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE ID_Customer = ?";
        try (Connection conn = DatabaseHandler.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, email);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}