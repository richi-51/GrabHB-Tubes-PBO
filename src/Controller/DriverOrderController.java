package Controller;

import java.sql.SQLException;
import java.util.List;

import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
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
        this.view = view;
    }

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
        try {
            DriverOrderService.confirmOrder(ID_Order);
            loggedInUser.setAvailability(DriverStatus.OCCUPIED);
            view.displayError( "Order berhasil diambil");
            loadOrders();
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error confirming order: " + ex.getMessage());
        }
    }

    public void completeOrder(int ID_Order) throws SQLException {
        try {
            DriverOrderService.completeOrder(ID_Order);
            view.displayError("Order completed!");
            loadOrders();
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error completing order: " + ex.getMessage());
        }
    }

    public static Order getCurrentOrder(int ID_Driver) throws SQLException {
        return DriverOrderService.fetchCurrentOrder(ID_Driver);
    }

    public void loadOrderHistory(int ID_Driver) throws SQLException {
        try {
            List<Order> orders = DriverOrderService.fetchCompletedOrders(ID_Driver);
            view.displayOrders(orders);
        } catch (SQLException ex) {
            ex.printStackTrace();
            view.displayError("Error loading order history: " + ex.getMessage());
        }
    }

    // public void calculateDriverIncomes(int ID_Driver) throws SQLException {
    //     try {
    //         double totalIncomes = DriverOrderService.calculateDriverIncomes(ID_Driver);
    //         view.displayIncomes("Total Incomes: Rp " + totalIncomes);
    //     } catch (SQLException ex) {
    //         ex.printStackTrace();
    //         view.displayError("Error calculating incomes: " + ex.getMessage());
    //     }
    // }
}
