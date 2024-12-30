package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Enum.UserType;
import Model.Class.Location.Lokasi;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Laporan;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Class.Payment.Ovo;
import Model.Class.Vehicle.Car;
import Model.Class.Vehicle.Motorcycle;
import Model.Class.Vehicle.Vehicle;
import Model.Enum.DriverStatus;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Enum.StatusAcc;
import Model.Enum.StatusLaporan;
import Model.Enum.StatusVerification;
import Model.Enum.TypeBikeOrder;
import Model.Enum.TypeCarOrder;

public class ManageCustDriverController {
    public ArrayList<Customer> getDataCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect()) {
            String sqlCustomer = "SELECT * FROM users u LEFT JOIN notlp n ON u.ID_User = n.ID_User WHERE userType = ?";

            var preparedStatement = conn.prepareStatement(sqlCustomer);
            preparedStatement.setString(1, "Customer");

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("ID_User");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phoneNumber");
                Date updateProfileAt = rs.getDate("updateProfileAt");
                String statusAcc = rs.getString("statusAcc");
                Date createdAccAt = rs.getDate("createdAccAt");

                customers.add(new Customer(
                        userId, username, name, password, phone, email, updateProfileAt, UserType.CUSTOMER,
                        rs.getString("profilePhoto"),
                        getStatusAcc(statusAcc), createdAccAt, getOvo(rs.getInt("ID_Tlp")),
                        getOrderUser(userId, true)));
            }

            return customers;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public ArrayList<Driver> getDataDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect()) {
            String sqlCustomer = "SELECT * FROM users u LEFT JOIN notlp n ON u.ID_User = n.ID_User WHERE userType = ?";

            var preparedStatement = conn.prepareStatement(sqlCustomer);
            preparedStatement.setString(1, "Driver");

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("ID_User");
                int vehicleId = rs.getInt("vehicle_ID");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String phone = rs.getString("phoneNumber");
                Date updateProfileAt = rs.getDate("updateProfileAt");
                String statusAcc = rs.getString("statusAcc");
                Date createdAccAt = rs.getDate("createdAccAt");

                ArrayList<Order> ordersDriver = getOrderUser(userId, false);
                double ratingDriver = getRatingDriver(ordersDriver);

                drivers.add(new Driver(
                        userId, username, name, password, phone, email, updateProfileAt, UserType.DRIVER,
                        rs.getString("profilePhoto"), getStatusAcc(statusAcc), DriverStatus.ONLINE, createdAccAt,
                        getVehicle(vehicleId), ordersDriver, ratingDriver,
                        getOvo(rs.getInt("ID_Tlp")), getVerificationStatus(rs.getString("statusVerify"))));
            }

            return drivers;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public boolean updateStatusAccUser(int id_user, StatusAcc statusAcc) {
        try (Connection conn = DatabaseHandler.connect()) {
            String sqlUpdateStatusAcc = "UPDATE users SET statusAcc = ? WHERE ID_User = ?";

            var preparedStatement = conn.prepareStatement(sqlUpdateStatusAcc);

            String status = "";
            if (statusAcc == StatusAcc.BLOCKED) {
                preparedStatement.setString(1, "Unblock");
                preparedStatement.setInt(2, id_user);
                status = "Block";
            } else {
                preparedStatement.setString(1, "Block");
                preparedStatement.setInt(2, id_user);
                status = "Unblock";
            }

            int answer = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin akan " + status + " akun tersebut?",
                    "Konfirmasi Block / Unblock Akun", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected > 0) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return false;
    }

    public boolean updateStatusVerifyUser(int id_user, StatusVerification statusAcc) {
        try (Connection conn = DatabaseHandler.connect()) {
            String sqlUpdateStatusAcc = "UPDATE users SET statusVerify = ? WHERE ID_User = ?";

            var preparedStatement = conn.prepareStatement(sqlUpdateStatusAcc);
            preparedStatement.setString(1, "Verified");
            preparedStatement.setInt(2, id_user);

            int answer = JOptionPane.showConfirmDialog(null, "Apakah semua data sudah diverifikasi?",
                    "Konfirmasi Verifikasi Akun Driver", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected > 0) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return false;
    }

    private Vehicle getVehicle(int id_vehicle) {
        try (Connection conn = DatabaseHandler.connect()) {
            String queryVehicle = "SELECT * FROM vehicle WHERE vehicle_ID = ?";

            var preparedStmtVehicle = conn.prepareStatement(queryVehicle);
            preparedStmtVehicle.setInt(1, id_vehicle);

            var resultVehicle = preparedStmtVehicle.executeQuery();

            while (resultVehicle.next()) {
                if (resultVehicle.getString("vehicleType").equalsIgnoreCase("Car")) {
                    return new Car(id_vehicle, resultVehicle.getString("vehicleName"),
                            resultVehicle.getString("plateNumber"), resultVehicle.getInt("jumlahSeat"));
                } else {
                    return new Motorcycle(id_vehicle, resultVehicle.getString("vehicleName"),
                            resultVehicle.getString("plateNumber"), resultVehicle.getInt("jumlahSeat"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return null;
    }

    private double getRatingDriver(ArrayList<Order> orders) {
        double rating = 0;
        for (Order order : orders) {
            rating += order.getRatingOrder();
        }
        return rating / orders.size();
    }

    private StatusAcc getStatusAcc(String accStatus) {
        return accStatus.equalsIgnoreCase("Blocked") ? StatusAcc.BLOCKED : StatusAcc.UNBLOCKED;
    }

    private ArrayList<Order> getOrderUser(int id_user, boolean isCustomer) {
        String orderQuery = "SELECT * FROM `order` o LEFT JOIN voucher v ON v.ID_Voucher = o.ID_Voucher LEFT JOIN laporan l ON l.ID_Order = o.ID_Order WHERE o.ID_Driver = ?";
        if (isCustomer) {
            orderQuery = "SELECT * FROM `order` o LEFT JOIN voucher v ON v.ID_Voucher = o.ID_Voucher LEFT JOIN laporan l ON l.ID_Order = o.ID_Order WHERE o.ID_Customer = ?";
        }

        try (Connection conn = DatabaseHandler.connect()) {
            var preparedStmtOrder = conn.prepareStatement(orderQuery);
            preparedStmtOrder.setInt(1, id_user);

            var resultOrders = preparedStmtOrder.executeQuery();

            ArrayList<Order> orders = new ArrayList<>();
            while (resultOrders.next()) {
                // Ambil Voucher yang dipake untuk ordernya (jika ada)
                ServiceType serviceTypeVoucher = resultOrders.getString("serviceType").equalsIgnoreCase("GrabCar")
                        ? ServiceType.GRABCAR
                        : ServiceType.GRABBIKE;

                Voucher voucher = new Voucher(resultOrders.getInt("ID_Voucher"),
                        resultOrders.getString("kodeVoucher"), resultOrders.getDouble("jumlahPotongan"),
                        serviceTypeVoucher,
                        resultOrders.getDate("valid_from"), resultOrders.getDate("valid_to"),
                        resultOrders.getTimestamp("created_at").toLocalDateTime(),
                        resultOrders.getTimestamp("update_at").toLocalDateTime(), resultOrders.getString("name"));

                // Ambil data Laporan (jika ada)
                StatusLaporan statusLaporan = getStatusLaporan(resultOrders.getString("statusLaporan"));

                Laporan laporan = new Laporan(resultOrders.getInt("ID_Laporan"), resultOrders.getString("isiKeluhan"),
                        statusLaporan, resultOrders.getDate("createdAt"), resultOrders.getDate("finishAt"));

                ServiceType serviceType = getServiceType(resultOrders.getString("serviceType"));

                if (serviceType == ServiceType.GRABBIKE) {
                    orders.add(new GrabBike(resultOrders.getInt("ID_Order"), resultOrders.getInt("ID_Driver"),
                            resultOrders.getInt("ID_Customer"), voucher, laporan,
                            getLocation(resultOrders.getInt("ID_WilayahPickUp")),
                            getLocation(resultOrders.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(resultOrders.getString("order_status")), resultOrders.getDate("order_date"),
                            resultOrders.getDate("updatedOrder"),
                            getPaymentMethod(resultOrders.getString("paymentMethod")), resultOrders.getDouble("price"),
                            getTypeBikeOrder(resultOrders.getString("orderType")), resultOrders.getDouble("rating"),
                            resultOrders.getString("ulasan")));
                } else {
                    orders.add(new GrabCar(resultOrders.getInt("ID_Order"), resultOrders.getInt("ID_Driver"),
                            resultOrders.getInt("ID_Customer"), voucher, laporan,
                            getLocation(resultOrders.getInt("ID_WilayahPickUp")),
                            getLocation(resultOrders.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(resultOrders.getString("order_status")), resultOrders.getDate("order_date"),
                            resultOrders.getDate("updatedOrder"),
                            getPaymentMethod(resultOrders.getString("paymentMethod")), resultOrders.getDouble("price"),
                            getTypeCarOrder(resultOrders.getString("orderType")), resultOrders.getDouble("rating"),
                            resultOrders.getString("ulasan")));
                }
            }
            return orders;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return null;
    }

    private StatusLaporan getStatusLaporan(String status) {
        if (status != null) {
            if (status.equalsIgnoreCase("Waiting")) {
                return StatusLaporan.WAITING;
            } else if (status.equalsIgnoreCase("On_Process")) {
                return StatusLaporan.ON_PROCESS;
            } else {
                return StatusLaporan.DONE;
            }
        }
        return null;
    }

    private StatusVerification getVerificationStatus(String status) {
        if (status != null) {
            if (status.equalsIgnoreCase("Verified")) {
                return StatusVerification.VERIFIED;
            } else {
                return StatusVerification.UNVERIFIED;
            }
        }
        return null;
    }

    private PaymentMethod getPaymentMethod(String payment) {
        return payment.equalsIgnoreCase("Cash") ? PaymentMethod.CASH : PaymentMethod.OVO;
    }

    private OrderStatus getOrderStatus(String order) {
        if (order.equalsIgnoreCase("Complete")) {
            return OrderStatus.COMPLETE;
        } else if (order.equalsIgnoreCase("On_Progress")) {
            return OrderStatus.ON_PROCESS;
        } else {
            return OrderStatus.CANCELLED;
        }
    }

    private ServiceType getServiceType(String service) {
        return service.equalsIgnoreCase("GrabCar") ? ServiceType.GRABCAR : ServiceType.GRABBIKE;
    }

    private Lokasi getLocation(int id_lokasi) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM wilayah WHERE ID_Wilayah = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id_lokasi);

            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return new Lokasi(id_lokasi, rs.getString("kelurahan"), rs.getString("kecamatan"), rs.getString("kota"),
                        rs.getDouble("garisLintang"), rs.getDouble("garisBujur"), rs.getString("alamat"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    private TypeBikeOrder getTypeBikeOrder(String type) {
        if (type.equalsIgnoreCase("Hemat")) {
            return TypeBikeOrder.HEMAT;
        } else if (type.equalsIgnoreCase("Reguler")) {
            return TypeBikeOrder.REGULER;
        } else if (type.equalsIgnoreCase("XL")) {
            return TypeBikeOrder.XL;
        } else if (type.equalsIgnoreCase("Electric")) {
            return TypeBikeOrder.ELECTRIC;
        } else {
            return null;
        }
    }

    private TypeCarOrder getTypeCarOrder(String type) {
        if (type.equalsIgnoreCase("Hemat")) {
            return TypeCarOrder.HEMAT;
        } else if (type.equalsIgnoreCase("Reguler")) {
            return TypeCarOrder.REGULER;
        } else if (type.equalsIgnoreCase("XL")) {
            return TypeCarOrder.XL;
        } else if (type.equalsIgnoreCase("Fast_Track")) {
            return TypeCarOrder.FAST_TRACK;
        } else {
            return null;
        }
    }

    private Ovo getOvo(int id_tlp) {
        try (Connection conn = DatabaseHandler.connect()) {
            String queryOvo = "SELECT * FROM ovo WHERE ID_Tlp = ?";
            var preparedStmtOvo = conn.prepareStatement(queryOvo);
            preparedStmtOvo.setInt(1, id_tlp);
            var resultOvo = preparedStmtOvo.executeQuery();

            if (resultOvo.next()) {
                return new Ovo(resultOvo.getInt("walletID"), resultOvo.getDouble("saldo"),
                        resultOvo.getDouble("coins"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

        return null;
    }
}
