package Controller;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Order.Voucher;
import Model.Enum.ServiceType;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoucherCustomer {

    public List<Voucher> getActiveVouchers(String serviceType) {
        List<Voucher> vouchers = new ArrayList<>();
        String query = "SELECT ID_Voucher, kodeVoucher, jumlahPotongan, valid_from, valid_to, created_at, updated_at " +
                "FROM vouchers " +
                "WHERE valid_from <= CURRENT_DATE AND valid_to >= CURRENT_DATE AND serviceType = '" + serviceType +"'";

        try (Connection conn = DatabaseHandler.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            while (rs.next()) {
                Voucher voucher = new Voucher(rs.getInt("ID_Voucher"),
                rs.getString("kodeVoucher"),
                rs.getDouble("jumlahPotongan"), 
                rs.getDate("valid_from"), 
                rs.getDate("valid_to"), 
                rs.getDate("created_at"), 
                rs.getDate("updated_at"));
                vouchers.add(voucher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vouchers;
    }
}