package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;
import Model.Class.User.Customer;
import Model.Class.Vehicle.Vehicle;
import Model.Enum.OrderStatus;
import Model.Enum.ServiceType;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Order.Laporan;

public class CustomerController {
    // private Customer customer;
    // private Order order;
    // private Laporan keluhan;

    // public CustomerController(Customer customer) {
    //     this.customer = customer;
    // }

    // public String viewOrder() {
    //     if (customer.getOrder() == null) {
    //         return "Belum ada order";
    //     } else {
    //         order = customer.getOrder();
    //         return "Order " + order.getID_order() +
    //                 ", Pickup Location: " + order.getPickUpLoc() +
    //                 ", Destination: " + order.getDestination() +
    //                 ", Price: " + order.getPrice();
    //     }
    // }

    // public void makeOrder(Order order) { // tinggal update order
    //     if (customer.getOrder() == null) {
    //         customer.setOrder(order);
    //         order.setDestination(null);
    //         order.setPickUpLoc(null);
    //         order.setPaymentMethod(null);
    //         order.setVoucher(null);
    //         order.getServiceType();
    //     }
    // }
    
    // public void seeHistory() {
    //     order = customer.getOrder();
    //     order.getID_order();
    //     order.getID_driver();
    //     order.getPickUpLoc();
    //     order.getDestination();
    //     order.getOrder_date();
    //     order.getOrder_status();
    //     order.getPrice();
    //     order.getServiceType();
    // }

    // public void seeBalance(Ovo ovoE_money) {
    //     customer.getID_Customer();
    //     customer.getOvoE_money();
    // }

    // public void statusAcc(Date createdAccAt) {
    //     customer.getID_Customer();
    //     customer.getCreatedAccAt();
    // }

    // public void makeReport() {
    //     order = customer.getOrder();
    //     order.getKeluhan();
    //     keluhan.setIsiKeluhan(null);
    // }


    public static boolean createOrder(ServiceType service, Vehicle vehicle, String currLoc, int currLocRegionID,
    String destination, int destinationRegionID, double cost) {

String query = "INSERT INTO orders(ID_Order, ID_Customer, vehicle_type, "
        + "current_location, region_id_current, destination, region_id_destination, cost, "
        + "order_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

try (Connection conn = DatabaseHandler.connect(); 
     PreparedStatement preparedStatement = conn.prepareStatement(query)) {

    preparedStatement.setInt(1, 1);
    preparedStatement.setInt(2, 101);
    preparedStatement.setString(3, vehicle.getPlateNumber());
    preparedStatement.setString(4, currLoc);
    preparedStatement.setInt(5, currLocRegionID);
    preparedStatement.setString(6, destination);
    preparedStatement.setInt(7, destinationRegionID);
    preparedStatement.setDouble(8, cost);
    preparedStatement.setString(9, "Pending");
    preparedStatement.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
    preparedStatement.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis()));

    preparedStatement.executeUpdate();
    return true;

} catch (SQLException e) {
    e.printStackTrace();
    return false;
}
}

public void seeHistory(int customerID) {
    String query = "SELECT ID_Order, ID_Driver, current_location AS pick_up_loc, "
                 + "destination, created_at AS order_date, order_status, cost AS price, "
                 + "vehicle_type AS service_type "
                 + "FROM orders WHERE ID_Customer = ? ORDER BY created_at DESC";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        // Set the parameter for the query
        preparedStatement.setInt(1, customerID);

        // Execute the query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int idOrder = resultSet.getInt("ID_Order");
                int idDriver = resultSet.getInt("ID_Driver");
                String pickUpLoc = resultSet.getString("pick_up_loc");
                String destination = resultSet.getString("destination");
                java.sql.Timestamp orderDate = resultSet.getTimestamp("order_date");
                String orderStatus = resultSet.getString("order_status");
                double price = resultSet.getDouble("price");
                String serviceType = resultSet.getString("service_type");

                // Display or process the retrieved data
                System.out.println("Order ID: " + idOrder);
                System.out.println("Driver ID: " + idDriver);
                System.out.println("Pick-Up Location: " + pickUpLoc);
                System.out.println("Destination: " + destination);
                System.out.println("Order Date: " + orderDate);
                System.out.println("Order Status: " + orderStatus);
                System.out.println("Price: " + price);
                System.out.println("Service Type: " + serviceType);
                System.out.println("-----------------------");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public String viewOrder(int customerID) {
    String query = "SELECT ID_Order, current_location AS pick_up_loc, destination, cost AS price "
                 + "FROM orders WHERE ID_Customer = ? AND order_status = 'Active' LIMIT 1";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setInt(1, customerID);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int idOrder = resultSet.getInt("ID_Order");
                String pickUpLoc = resultSet.getString("pick_up_loc");
                String destination = resultSet.getString("destination");
                double price = resultSet.getDouble("price");
                return "Order " + idOrder +
                       ", Pickup Location: " + pickUpLoc +
                       ", Destination: " + destination +
                       ", Price: " + price;
            } else {
                return "Belum ada order";
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return "Something wong";
    }
}

public void seeBalance(int customerID) {
    String query = "SELECT balance FROM e_money WHERE ID_Customer = ?";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setInt(1, customerID);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Saldo OVO/E-Money: " + balance);
            } else {
                System.out.println("Saldo tidak ditemukan untuk ID_Customer: " + customerID);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void statusAcc(int customerID) {
    String query = "SELECT created_at FROM customers WHERE ID_Customer = ?";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement preparedStatement = conn.prepareStatement(query)) {

        preparedStatement.setInt(1, customerID);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                java.sql.Date createdAccAt = resultSet.getDate("created_at");
                System.out.println("Account Created At: " + createdAccAt);
            } else {
                System.out.println("Customer ID not found: " + customerID);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void makeReport(int customerID) {
    String querySelect = "SELECT keluhan FROM orders WHERE ID_Customer = ? AND keluhan IS NOT NULL";
    String queryInsert = "INSERT INTO reports (ID_Customer, isi_keluhan, created_at) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement selectStatement = conn.prepareStatement(querySelect);
         PreparedStatement insertStatement = conn.prepareStatement(queryInsert)) {

        selectStatement.setInt(1, customerID);

        try (ResultSet resultSet = selectStatement.executeQuery()) {
            if (resultSet.next()) {
                String existingComplaint = resultSet.getString("keluhan");
                System.out.println("Existing Complaint: " + existingComplaint);
            } else {
                System.out.println("No existing complaints found for Customer ID: " + customerID);
            }
        }

        insertStatement.setInt(1, customerID);
        insertStatement.setString(2, null);
        insertStatement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));

        int rowsInserted = insertStatement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new report has been created with an empty complaint.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}