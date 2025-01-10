package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Model.Class.User.Driver;
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

                String query2 = "UPDATE users SET availabilityDriver = 'Online' WHERE ID_User = ?";
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt2.setInt(1, driver.getID_Driver());
                stmt2.executeUpdate();        
                JOptionPane.showMessageDialog(view, "Order selesai!","Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Error saat menyelesaikan order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
