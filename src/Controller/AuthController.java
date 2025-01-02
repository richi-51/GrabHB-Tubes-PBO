package Controller;

import Model.Class.User.Admin;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
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
import Model.Enum.UserType;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Laporan;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Class.Payment.Ovo;
import Model.Class.Singleton.*;
import View.LoadingForRegist;
import View.LoginForm;
import View.ManageCustomer;
import View.ManageDriver;
import View.ManageLaporan;
import View.ManageVoucher;
import View.RegisterForm;
import View.TemplateMenu;
import View.TotalPendapatan;
import View.UpdateProfile;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthController {
    private LoginForm loginView;
    private RegisterForm registerView;

    public AuthController(){

    }
    public AuthController(LoginForm loginView, RegisterForm registerView) {
        this.loginView = loginView;
        this.registerView = registerView;
        
        this.loginView.addLoginListener(new LoginAction());
        this.loginView.addRegisterListener(e -> registerView.setVisible(true));
        this.registerView.addRegisterListener(new RegisterAction());
        this.registerView.addRegisterListener(e-> loginView.dispose());
    }
    
    // Getter and Setter
    public LoginForm getLoginView() {
        return loginView;
    }

    public void setLoginView(LoginForm loginView) {
        this.loginView = loginView;
    }

    public RegisterForm getRegisterView() {
        return registerView;
    }

    public void setRegisterView(RegisterForm registerView) {
        this.registerView = registerView;
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String usernameEmail = loginView.getUsernameEmail();
            String password = loginView.getPassword();

            try (Connection conn = DatabaseHandler.connect()) {
                String queryUser = "";
                if (usernameEmail.contains("@")) {
                    queryUser = "SELECT * FROM Users u LEFT JOIN notlp n ON u.ID_User = n.ID_User WHERE email = ? AND password = ?";
                } else {
                    queryUser = "SELECT * FROM Users u LEFT JOIN notlp n ON u.ID_User = n.ID_User WHERE username = ? AND password = ?";
                }

                // Periksa apakah user ada di tabel Users
                var preparedStatement = conn.prepareStatement(queryUser);
                preparedStatement.setString(1, usernameEmail);
                preparedStatement.setString(2, password);

                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int userId = resultSet.getInt("ID_User");
                    int vehicleId = resultSet.getInt("vehicle_ID");
                    String name = resultSet.getString("name");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phoneNumber");
                    Date updateProfileAt = resultSet.getDate("updateProfileAt");
                    String userType = resultSet.getString("userType");
                    String statusAcc = resultSet.getString("statusAcc");
                    Date createdAccAt = resultSet.getDate("createdAccAt");

                    // Simpan data login di Singleton sesuai role
                    if ("Admin".equalsIgnoreCase(userType)) {
                        SingletonManger.getInstance().setLoggedInUser(new Admin(
                                userId, username, name, password, phone, email, updateProfileAt, UserType.ADMIN,
                                resultSet.getString("profilePhoto")));

                        TemplateMenu tmp = new TemplateMenu();
                        Component panels[] = {new UpdateProfile(tmp), new ManageCustomer(tmp), new ManageDriver(tmp), new ManageVoucher(tmp), new ManageLaporan(tmp), new TotalPendapatan(tmp, false)};

                        new TemplateMenu("Admin HomePage", new String[]{"Update Profile", "Manage Customers", "Manage Drivers", "Manage Vouchers", "Manage Reports", "View Revenue"}, panels, "Welcome to Admin Panel");


                    } else if ("Customer".equalsIgnoreCase(userType)) {
                        SingletonManger.getInstance().setLoggedInUser(new Customer(
                                userId, username, name, password, phone, email, updateProfileAt, UserType.CUSTOMER,
                                resultSet.getString("profilePhoto"),
                                getStatusAcc(statusAcc), createdAccAt, getOvo(resultSet.getInt("ID_Tlp")),
                                getOrderUser(userId, true)));
                    } else if ("Driver".equalsIgnoreCase(userType)) {
                        ArrayList<Order> ordersDriver = getOrderUser(userId, false);
                        double ratingDriver = getRatingDriver(ordersDriver);

                        SingletonManger.getInstance().setLoggedInUser(new Driver(
                                userId, username, name, password, phone, email, updateProfileAt, UserType.DRIVER,
                                resultSet.getString("profilePhoto"), getStatusAcc(statusAcc), DriverStatus.ONLINE,
                                createdAccAt, getVehicle(vehicleId), ordersDriver,
                                ratingDriver, getOvo(resultSet.getInt("ID_Tlp")),
                                getVerificationStatus(resultSet.getString("statusVerify"))));

                        // Update status driver karena baru logIn harus online
                        String queryUpdate = "UPDATE users SET availabilityDriver = ? WHERE ID_user = ?";
                        var prepareStmtupdate = conn.prepareStatement(queryUpdate);
                        prepareStmtupdate.setString(1, "Online");
                        prepareStmtupdate.setInt(2, userId);
                        prepareStmtupdate.executeUpdate();
                    }

                    // Close LogIn window
                    loginView.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginView, "Invalid username or password!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
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
                JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
            }

            return null;
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
                JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
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
            String orderQuery = "SELECT * FROM `order` o LEFT JOIN voucher v ON v.ID_Voucher = o.ID_Voucher LEFT JOIN laporan l ON l.ID_Order = o.ID_Order INNER JOIN users u ON u.ID_User = v.ID_Admin WHERE o.ID_Driver = ?";
            if (isCustomer) {
                orderQuery = "SELECT * FROM `order` o LEFT JOIN voucher v ON v.ID_Voucher = o.ID_Voucher LEFT JOIN laporan l ON l.ID_Order = o.ID_Order INNER JOIN users u ON u.ID_User = v.ID_Admin WHERE o.ID_Customer = ?";
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

                    Laporan laporan = new Laporan(resultOrders.getInt("ID_Laporan"),
                            resultOrders.getString("isiKeluhan"), statusLaporan, resultOrders.getDate("createdAt"),
                            resultOrders.getDate("finishAt"));

                    ServiceType serviceType = getServiceType(resultOrders.getString("serviceType"));

                    if (serviceType == ServiceType.GRABBIKE) {
                        orders.add(new GrabBike(resultOrders.getInt("ID_Order"), resultOrders.getInt("ID_Driver"),
                                resultOrders.getInt("ID_Customer"), voucher, laporan,
                                getLocation(resultOrders.getInt("ID_WilayahPickUp")),
                                getLocation(resultOrders.getInt("ID_WilayahDestination")), serviceType,
                                getOrderStatus(resultOrders.getString("order_status")),
                                resultOrders.getDate("order_date"), resultOrders.getDate("updatedOrder"),
                                getPaymentMethod(resultOrders.getString("paymentMethod")),
                                resultOrders.getDouble("price"), getTypeBikeOrder(resultOrders.getString("orderType")),
                                resultOrders.getDouble("rating"), resultOrders.getString("ulasan")));
                    } else {
                        orders.add(new GrabCar(resultOrders.getInt("ID_Order"), resultOrders.getInt("ID_Driver"),
                                resultOrders.getInt("ID_Customer"), voucher, laporan,
                                getLocation(resultOrders.getInt("ID_WilayahPickUp")),
                                getLocation(resultOrders.getInt("ID_WilayahDestination")), serviceType,
                                getOrderStatus(resultOrders.getString("order_status")),
                                resultOrders.getDate("order_date"), resultOrders.getDate("updatedOrder"),
                                getPaymentMethod(resultOrders.getString("paymentMethod")),
                                resultOrders.getDouble("price"), getTypeCarOrder(resultOrders.getString("orderType")),
                                resultOrders.getDouble("rating"), resultOrders.getString("ulasan")));
                    }
                }
                return orders;
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
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
                    return new Lokasi(id_lokasi, rs.getString("kelurahan"), rs.getString("kecamatan"),
                            rs.getString("kota"), rs.getDouble("garisLintang"), rs.getDouble("garisBujur"),
                            rs.getString("alamat"));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginView, "Database error: " + ex.getMessage());
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
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = registerView.getName();
            String username = registerView.getUsername();
            String email = registerView.getEmail();
            String phone = registerView.getPhoneNumber();
            String password = registerView.getPassword();
            String userType = "Customer";
            String statusVerify = "None";
            String statusAcc = "Unblock";
            int rowsInserted = 0;

            try (Connection conn = DatabaseHandler.connect()) {
                // Insert data vehicle
                if (registerView.getIsDriverRegisterSelected()) {
                    String platNo = registerView.getPlatNo();
                    String vehicleName = registerView.getNamaKend();
                    String vehicleType = registerView.getVehicleType();
                    userType = "Driver";
                    statusVerify = "Unverified";
                    int jumlahSeat = Integer.parseInt(registerView.getKapasitasKend());

                    String queryVehicle = "INSERT INTO vehicle (plateNumber, vehicleName, vehicleType, jumlahSeat) VALUES (?, ?, ?, ?)";
                    var preparedStmtVehicle = conn.prepareStatement(queryVehicle);

                    preparedStmtVehicle.setString(1, platNo);
                    preparedStmtVehicle.setString(2, vehicleName);
                    preparedStmtVehicle.setString(3, vehicleType);
                    preparedStmtVehicle.setInt(4, jumlahSeat);

                    int vehicleInserted = preparedStmtVehicle.executeUpdate();
                    if (vehicleInserted == 0) {
                        JOptionPane.showMessageDialog(registerView, "Failed to register your vehicle :(",
                                "Failed to Register Vehicle", JOptionPane.ERROR_MESSAGE);
                    }

                    // Get Id vehicle
                    String getIdVehicle = "SELECT vehicle_ID FROM vehicle WHERE vehicleName = ?";
                    var preparedStmtGetIdVehicle = conn.prepareStatement(getIdVehicle);

                    preparedStmtGetIdVehicle.setString(1, registerView.getNamaKend());

                    var resultSetIdVehicle = preparedStmtGetIdVehicle.executeQuery();
                    int idVehicle = 0;
                    while (resultSetIdVehicle.next()) {
                        idVehicle = resultSetIdVehicle.getInt("vehicle_ID");
                    }

                    // Insert Data User (Driver)
                    String query = "INSERT INTO Users (vehicle_ID, name, username, password, email, userType, statusAcc, statusVerify, createdAccAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
                    var preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, idVehicle);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, username);
                    preparedStatement.setString(4, password);
                    preparedStatement.setString(5, email);
                    preparedStatement.setString(6, userType);
                    preparedStatement.setString(7, statusAcc);
                    preparedStatement.setString(8, statusVerify);

                    rowsInserted = preparedStatement.executeUpdate();

                    // Get Id User
                    String getIdSQL = "SELECT ID_User FROM Users WHERE username = ?";
                    var preparedStmtGetId = conn.prepareStatement(getIdSQL);

                    preparedStmtGetId.setString(1, username);

                    var resultSetId = preparedStmtGetId.executeQuery();
                    int userId = 0;
                    while (resultSetId.next()) {
                        userId = resultSetId.getInt("ID_User");
                    }

                    // Insert no_telp
                    String insertNotlp = "INSERT INTO notlp (ID_User, phoneNumber) VALUES(?, ?)";
                    var preparedStmtInsertTlp = conn.prepareStatement(insertNotlp);

                    preparedStmtInsertTlp.setInt(1, userId);
                    preparedStmtInsertTlp.setString(2, phone);
                    preparedStmtInsertTlp.executeUpdate();
                } else {
                    // Insert data Customer
                    String query = "INSERT INTO Users (name, username, password, email, userType, statusAcc, statusVerify, createdAccAt) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                    var preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, username);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, userType);
                    preparedStatement.setString(6, statusAcc);
                    preparedStatement.setString(7, statusVerify);

                    rowsInserted = preparedStatement.executeUpdate();

                    // Get Id User
                    String getIdSQL = "SELECT ID_User FROM Users WHERE username = ?";
                    var preparedStmtGetId = conn.prepareStatement(getIdSQL);

                    preparedStmtGetId.setString(1, username);

                    var resultSetId = preparedStmtGetId.executeQuery();
                    int userId = 0;
                    while (resultSetId.next()) {
                        userId = resultSetId.getInt("ID_User");
                    }

                    // Insert no_telp
                    String insertNotlp = "INSERT INTO notlp (ID_User, phoneNumber) VALUES(?, ?)";
                    var preparedStmtInsertTlp = conn.prepareStatement(insertNotlp);

                    preparedStmtInsertTlp.setInt(1, userId);
                    preparedStmtInsertTlp.setString(2, phone);
                    preparedStmtInsertTlp.executeUpdate();
                }

                if (rowsInserted > 0) {
                    new LoadingForRegist("Registration successful!", "Success");
                    registerView.dispose(); // Menutup form register
                } else {
                    new LoadingForRegist("Registration failed!", "Failed");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(registerView, "Database error: " + ex.getMessage());
            }
        }
    }

    public RegisterForm getRegisterForm() {
        return this.registerView;
    }
}
