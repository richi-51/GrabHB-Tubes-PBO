package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Laporan;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Class.Payment.Ovo;
import Model.Class.Singleton.SingletonManger;
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

public class HistoryOrderController {
    public String[] getDayOfOrder(boolean isDriver, boolean isCustomer) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT DAY(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT DAY(order_date) FROM `order` WHERE ID_Driver = ?";
            } else if (isCustomer) {
                query = "SELECT DISTINCT DAY(order_date) FROM `order` WHERE ID_Customer = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            } else if (isCustomer) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            }

            ArrayList<String> results = new ArrayList<>();

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                results.add(String.valueOf(rs.getInt("DAY(order_date)")));
            }

            String days[] = new String[results.size() + 1];
            days[0] = "Tanggal: ";
            for (int i = 1; i < results.size() + 1; i++) {
                days[i] = results.get(i - 1);
            }

            return days;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public String[] getMonthsOfOrder(boolean isDriver, boolean isCustomer) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT MONTH(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT MONTH(order_date) FROM `order` WHERE ID_Driver = ?";
            } else if (isCustomer) {
                query = "SELECT DISTINCT MONTH(order_date) FROM `order` WHERE ID_Customer = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            } else if (isCustomer) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            }

            ArrayList<String> results = new ArrayList<>();

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                results.add(String.valueOf(rs.getInt("MONTH(order_date)")));
            }

            String months[] = new String[results.size() + 1];
            months[0] = "Bulan ke-";
            for (int i = 1; i < results.size() + 1; i++) {
                months[i] = results.get(i - 1);
            }

            return months;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public String[] getYearsOfOrder(boolean isDriver, boolean isCustomer) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT YEAR(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT YEAR(order_date) FROM `order` WHERE ID_Driver = ?";
            } else if (isCustomer) {
                query = "SELECT DISTINCT YEAR(order_date) FROM `order` WHERE ID_Customer = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            }
            if (isCustomer) {
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            }

            ArrayList<String> results = new ArrayList<>();

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                results.add(String.valueOf(rs.getInt("YEAR(order_date)")));
            }

            String years[] = new String[results.size() + 1];
            years[0] = "Tahun: ";
            for (int i = 1; i < results.size() + 1; i++) {
                years[i] = results.get(i - 1);
            }

            return years;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public ArrayList<Order> getHistoryOrderAdmin(String day, String month, String year) {

        try (Connection conn = DatabaseHandler.connect()) {
            ArrayList<Order> orders = new ArrayList<>();

            String query = "SELECT * FROM `order` o LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher";

            if (!day.equalsIgnoreCase("Tanggal: ") || !month.equalsIgnoreCase("Bulan ke-")
                    || !year.equalsIgnoreCase("Tahun: ")) {
                query += " WHERE ";

                if (!day.equalsIgnoreCase("Tanggal: ")) {
                    query += "DAY(order_date) = ?";

                    if (!month.equalsIgnoreCase("Bulan ke-")) {
                        query += " AND MONTH(order_date) = ?";

                        if (!year.equalsIgnoreCase("Tahun: ")) {
                            query += " AND YEAR(order_date) = ?";
                        }

                    } else if (!year.equalsIgnoreCase("Tahun: ")) {
                        query += " AND YEAR(order_date) = ?";
                    }

                } else if (!month.equalsIgnoreCase("Bulan ke-")) {
                    query += "MONTH(order_date) = ?";

                    if (!year.equalsIgnoreCase("Tahun: ")) {
                        query += " AND YEAR(order_date) = ?";
                    }
                } else {
                    query += "YEAR(order_date) = ?";
                }
            }

            var preparedStatement = conn.prepareStatement(query);
            if (!day.equalsIgnoreCase("Tanggal: ")) {
                int tanggal = Integer.parseInt(day);
                preparedStatement.setInt(1, tanggal);

                if (!month.equalsIgnoreCase("Bulan ke-")) {
                    int bulan = Integer.parseInt(month);
                    preparedStatement.setInt(2, bulan);

                    if (!year.equalsIgnoreCase("Tahun: ")) {
                        int tahun = Integer.parseInt(year);
                        preparedStatement.setInt(3, tahun);
                    }

                } else if (!year.equalsIgnoreCase("Tahun: ")) {
                    int tahun = Integer.parseInt(year);
                    preparedStatement.setInt(2, tahun);
                }

            } else if (!month.equalsIgnoreCase("Bulan ke-")) {
                int bulan = Integer.parseInt(month);
                preparedStatement.setInt(1, bulan);

                if (!year.equalsIgnoreCase("Tahun: ")) {
                    int tahun = Integer.parseInt(year);
                    preparedStatement.setInt(2, tahun);
                }
            } else if (!year.equalsIgnoreCase("Tahun: ")) {
                int tahun = Integer.parseInt(year);
                preparedStatement.setInt(1, tahun);
            }

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                // Ambil Voucher yang dipake untuk ordernya (jika ada)
                ServiceType serviceTypeVoucher = rs.getString("serviceType").equalsIgnoreCase("GrabCar")
                        ? ServiceType.GRABCAR
                        : ServiceType.GRABBIKE;

                Voucher voucher = new Voucher(rs.getInt("ID_Voucher"),
                        rs.getString("kodeVoucher"), rs.getDouble("jumlahPotongan"),
                        serviceTypeVoucher,
                        rs.getDate("valid_from"), rs.getDate("valid_to"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("update_at").toLocalDateTime(), "");

                ServiceType serviceType = getServiceType(rs.getString("serviceType"));

                if (serviceType == ServiceType.GRABBIKE) {
                    orders.add(new GrabBike(rs.getInt("ID_Order"), rs.getInt("ID_Driver"),
                            rs.getInt("ID_Customer"), voucher, null,
                            getLocation(rs.getInt("ID_WilayahPickUp")),
                            getLocation(rs.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(rs.getString("order_status")),
                            rs.getDate("order_date"), rs.getDate("updatedOrder"),
                            getPaymentMethod(rs.getString("paymentMethod")),
                            rs.getDouble("price"), getTypeBikeOrder(rs.getString("orderType")),
                            rs.getDouble("rating"), rs.getString("ulasan")));
                } else {
                    orders.add(new GrabCar(rs.getInt("ID_Order"), rs.getInt("ID_Driver"),
                            rs.getInt("ID_Customer"), voucher, null,
                            getLocation(rs.getInt("ID_WilayahPickUp")),
                            getLocation(rs.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(rs.getString("order_status")),
                            rs.getDate("order_date"), rs.getDate("updatedOrder"),
                            getPaymentMethod(rs.getString("paymentMethod")),
                            rs.getDouble("price"), getTypeCarOrder(rs.getString("orderType")),
                            rs.getDouble("rating"), rs.getString("ulasan")));
                }
            }

            return orders;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }


    public ArrayList<Order> getHistoryOrderDriverCust(String day, String month, String year, boolean isCustomer) {

        try (Connection conn = DatabaseHandler.connect()) {
            ArrayList<Order> orders = new ArrayList<>();

            String query = "SELECT * FROM `order` o LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher WHERE o.ID_Driver = ? ";

            if (isCustomer) {
                query = "SELECT * FROM `order` o LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher WHERE o.ID_Customer = ? ";
            }

            if (!day.equalsIgnoreCase("Tanggal: ") || !month.equalsIgnoreCase("Bulan ke-") || !year.equalsIgnoreCase("Tahun: ")) {

                if (!day.equalsIgnoreCase("Tanggal: ")) {
                    query += "AND DAY(order_date) = ?";

                    if (!month.equalsIgnoreCase("Bulan ke-")) {
                        query += " AND MONTH(order_date) = ?";

                        if (!year.equalsIgnoreCase("Tahun: ")) {
                            query += " AND YEAR(order_date) = ?";
                        }

                    } else if (!year.equalsIgnoreCase("Tahun: ")) {
                        query += " AND YEAR(order_date) = ?";
                    }

                } else if (!month.equalsIgnoreCase("Bulan ke-")) {
                    query += "AND MONTH(order_date) = ?";

                    if (!year.equalsIgnoreCase("Tahun: ")) {
                        query += " AND YEAR(order_date) = ?";
                    }
                } else {
                    query += "AND YEAR(order_date) = ?";
                }
            }

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
            if (!day.equalsIgnoreCase("Tanggal: ")) {
                int tanggal = Integer.parseInt(day);
                preparedStatement.setInt(2, tanggal);

                if (!month.equalsIgnoreCase("Bulan ke-")) {
                    int bulan = Integer.parseInt(month);
                    preparedStatement.setInt(3, bulan);

                    if (!year.equalsIgnoreCase("Tahun: ")) {
                        int tahun = Integer.parseInt(year);
                        preparedStatement.setInt(4, tahun);
                    }

                } else if (!year.equalsIgnoreCase("Tahun: ")) {
                    int tahun = Integer.parseInt(year);
                    preparedStatement.setInt(3, tahun);
                }

            } else if (!month.equalsIgnoreCase("Bulan ke-")) {
                int bulan = Integer.parseInt(month);
                preparedStatement.setInt(2, bulan);

                if (!year.equalsIgnoreCase("Tahun: ")) {
                    int tahun = Integer.parseInt(year);
                    preparedStatement.setInt(3, tahun);
                }
            } else if (!year.equalsIgnoreCase("Tahun: ")) {
                int tahun = Integer.parseInt(year);
                preparedStatement.setInt(2, tahun);
            }

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                // Ambil Voucher yang dipake untuk ordernya (jika ada)
                ServiceType serviceTypeVoucher = rs.getString("serviceType").equalsIgnoreCase("GrabCar")
                        ? ServiceType.GRABCAR
                        : ServiceType.GRABBIKE;

                Voucher voucher = new Voucher(rs.getInt("ID_Voucher"),
                        rs.getString("kodeVoucher"), rs.getDouble("jumlahPotongan"),
                        serviceTypeVoucher,
                        rs.getDate("valid_from"), rs.getDate("valid_to"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("update_at").toLocalDateTime(), "");

                ServiceType serviceType = getServiceType(rs.getString("serviceType"));

                if (serviceType == ServiceType.GRABBIKE) {
                    orders.add(new GrabBike(rs.getInt("ID_Order"), rs.getInt("ID_Driver"),
                            rs.getInt("ID_Customer"), voucher, null,
                            getLocation(rs.getInt("ID_WilayahPickUp")),
                            getLocation(rs.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(rs.getString("order_status")),
                            rs.getDate("order_date"), rs.getDate("updatedOrder"),
                            getPaymentMethod(rs.getString("paymentMethod")),
                            rs.getDouble("price"), getTypeBikeOrder(rs.getString("orderType")),
                            rs.getDouble("rating"), rs.getString("ulasan")));
                } else {
                    orders.add(new GrabCar(rs.getInt("ID_Order"), rs.getInt("ID_Driver"),
                            rs.getInt("ID_Customer"), voucher, null,
                            getLocation(rs.getInt("ID_WilayahPickUp")),
                            getLocation(rs.getInt("ID_WilayahDestination")), serviceType,
                            getOrderStatus(rs.getString("order_status")),
                            rs.getDate("order_date"), rs.getDate("updatedOrder"),
                            getPaymentMethod(rs.getString("paymentMethod")),
                            rs.getDouble("price"), getTypeCarOrder(rs.getString("orderType")),
                            rs.getDouble("rating"), rs.getString("ulasan")));
                }
            }

            return orders;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }



    public Driver getDetailDriver(int id_user){
        try (Connection conn = DatabaseHandler.connect()) {
            String sqlCustomer = "SELECT * FROM users u LEFT JOIN notlp n ON u.ID_User = n.ID_User WHERE u.ID_User = ?";

            var preparedStatement = conn.prepareStatement(sqlCustomer);
            preparedStatement.setInt(1, id_user);

            var rs = preparedStatement.executeQuery();

            if (rs.next()) {
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

                Driver driver = new Driver(
                        userId, username, name, password, phone, email, updateProfileAt, UserType.DRIVER,
                        rs.getString("profilePhoto"), getStatusAcc(statusAcc), DriverStatus.ONLINE, createdAccAt,
                        getVehicle(vehicleId), null, ratingDriver,
                        getOvo(rs.getInt("ID_Tlp")), getVerificationStatus(rs.getString("statusVerify")));
                
                return driver;
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
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

    private double getRatingDriver(ArrayList<Order> orders) {
        double rating = 0;
        for (Order order : orders) {
            rating += order.getRatingOrder();
        }
        return rating / orders.size();
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



    public void updateRating(int idOrder, double rating, String ulasan) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "UPDATE `order` SET rating = ?, ulasan = ? WHERE ID_Order = ?";

            var preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, rating);
            preparedStmt.setString(2, ulasan);
            preparedStmt.setInt(3, idOrder);

            int rows = preparedStmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Terima kasih atas ulasan dan rating Anda", "Infomation Message", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

    }
    
    
    public void insertOrderReport(int idOrder, String keluhan) {
        try (Connection conn = DatabaseHandler.connect()) {
            boolean cek = isLaporanExist(idOrder);

            String query = "INSERT INTO laporan (isiKeluhan, statusLaporan, createdAt, ID_Order) VALUES (?, ?, CURRENT_DATE(), ?)";
            if (cek) {
               query = "UPDATE laporan SET isiKeluhan = ?, statusLaporan = ?, createdAt = CURRENT_DATE() WHERE ID_Order = ?";
            }

            var preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, keluhan);
            preparedStmt.setString(2, "Waiting");
            preparedStmt.setInt(3, idOrder);

            int answer = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin akan melaporkan Order ini?", "Confirmation Report", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                int rows = preparedStmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Keluhan berhasil dilaporkan.", "Infomation Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }

    }



    private boolean isLaporanExist(int idOrder) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT * FROM laporan WHERE ID_Order = ?";

            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, idOrder);

            var rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return false;
    }
    
}
