package Controller;

import Model.Class.Db.DatabaseHandler;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import Model.Enum.PaymentMethod;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmOrder {
    
    public double confirmOrder(int ID_Order, Driver driver, double saldo) {
        if (driver != null) {
            try (Connection conn = DatabaseHandler.connect()) {
                String query = "SELECT * FROM `order` WHERE ID_Order = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, ID_Order);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("paymentMethod").trim().toUpperCase());
                    double price = rs.getDouble("price");
                    
                    if (paymentMethod == PaymentMethod.CASH) {
                        double totalPrice = price * 0.9;
                        if (driver.getOvoDriver().getSaldo() < totalPrice) {
                            JOptionPane.showMessageDialog(null, "Saldo Anda tidak mencukupi!", "Error", JOptionPane.ERROR_MESSAGE);
                            return 0;
                        }

                        saldo -= totalPrice;
                        
                    }

                    String updateOrderQuery = "UPDATE `order` SET order_status = 'On_Process', ID_Driver = ? WHERE ID_Order = ?";
                    PreparedStatement updateOrderStmt = conn.prepareStatement(updateOrderQuery);
                    updateOrderStmt.setInt(1, driver.getID_Driver());
                    updateOrderStmt.setInt(2, ID_Order);
                    int rows1 = updateOrderStmt.executeUpdate();

                    String updateDriverQuery = "UPDATE users SET availabilityDriver = 'OCCUPIED' WHERE ID_User = ?";
                    PreparedStatement updateDriverStmt = conn.prepareStatement(updateDriverQuery);
                    updateDriverStmt.setInt(1, driver.getID_Driver());
                    int rows2 = updateDriverStmt.executeUpdate();

                    if (rows1 > 0 && rows2 > 0) {
                        driver.setAvailability(DriverStatus.OCCUPIED);

                        JOptionPane.showMessageDialog(null, "Order berhasil dikonfirmasi!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return saldo;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saat mengonfirmasi order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return 0;
    }
}
