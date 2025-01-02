package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.SingletonManger;

public class PendapatanController {
    public String[] getDayOfOrder(boolean isDriver) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT DAY(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT DAY(order_date) FROM `order` WHERE ID_Driver = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
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
                days[i] = results.get(i-1);
            }

            return days;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public String[] getMonthsOfOrder(boolean isDriver) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT MONTH(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT MONTH(order_date) FROM `order` WHERE ID_Driver = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
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
                months[i] = results.get(i-1);
            }

            return months;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public String[] getYearsOfOrder(boolean isDriver) {
        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT DISTINCT YEAR(order_date) FROM `order`";
            if (isDriver) {
                query = "SELECT DISTINCT YEAR(order_date) FROM `order` WHERE ID_Driver = ?";
            }

            var preparedStatement = conn.prepareStatement(query);
            if (isDriver) {
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
                years[i] = results.get(i-1);
            }

            return years;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public double getRevenueAdmin(String day, String month, String year) {
        double total = 0;

        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT price, jumlahPotongan FROM `order` o LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher";

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
            } else if (!year.equalsIgnoreCase("Tahun: ")){
                int tahun = Integer.parseInt(year);
                preparedStatement.setInt(1, tahun);
            }

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                double price = rs.getDouble("price");
                double jmlhPotongan = Double.valueOf(rs.getDouble("jumlahPotongan")) != null ? rs.getDouble("jumlahPotongan") : 0;

                total += price - jmlhPotongan;
            }

            return total;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return total;
    }
    
    public double getRevenueDriver(String day, String month, String year) {
        double total = 0;

        try (Connection conn = DatabaseHandler.connect()) {
            String query = "SELECT price, jumlahPotongan FROM `order` o LEFT JOIN voucher v ON o.ID_Voucher = v.ID_Voucher WHERE o.ID_Driver = ? ";

            if (!day.equalsIgnoreCase("Tanggal: ") || !month.equalsIgnoreCase("Bulan ke-")
                    || !year.equalsIgnoreCase("Tahun: ")) {

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
            } else if (!year.equalsIgnoreCase("Tahun: ")){
                int tahun = Integer.parseInt(year);
                preparedStatement.setInt(2, tahun);
            }

            var rs = preparedStatement.executeQuery();
            while (rs.next()) {
                double price = rs.getDouble("price");
                double jmlhPotongan = Double.valueOf(rs.getDouble("jumlahPotongan")) != null ? rs.getDouble("jumlahPotongan") : 0;

                total += price - jmlhPotongan;
            }

            return total;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return total;
    }
}
