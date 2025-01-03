package Controller;

import Model.Class.Singleton.SingletonManger;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class DriverOrderService {

    private static Lokasi fetchRegionLocation(int ID_Wilayah) throws SQLException{
        Connection conn = DatabaseHandler.connect();
        
        String query = "SELECT * FROM wilayah WHERE ID_Wilayah = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, ID_Wilayah);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Lokasi(
                rs.getInt("ID_Wilayah"),
                rs.getString("kelurahan"),
                rs.getString("kecamatan"),
                rs.getString("kota"),
                rs.getDouble("garisLintang"),
                rs.getDouble("garisBujur"),
                rs.getString("alamat")
            );
        }
        return null;
    }

    private static Voucher fetchVoucher(int ID_Voucher) throws SQLException{
        Connection conn = DatabaseHandler.connect();

        String query = "SELECT * FROM Voucher WHERE ID_Voucher = ?";
        PreparedStatement pstmt =  conn.prepareStatement(query);
        pstmt.setInt(1, ID_Voucher);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Voucher(
                rs.getInt("ID_Voucher"), 
                rs.getString("kodeVoucher"), 
                rs.getDouble("jumlahPotongan"), 
                new Date(rs.getTimestamp("valid_from").getTime()), 
                new Date(rs.getTimestamp("valid_to").getTime()), 
                new Date(rs.getTimestamp("created_at").getTime()), 
                new Date(rs.getTimestamp("updated_at").getTime())
            );
        }
        return null;
    }

    public static double[] getLocationCoordinates(String kelurahan)throws SQLException{
        double[] coordinates = new double[2];

        Connection conn = DatabaseHandler.connect();

        String query = "SELECT * FROM wilayah WHERE kelurahan = '" + kelurahan + "'";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            coordinates[0] = rs.getDouble("garisLintang");
            coordinates[1] = rs.getDouble("garisBujur");
        }
        return coordinates;
    }

    public static String cekServiceType(int ID_Driver) throws SQLException{
        Connection conn = DatabaseManager.connect();
        
        String query = "SELECT v.vehicleType FROM vehicle v INNER JOIN users u ON v.vehicle_ID = u.vehicle_ID WHERE u.ID_User = " + ID_Driver + " ";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            if (rs.getString("vehicleType").equalsIgnoreCase("Motorcyle")) {
                return "GrabBike";
            }else if(rs.getString("vehicleType").equalsIgnoreCase("Car")){
                return "GrabCar";
            }
        } 
        return "";
    } 

    public static List<Order> fetchOrders(int ID_Driver) throws SQLException {
        Connection conn = DatabaseHandler.connect();

        String query = "SELECT * FROM order WHERE order_status IS NULL AND ";
        
        String serviceType = cekServiceType(ID_Driver);  
        if (serviceType.equals("GrabBike")) {
            query += "serviceType = 'GrabBike'";
        } else if (serviceType.equals("GrabCar")) {
            query += "serviceType = 'GrabCar'";
        }

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Voucher voucher = fetchVoucher(rs.getInt("ID_Voucher"));
            
            int ID_WilayahPickUp = rs.getInt("ID_WilayahPickUp");
            int ID_WilayahDestination = rs.getInt("ID_WilayahDestination");
            Lokasi lokasiPickUp = fetchRegionLocation(ID_WilayahPickUp);
            Lokasi lokasiDestionation = fetchRegionLocation(ID_WilayahDestination);

            LocalDateTime updateOrder = rs.getObject("updateOrder", LocalDateTime.class);
            Date updateOrderConvert = Date.from(updateOrder.atZone(ZoneId.systemDefault()).toInstant());

            if (lokasiPickUp != null && lokasiDestionation != null) {
                Order order = new Order(
                    rs.getInt("ID_Order"), 
                    rs.getInt("ID_Driver"), 
                    rs.getInt("ID_Customer"), 
                    voucher, 
                    lokasiPickUp, 
                    lokasiDestionation, 
                    ServiceType.valueOf(rs.getString("serviceType")), 
                    OrderStatus.valueOf(rs.getString("order_status")), 
                    new Date(rs.getTimestamp("order_date").getTime()), 
                    updateOrderConvert, 
                    PaymentMethod.valueOf(rs.getString("paymentMethod")), 
                    rs.getDouble("price"), 
                    rs.getDouble("rating"),
                    rs.getString("ulasan")
                );
                orders.add(order);
            }
        }
        return orders;
    }

    public static void confirmOrder(int ID_Order) throws SQLException{
        Connection conn = DatabaseHandler.connect();

        String query = "UPDATE order SET order_status = 'On_Progress' AND ID_Driver = ? WHERE ID_Order = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
        stmt.setInt(2, ID_Order);
        stmt.executeUpdate();

        String query2 = "UPDATE users SET availability = 'Occupied' WHERE ID_Driver = ?";
        PreparedStatement stmt2 = conn.prepareStatement(query2);
        stmt2.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
        stmt2.executeUpdate();        
    }

    public static Order fetchCurrentOrder(int ID_Driver) throws SQLException{
        Connection conn = DatabaseManager.connect();
        
        String query = "SELECT * FROM order WHERE ID_Driver = ? AND order_status = 'On_Progress'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_Driver);
        ResultSet rs = stmt.executeQuery();

        Order currentOrder = null;
        if (rs.next()) {
            Voucher voucher = fetchVoucher(rs.getInt("ID_Voucher"));

            int ID_WilayahPickUp = rs.getInt("ID_WilayahPickUp");
            int ID_WilayahDestination = rs.getInt("ID_WilayahDestination");
            Lokasi lokasiPickUp = fetchRegionLocation(ID_WilayahPickUp);
            Lokasi lokasiDestionation = fetchRegionLocation(ID_WilayahDestination);

            LocalDateTime updateOrder = rs.getObject("updateOrder", LocalDateTime.class);
            Date updateOrderConvert = Date.from(updateOrder.atZone(ZoneId.systemDefault()).toInstant());

            if (lokasiPickUp != null && lokasiDestionation != null) {
                currentOrder = new Order(
                    rs.getInt("ID_Order"), 
                    ID_Driver, 
                    rs.getInt("ID_Customer"), 
                    voucher, 
                    lokasiPickUp, 
                    lokasiDestionation, 
                    ServiceType.valueOf(rs.getString("serviceType")), 
                    OrderStatus.valueOf(rs.getString("order_status")), 
                    new Date(rs.getTimestamp("order_date").getTime()), 
                    updateOrderConvert, 
                    PaymentMethod.valueOf(rs.getString("paymentMethod")), 
                    rs.getDouble("price"), 
                    rs.getDouble("rating"),
                    rs.getString("ulasan")
                );
            }
        }
        return currentOrder;
    }

    public static void completeOrder(int ID_Order) throws SQLException{
        Connection conn = DatabaseManager.connect();
        String query = "UPDATE order SET order_status = 'COMPLETE' WHERE ID_Order = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_Order);
        stmt.executeUpdate();
    }

    public static List<Order> fetchCompletedOrders(int ID_Driver) throws SQLException{
        Connection conn = DatabaseHandler.connect();
        
        String query = "SELECT * FROM order WHERE ID_Driver = ? AND order_status = 'COMPLETE'";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, ID_Driver);
        ResultSet rs = stmt.executeQuery();
        
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            int ID_WilayahPickUp = rs.getInt("ID_WilayahPickUp");
            int ID_WilayahDestination = rs.getInt("ID_WilayahDestination");

            Voucher voucher = fetchVoucher(rs.getInt("ID_Voucher"));

            Lokasi lokasiPickUp = fetchRegionLocation(ID_WilayahPickUp);
            Lokasi lokasiDestionation = fetchRegionLocation(ID_WilayahDestination);

            LocalDateTime updateOrder = rs.getObject("updateOrder", LocalDateTime.class);
            Date updateOrderConvert = Date.from(updateOrder.atZone(ZoneId.systemDefault()).toInstant());

            Order order = new Order(
                rs.getInt("ID_Order"), 
                rs.getInt("ID_Driver"), 
                rs.getInt("ID_Customer"), 
                voucher, 
                lokasiPickUp, 
                lokasiDestionation, 
                ServiceType.valueOf(rs.getString("serviceType")), 
                OrderStatus.valueOf(rs.getString("order_status")), 
                new Date(rs.getTimestamp("order_date").getTime()), 
                updateOrderConvert, 
                PaymentMethod.valueOf(rs.getString("paymentMethod")), 
                rs.getDouble("price"), 
                rs.getDouble("rating"),
                rs.getString("ulasan")
            );
            orders.add(order);
        }
        return orders;
    }

    // public static double calculateDriverIncomes(int ID_Driver){
    //     double totalIncomes = 0.0;
    //     DatabaseHandler conn = new DatabaseHandler();
    //     conn.connect();

    //     String query = "SELECT SUM(price) AS total_income FROM order WHERE ID_Driver = ? AND order_status = 'COMPLETE'";
    //     PreparedStatement stmt = conn.con.prepareStatement(query);
    //     stmt.setInt(1, ID_Driver);
    //     ResultSet rs = stmt.executeQuery();

    //     if (rs.next()) {
    //         totalIncomes = rs.getDouble("total_income");
    //     }

    //     conn.disconnect();
    //     return totalIncomes;
    // }

}
