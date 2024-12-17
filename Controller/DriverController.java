package Controller;
import Model.Class.Order.Order;
import Model.Class.User.Admin;
import Model.Class.User.Driver;
import Model.Class.User.User;
import Model.Enum.DriverStatus;

import java.security.Identity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Controller.DatabaseManager;

public class DriverController {
    private Driver driver;

    public DriverController(Driver driver) {
        this.driver = driver;
    }

    static DatabaseHandler conn = new DatabaseHandler();

    public String searchDriver(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String sql = "SELECT ID_Driver FROM Users WHERE ID_User = '" + ID_Driver + "'";
            Statement stmt = conn.createStatement();

            if (stmt.execute(sql)) {
                return ID_Driver;
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean updateAvailability(String ID_Driver, String availabilityStatus){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String sql = "UPDATE users SET availability = '" + availabilityStatus + "' where ID_User = '" + id + "'";
    
                Statement stmt = conn.createStatement();
    
                if (stmt.execute(sql)) {
                    return true;
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String lihatRating(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String sql = "SELECT ROUND (AVG(r.rating),1) as rating FROM review r INNER JOIN `order` o ON r.ID_Order = o.ID_Order INNER JOIN users u ON o.ID_Driver = u.ID_User WHERE o.ID_Driver = '" + id + "'";
    
                Statement stmt = conn.createStatement();
    
                if (stmt.execute(sql)) {
                    return "Rating Anda: " + sql;
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String cekServiceType(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String sql = "SELECT v.vehicleType FROM vehicle v INNER JOIN users u ON v.vehicle_ID = u.vehicle_ID WHERE u.ID_User = '" + id + "'";
    
                Statement stmt = conn.createStatement();
    
                if (stmt.execute(sql)) {
                    if (sql.equalsIgnoreCase("Motorcyle")) {
                        return "GrabBike";
                    }else if(sql.equalsIgnoreCase("Car")){
                        return "GrabCar";
                    }
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void showListOrder(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String serviceType = cekServiceType(id);
                
                String sql = "SELECT o.ID_Order, u.name, v.kodeVoucher, wi.alamat AS alamat_jemput, w.alamat AS alamat_tujuan, o.serviceType, o.orderType, o.paymentMethod, o.price FROM `order` o INNER JOIN users u ON o.ID_Customer = u.ID_User LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher INNER JOIN wilayah wi ON o.ID_WilayahPickUp = wi.ID_Wilayah INNER JOIN wilayah w ON o.ID_WilayahDestination = w.ID_Wilayah  WHERE o.serviceType = '" + serviceType + "' AND o.ID_Driver IS NULL";
    
                Statement stmt = conn.createStatement();
    
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String[][] data = {
                        {"ID_Order: ", rs.getString("ID_Order")}
                    };
                }
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String selesaiOrder(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String sql = "UPDATE order SET order_status = 'Complete' WHERE u.ID_User = '" + id + "' AND order_status = 'On_Progress'";
    
                Statement stmt = conn.createStatement();
    
                if (stmt.execute(sql)) {
                    if (sql.equalsIgnoreCase("Motorcyle")) {
                        return "GrabBike";
                    }else if(sql.equalsIgnoreCase("Car")){
                        return "GrabCar";
                    }
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String pendapatanPerBulan(String ID_Driver){
        Connection conn = DatabaseManager.connect();
        try {
            String id = searchDriver(ID_Driver);
            if (id != "") {
                String sql = "SELECT SUM(price) AS Pendapatan Per Bulan FROM  orders WHERE  order_status = 'Complete'  AND ID_Driver = '" + id + "'" + "GROUP BY DATE_FORMAT(order_date, '%Y-%m')";
    
                Statement stmt = conn.createStatement();
    
                if (stmt.execute(sql)) {
                    return "Pendapatan Anda bulan ini adalah: " + sql;
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Maaf ID_Driver tidak terdaftar", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    
}
