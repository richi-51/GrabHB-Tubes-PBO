package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Location.Lokasi;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Order;
import Model.Class.Order.Voucher;
import Model.Class.Singleton.SingletonManger;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Enum.TypeBikeOrder;
import Model.Enum.TypeCarOrder;

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
}
