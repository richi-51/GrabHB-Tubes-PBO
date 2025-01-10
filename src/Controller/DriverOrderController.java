package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import Model.Enum.PaymentMethod;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Order.Order;

public class DriverOrderController {

    public interface OrderView {
        void displayOrders(List<Order> orders);
        void displayIncomes(String incomes);
        void displayError(String message);
        int displayConfirm(String question);
    }

    private OrderView view;

    public DriverOrderController(OrderView view) {
        // initializeAvailability();
        this.view = view;
    }

    // private void initializeAvailability() {
    //     Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        
    //     if (loggedInUser == null) {
    //         try (Connection conn = DatabaseHandler.connect()) {
    //             // Query untuk mendapatkan data pengguna dari database
    //             String query = "SELECT ID_User, username, name, password, email, availabilityDriver FROM users WHERE name = 'Adrian'";
    //             PreparedStatement statement = conn.prepareStatement(query);
    
    //             ResultSet resultSet = statement.executeQuery();
    //             if (resultSet.next()) {
    //                 // Ambil data dari hasil query
    //                 int idUser = resultSet.getInt("ID_User");
    //                 String username = resultSet.getString("username");
    //                 String name = resultSet.getString("name");
    //                 String password = resultSet.getString("password");
    //                 String email = resultSet.getString("email");

    //                 // Buat objek Driver dan set atributnya
    //                 loggedInUser = new Driver(idUser, username, name, password, " ", email, null, null, " ", null, DriverStatus.ONLINE, null, null, null, 4, null, null);

    //                 // Simpan ke SingletonManger
    //                 SingletonManger.getInstance().setLoggedInUser(loggedInUser);
    
    //             } else {
    //                 JOptionPane.showMessageDialog(null, 
    //                     "Tidak ada data pengguna di database!", "Error", JOptionPane.ERROR_MESSAGE);
    //             }
    //         } catch (SQLException ex) {
    //             ex.printStackTrace();
    //             JOptionPane.showMessageDialog(null, 
    //                 "Error saat mengambil data pengguna dari database", "Error", JOptionPane.ERROR_MESSAGE);
    //         }
    //     }
    // }

    public void loadOrders() throws SQLException{
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        try {
            List<Order> orders = DriverOrderService.fetchOrders(loggedInUser.getID_Driver());
            view.displayOrders(orders);
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error loading orders: " + ex.getMessage());
        }
    }

    public void confirmOrder(int ID_Order) throws SQLException {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        Connection conn = DatabaseHandler.connect(); 

        String query = "SELECT * FROM `order` WHERE ID_Order = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_Order);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("paymentMethod"));
            if (paymentMethod == PaymentMethod.CASH) {
                double price = rs.getDouble("price")*0.9;
                if (loggedInUser.getOvoDriver().getSaldo() < price) {
                    view.displayError("Saldo Anda tidak mencukupi!!");
                }
                else{
                    try {
                        DriverOrderService.confirmOrder(ID_Order);
                        loggedInUser.setAvailability(DriverStatus.OCCUPIED);
                        view.displayError( "Order confirmed");
                        loadOrders();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        view.displayError("Error confirming order: " + ex.getMessage());
                    }
                }
            }
        }
    }

    public void completeOrder(int ID_Order) throws SQLException {
        try {
            DriverOrderService.completeOrder(ID_Order);
            view.displayError("Order selesai!");
            loadOrders();
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error completing order: " + ex.getMessage());
        }
    }

    public void updatedLocation(int ID_Order) throws SQLException {
        try {
            DriverOrderService.updateLocation(ID_Order);;
            view.displayError("Lokasi Anda diperbaharui!");
            loadOrders();
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error update location: " + ex.getMessage());
        }
    }

    public static Order getCurrentOrder(int ID_Driver) throws SQLException {
        return DriverOrderService.fetchCurrentOrder(ID_Driver);
    }

}
