package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Order.Voucher;
import Model.Class.Db.DatabaseHandler;
import Model.Enum.ServiceType;

public class VoucherController {
    public ArrayList<Voucher> getVouchers() {
        ArrayList<Voucher> vouchers = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect()) {
            String sqlVoucher = "SELECT * FROM voucher v INNER JOIN users u ON u.ID_User = v.ID_Admin";

            var preparedStatement = conn.prepareStatement(sqlVoucher);
            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idVoucher = rs.getInt("ID_Voucher");
                String kodeVoucher = rs.getString("kodeVoucher");
                ServiceType serviceType = rs.getString("serviceType").equalsIgnoreCase("GrabCar") ? ServiceType.GRABCAR : ServiceType.GRABBIKE;
                double jumlahPotongan = rs.getDouble("jumlahPotongan");
                Date validFrom = rs.getDate("valid_from");
                Date validTo = rs.getDate("valid_to");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updateAt = rs.getTimestamp("update_at").toLocalDateTime();
                String dibuat_dieditOleh = rs.getString("name");

                vouchers.add(new Voucher(idVoucher, kodeVoucher, jumlahPotongan, serviceType, validFrom, validTo, createdAt, updateAt, dibuat_dieditOleh));
            }

            return vouchers;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }

    public void addVoucher(int idAdmin, String kodeVoucher, String serviceType, double jmlhPotongan, Date validFrom, Date validTo){
        try (Connection conn = DatabaseHandler.connect()) {
            String insertVoucher = "INSERT INTO voucher (ID_Admin, kodeVoucher, serviceType, jumlahPotongan, valid_from, valid_to, created_at) VALUES (?, ?, ?, ?, ?, ?, NOW())";

            var preparedStatement = conn.prepareStatement(insertVoucher);
            preparedStatement.setInt(1, idAdmin);
            preparedStatement.setString(2, kodeVoucher);
            preparedStatement.setString(3, serviceType);
            preparedStatement.setDouble(4, jmlhPotongan);
            preparedStatement.setDate(5, validFrom);
            preparedStatement.setDate(6, validTo);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Penambahan Voucher Berhasil!", "Success Message", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Penambahan Voucher Gagal!", "Failed Message", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    public void updateVoucher(int id_voucher, int idAdmin, String kodeVoucher, String serviceType, double jmlhPotongan, Date validFrom, Date validTo){
        try (Connection conn = DatabaseHandler.connect()) {
            String updateVoucher = "UPDATE voucher SET ID_Admin = ?, kodeVoucher = ?, serviceType = ?, jumlahPotongan = ?, valid_from = ?, valid_to = ?, update_at = NOW() WHERE ID_Voucher = ?";

            var preparedStatement = conn.prepareStatement(updateVoucher);
            preparedStatement.setInt(1, idAdmin);
            preparedStatement.setString(2, kodeVoucher);
            preparedStatement.setString(3, serviceType);
            preparedStatement.setDouble(4, jmlhPotongan);
            preparedStatement.setDate(5, validFrom);
            preparedStatement.setDate(6, validTo);
            preparedStatement.setInt(7, id_voucher);

            int rowAffected = preparedStatement.executeUpdate();
            
            if (rowAffected > 0) {
                JOptionPane.showMessageDialog(null, "Update Voucher Berhasil!", "Success Message", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "Update Voucher Gagal!", "Failed Message", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
    
    public void deleteVoucher(int id_voucher){
        try (Connection conn = DatabaseHandler.connect()) {
            String deleteVoucher = "DELETE FROM voucher WHERE ID_Voucher = ?";

            var preparedStatement = conn.prepareStatement(deleteVoucher);
            preparedStatement.setInt(1, id_voucher);

            int answer = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin akan mengahapus Voucher tersebut?", "Delete Voucher", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                int rowAffected = preparedStatement.executeUpdate();
                
                if (rowAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Update Voucher Berhasil!", "Success Message", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Update Voucher Gagal!", "Failed Message", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
}
