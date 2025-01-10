package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import View.DriverOrderPage;
import Model.Enum.DriverStatus;
import Model.Enum.StatusLaporan;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.Laporan;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;

public class LoadOrderList {
    
    private DriverOrderPage driverOrderPage;

    public LoadOrderList(DriverOrderPage driverOrderPage) {
        this.driverOrderPage = driverOrderPage;
        loadOrders();
    }

    private String cekServiceType(int ID_Driver){        
        try (Connection conn = DatabaseHandler.connect()) {
            System.out.println(ID_Driver);
            String query = "SELECT v.vehicleType FROM vehicle v INNER JOIN users u ON v.vehicle_ID = u.vehicle_ID WHERE u.ID_User = " + ID_Driver + " ";
    
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
    
            if (rs.next()) {
                if (rs.getString("vehicleType").equalsIgnoreCase("Motorcycle")) {
                    return "GrabBike";
                }else if(rs.getString("vehicleType").equalsIgnoreCase("Car")){
                    return "GrabCar";
                }
            } 
            return "";
        } catch (Exception e) {
            
        }
        return "";
    } 
     private Voucher fetchVoucher(int ID_Voucher){
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM Voucher WHERE ID_Voucher = ?";
            PreparedStatement pstmt =  conn.prepareStatement(query);
            pstmt.setInt(1, ID_Voucher);
            ResultSet rs = pstmt.executeQuery();
     
            if (rs.next()) {
                return new Voucher(
                    rs.getInt("ID_Voucher"), 
                    rs.getString("kodeVoucher"), 
                    rs.getDouble("jumlahPotongan"), 
                    ServiceType.valueOf(rs.getString("serviceType")),
                    new Date(rs.getTimestamp("valid_from").getTime()), 
                    new Date(rs.getTimestamp("valid_to").getTime()), 
                    (rs.getTimestamp("created_at").toLocalDateTime()), 
                    (rs.getTimestamp("updated_at").toLocalDateTime()),
                    rs.getString(" ")
                );
            }
            return null;
        } catch (Exception e) {
            
        }
        return null;
    }

    private Laporan fetchLaporan(int ID_Order){
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM laporan WHERE ID_Order = ?";
            PreparedStatement pstmt =  conn.prepareStatement(query);
            pstmt.setInt(1, ID_Order);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Laporan(
                    rs.getInt("ID_Laporan"), 
                    rs.getString("isiKeluhan"), 
                    StatusLaporan.valueOf(rs.getString("statusLaporan")), 
                    new Date(rs.getTimestamp("createdAt").getTime()), 
                    new Date(rs.getTimestamp("finishAt").getTime())
                );
                
            }
            return null;
        } catch (Exception e) {
            
        }
        return null;
    }

    private Lokasi fetchRegionLocation(int ID_Wilayah){
        try (Connection conn = DatabaseHandler.connect()) {
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
        } catch (Exception e) {
            
        }
        return null;
    }

    public List<Order> loadOrders(){
        List<Order> orders = new ArrayList<>();
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        System.out.println(loggedInUser.getID_Driver());
        
        if (loggedInUser != null) {
            try (Connection conn = DatabaseHandler.connect()) {
                String query = "SELECT * FROM `order` WHERE order_status = 'On_Process'";
                String serviceType = cekServiceType(loggedInUser.getID_Driver());
                System.out.println(serviceType);
                if (serviceType.equals("GrabBike")) {
                    query += " AND serviceType = 'GrabBike'";
                } else if (serviceType.equals("GrabCar")) {
                    query += " AND serviceType = 'GrabCar'";
                }
            
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
            
                while (rs.next()) {
                    Voucher voucher = fetchVoucher(rs.getInt("ID_Voucher"));

                    Laporan laporan = fetchLaporan(rs.getInt("ID_Order"));

                    Lokasi lokasiPickUp = fetchRegionLocation(rs.getInt("ID_WilayahPickUp"));
                    Lokasi lokasiDestionation = fetchRegionLocation(rs.getInt("ID_WilayahDestination"));
            
                    ServiceType serviceTypeEnum = null;
                    String serviceTypeStr = rs.getString("serviceType");
                    if (serviceTypeStr != null) {
                        try {
                            serviceTypeEnum = ServiceType.valueOf(serviceTypeStr.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid serviceType: " + serviceTypeStr + " for ID_Order: " + rs.getInt("ID_Order"));
                        }
                    } else {
                        System.err.println("serviceType is null for ID_Order: " + rs.getInt("ID_Order"));
                    }
            
                    OrderStatus orderStatusEnum = null;
                    String orderStatusStr = rs.getString("order_status");
                    if (orderStatusStr != null) {
                        try {
                            orderStatusEnum = OrderStatus.valueOf(orderStatusStr.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid orderStatus: " + orderStatusStr + " for ID_Order: " + rs.getInt("ID_Order"));
                        }
                    } else {
                        orderStatusEnum = null; 
                    }
            
                    PaymentMethod paymentMethodEnum = null;
                    String paymentMethodStr = rs.getString("paymentMethod");
                    if (paymentMethodStr != null) {
                        try {
                            paymentMethodEnum = PaymentMethod.valueOf(paymentMethodStr.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid paymentMethod: " + paymentMethodStr + " for ID_Order: " + rs.getInt("ID_Order"));
                        }
                    } else {
                        System.err.println("paymentMethod is null for ID_Order: " + rs.getInt("ID_Order"));
                    }
            
                    LocalDateTime updateOrder = rs.getObject("updatedOrder", LocalDateTime.class);
                    Date updateOrderConvert = null;
                    if (updateOrder != null) {
                        updateOrderConvert = Date.from(updateOrder.atZone(ZoneId.systemDefault()).toInstant());
                    }

                    if (lokasiPickUp != null && lokasiDestionation != null) {
                        Order order = new Order(
                            rs.getInt("ID_Order"),
                            rs.getInt("ID_Driver"),
                            rs.getInt("ID_Customer"),
                            voucher,
                            laporan,
                            lokasiPickUp,
                            lokasiDestionation,
                            serviceTypeEnum,
                            orderStatusEnum,
                            new Date(rs.getTimestamp("order_date").getTime()),
                            updateOrderConvert,
                            paymentMethodEnum,
                            rs.getDouble("price"),
                            rs.getDouble("rating"),
                            rs.getString("ulasan")
                        );
                        orders.add(order);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(driverOrderPage, "Error loading orders", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return orders; 
    }
}
