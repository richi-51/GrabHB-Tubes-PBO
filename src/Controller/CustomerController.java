package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    private Customer customer;
    private Order order;
    private Laporan keluhan;

    public CustomerController(Customer customer) {
        this.customer = customer;
    }

    public String viewOrder() {
        if (customer.getOrder() == null) {
            return "Belum ada order";
        } else {
            order = customer.getOrder();
            return "Order " + order.getID_order() +
                    ", Pickup Location: " + order.getPickUpLoc() +
                    ", Destination: " + order.getDestination() +
                    ", Price: " + order.getPrice();
        }
    }

    public void makeOrder(Order order) { // tinggal update order
        if (customer.getOrder() == null) {
            customer.setOrder(order);
            order.setDestination(null);
            order.setPickUpLoc(null);
            order.setPaymentMethod(null);
            order.setVoucher(null);
            order.getServiceType();
        }
    }

    public void seeBalance(Ovo ovoE_money) {
        customer.getID_Customer();
        customer.getOvoE_money();
    }

    public void statusAcc(Date createdAccAt) {
        customer.getID_Customer();
        customer.getCreatedAccAt();
    }

    public void makeReport() {
        // if (order.getOrder_status() == OrderStatus.COMPLETE) {
        // }
        order = customer.getOrder();
        order.getKeluhan();
        keluhan.setIsiKeluhan(null);
    }

    public void seeHistory() {
        order = customer.getOrder();
        order.getID_order();
        order.getID_driver();
        order.getPickUpLoc();
        order.getDestination();
        order.getOrder_date();
        order.getOrder_status();
        order.getPrice();
        order.getServiceType();
    }

    public static boolean createOrder(ServiceType service, Vehicle vehicle, String currLoc, int currLocRegionID,
            String destination, int destinationRegionID, double cost) {
        DatabaseHandler connect = new DatabaseHandler();
        connect.connect();

        String query = "INSERT INTO orders(ID_Order, ID_Customer, vehicle_type, "
                + "current_location, region_id_current, destination, region_id_destination, cost, "
                + "order_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connect.con.prepareStatement(query);

            // Set values for the query
            preparedStatement.setInt(1, 1); // Example: ID_Order
            preparedStatement.setInt(2, 101); // Example: ID_Customer
            preparedStatement.setString(3, "Car"); // Example: vehicle_type
            preparedStatement.setString(4, "Location A"); // Example: current_location
            preparedStatement.setInt(5, 1); // Example: region_id_current
            preparedStatement.setString(6, "Location B"); // Example: destination
            preparedStatement.setInt(7, 2); // Example: region_id_destination
            preparedStatement.setDouble(8, 250.75); // Example: cost
            preparedStatement.setString(9, "Pending"); // Example: order_status
            preparedStatement.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis())); // created_at
            preparedStatement.setTimestamp(11, new java.sql.Timestamp(System.currentTimeMillis())); // updated_at
            preparedStatement.executeUpdate();
            connect.disconnect();
            return (true);

        } catch (SQLException e) {
            e.printStackTrace();
            connect.disconnect();
            return (false);
        }
    }
}