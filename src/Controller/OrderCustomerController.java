package Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import Model.RupiahFormatter;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Customer;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Enum.TypeBikeOrder;
import Model.Enum.TypeCarOrder;

public class OrderCustomerController {
    public double calculateDistance(Lokasi pickUpLoc, Lokasi destinationLoc) {
        double initRadiansLatitude = Math.toRadians(pickUpLoc.getGarisLintang());
        double finalRadiansLatitude = Math.toRadians(destinationLoc.getGarisLintang());
        double deltaRadiansLatitude = Math.abs(finalRadiansLatitude - initRadiansLatitude);

        double initialRadiansLongitude = Math.toRadians(pickUpLoc.getGarisBujur());
        double finalRadiansLongitude = Math.toRadians(destinationLoc.getGarisBujur());
        double deltaRadiansLongitude = Math.abs(finalRadiansLongitude - initialRadiansLongitude);

        double earthRadius = 6371.0; // in kilometers
        double haversine = Math.pow(Math.sin(deltaRadiansLatitude / 2), 2) +
                Math.cos(initRadiansLatitude) * Math.cos(finalRadiansLatitude) *
                        Math.pow(Math.sin(deltaRadiansLongitude / 2), 2);
        double centralAngle = Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        return earthRadius * centralAngle;
    }

    public int getPrice(String orderType, ServiceType serviceType, Lokasi pickUp, Lokasi destinasi, double disc) {
        double jarak = calculateDistance(pickUp, destinasi);
        if (serviceType == ServiceType.GRABBIKE) {
            if (orderType.equalsIgnoreCase("Hemat")) {
                return (int) (TypeBikeOrder.HEMAT.getPriceValue() * jarak * 1000 - disc);
            } else if (orderType.equalsIgnoreCase("Reguler")) {
                return (int) (TypeBikeOrder.REGULER.getPriceValue() * jarak * 1000 - disc);
            } else if (orderType.equalsIgnoreCase("XL")) {
                return (int) (TypeBikeOrder.XL.getPriceValue() * jarak * 1000 - disc);
            } else {
                return (int) (TypeBikeOrder.ELECTRIC.getPriceValue() * jarak * 1000 - disc);
            }
        } else if (serviceType == ServiceType.GRABCAR) {
            if (orderType.equalsIgnoreCase("Hemat")) {
                return (int) (TypeCarOrder.HEMAT.getPriceValue() * jarak * 1000 - disc);
            } else if (orderType.equalsIgnoreCase("Reguler")) {
                return (int) (TypeCarOrder.REGULER.getPriceValue() * jarak * 1000 - disc);
            } else if (orderType.equalsIgnoreCase("XL")) {
                return (int) (TypeCarOrder.XL.getPriceValue() * jarak * 1000 - disc);
            } else {
                return (int) (TypeCarOrder.FAST_TRACK.getPriceValue() * jarak * 1000 - disc);
            }
        }
        return 0;
    }

    // public int getEstimatedTime(Lokasi pickUp, Lokasi destinasi){
    // double jarak = calculateDistance(pickUp, destinasi);
    // if (condition) {

    // }

    // }
    // Nanti diselesaikan

    public String[] getAllLocation() {
        ArrayList<Lokasi> locations = new ArrayList<>();
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM wilayah";

            var preparedStatement = conn.prepareStatement(query);

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                locations.add(new Lokasi(rs.getInt("ID_Wilayah"), rs.getString("kelurahan"), rs.getString("kecamatan"),
                        rs.getString("kota"), rs.getDouble("garisLintang"), rs.getDouble("garisBujur"),
                        rs.getString("alamat")));
            }

            String results[] = new String[locations.size() + 2];
            results[0] = "";
            for (int i = 1; i < locations.size()+1; i++) {
                results[i] = locations.get(i-1).getAlamat() + ", " + locations.get(i-1).getKota();
            }

            results[results.length - 1] = "Add new Location";

            return results;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public Lokasi getLocation(String lok) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM wilayah WHERE alamat = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, lok);

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return new Lokasi(rs.getInt("ID_Wilayah"), rs.getString("kelurahan"), rs.getString("kecamatan"),
                        rs.getString("kota"), rs.getDouble("garisLintang"), rs.getDouble("garisBujur"),
                        rs.getString("alamat"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }


    public String[] getAllVoucher(int idCustomer, ServiceType serviceType, int price) {
        ArrayList<Voucher> vouchers = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM voucher WHERE ID_Customer = ? AND serviceType = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, idCustomer);
            preparedStatement.setString(2, serviceType.toString());

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                double potongan = rs.getDouble("jumlahPotongan");

                if (price != 0 && potongan > price) {
                    potongan = price * 0.85;
                }

                ServiceType serviceTypeVoucher = rs.getString("serviceType").equalsIgnoreCase("GrabCar")
                            ? ServiceType.GRABCAR
                            : ServiceType.GRABBIKE;
                vouchers.add(new Voucher(rs.getInt("ID_Voucher"),
                            rs.getString("kodeVoucher"),potongan,
                            serviceTypeVoucher,
                            rs.getDate("valid_from"), rs.getDate("valid_to"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("update_at").toLocalDateTime(), ""));
            }

            if (vouchers.size() == 0) {
                String results[] = new String[2];
    
                results[0] = "Sorry you don't have any voucher";
                results[1] = "I have voucher code";
                return results;
            }else{
                String results[] = new String[vouchers.size() + 3];
                results[0] = "";
                for (int i = 1; i < vouchers.size()+1; i++) {
                    results[i] = vouchers.get(i-1).getKodeVoucher() + ": Potongan " + (int)(vouchers.get(i-1).getJumlahPotongan());
                }
    
                results[results.length - 2] = "I have another voucher code";
                results[results.length - 1] = "I don't want to use my voucher";
    
                return results;
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }
    
    
    public boolean validateVoucherCode(String kode, int idCustomer) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT ID_Voucher FROM voucher WHERE kodeVoucher = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, kode);

            var rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String sqlUpdateVoucher = "UPDATE voucher SET ID_Customer = ? WHERE kodeVoucher = ?";
                var preparedStatementUpdate = conn.prepareStatement(sqlUpdateVoucher);
                preparedStatementUpdate.setInt(1, idCustomer);
                preparedStatementUpdate.setString(2, kode);

                int affectedRowsUpdate = preparedStatementUpdate.executeUpdate();

                if (affectedRowsUpdate > 0) {
                    JOptionPane.showMessageDialog(null, "Selamat Voucher berhasil ditambahkan!", "Add Voucher From Customer", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return false;
    }


    private Voucher getVoucher(String voucherCode){
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM voucher WHERE kodeVoucher = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, voucherCode);

            var rs = preparedStatement.executeQuery();

            if (rs.next()) {
                ServiceType serviceTypeVoucher = rs.getString("serviceType").equalsIgnoreCase("GrabCar")
                            ? ServiceType.GRABCAR
                            : ServiceType.GRABBIKE;
                Voucher voucher = new Voucher(rs.getInt("ID_Voucher"),
                            rs.getString("kodeVoucher"), rs.getDouble("jumlahPotongan"),
                            serviceTypeVoucher,
                            rs.getDate("valid_from"), rs.getDate("valid_to"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("update_at").toLocalDateTime(), "");

                return voucher;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }



    public boolean insertNewOrder(String kodeVoucher, Lokasi pickUp, Lokasi destinasi, ServiceType serviceType, String paymentMethod, double price, String orderType){
        Customer customer = (Customer)SingletonManger.getInstance().getLoggedInUser();
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "INSERT INTO `order`(ID_Customer, ID_Voucher, ID_WilayahPickUp, ID_WilayahDestination, serviceType, order_status, order_date, paymentMethod, price, orderType) VALUES (?,?,?,?,?,?, CURRENT_DATE() ,?,?,?)";

            Voucher voucher = getVoucher(kodeVoucher);

            var preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, customer.getID_Customer());
            preparedStatement.setInt(2, voucher.getID_Voucher());
            preparedStatement.setInt(3, pickUp.getID_wilayah());
            preparedStatement.setInt(4, destinasi.getID_wilayah());
            preparedStatement.setString(5, serviceType.toString());
            preparedStatement.setString(6,"On_Process");
            preparedStatement.setString(7,paymentMethod);
            preparedStatement.setDouble(8,price);
            preparedStatement.setString(9,orderType);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idOrder = generatedKeys.getInt(1); 

                        // tambahkan order yang baru ke customer!!!!
                        // Lanjutkan nant
                        PaymentMethod pm = paymentMethod.equalsIgnoreCase("Cash") ? PaymentMethod.CASH : PaymentMethod.OVO;

                        customer.getOrder().add(new Order(idOrder, 0, customer.getID_Customer(), voucher, null, pickUp, destinasi, serviceType, OrderStatus.ON_PROCESS, new Date(), null, pm, price, 0, ""));

                        JOptionPane.showMessageDialog(null, "Tunggu sebentar ya, kami sedang mencarikan driver untukmu :)", "Search For Driver Nearby", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return false;
    }
}
