package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Model.Class.User.Driver;
import Model.Enum.PaymentMethod;
import Model.Class.Db.DatabaseHandler;

public class CompleteOrder {
    
    public void completeOrder(int ID_Order, Driver driver, JFrame view) {
        if (driver != null) {
            try (Connection conn = DatabaseHandler.connect()) {
                String query = "UPDATE `order` SET order_status = 'Complete', ID_Driver = ? WHERE ID_Order = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, driver.getID_Driver());
                stmt.setInt(2, ID_Order);
                stmt.executeUpdate();

                String query2 = "SELECT * FROM `order` WHERE ID_Order = ?";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setInt(1, ID_Order);
                try (ResultSet rs = stmt2.executeQuery()) {
                    if (rs.next()) {
                        double price = rs.getDouble("price");
                        PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("paymentMethod").trim().toUpperCase());
                        if (paymentMethod == PaymentMethod.OVO) {
                            double getPlusSaldo = price + driver.getOvoDriver().getSaldo();
                            String queryUpdateSaldo = "UPDATE users SET availabilityDriver = 'Online' WHERE ID_User = ?";
                                  
                        }
                    } else {
        
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                String query3 = "UPDATE users SET availabilityDriver = 'Online' WHERE ID_User = ?";
                PreparedStatement stmt3 = conn.prepareStatement(query3);
                stmt3.setInt(1, driver.getID_Driver());
                stmt3.executeUpdate();        
                JOptionPane.showMessageDialog(view, "Order selesai!","Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat menyelesaikan order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
