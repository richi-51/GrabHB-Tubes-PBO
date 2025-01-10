package Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Order.Laporan;
import Model.Class.Singleton.SingletonManger;
import Model.Enum.StatusLaporan;

public class ManageLaporanController {
    public ArrayList<Laporan> getLaporans() {
        ArrayList<Laporan> laporans = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect()) {
            String sqlLaporan = "SELECT * FROM laporan";

            var preparedStatement = conn.prepareStatement(sqlLaporan);
            var rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int idLaporan = rs.getInt("ID_Laporan");
                String isiKeluhan = rs.getString("isiKeluhan");


                String statusLaporan = rs.getString("statusLaporan");
                StatusLaporan status = StatusLaporan.DONE;
                if (statusLaporan.equalsIgnoreCase("Waiting")) {
                    status = StatusLaporan.WAITING;
                }else if (statusLaporan.equalsIgnoreCase("On_Process")) {
                    status = StatusLaporan.ON_PROCESS;
                }else{
                    status = StatusLaporan.DONE;
                }

                Date createdAt = rs.getDate("createdAt");
                Date finishAt  = rs.getDate("finishAt");


                laporans.add(new Laporan(idLaporan, isiKeluhan, status, createdAt, finishAt));
            }

            return laporans;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return null;
    }


    public String getAdminNote(int idLaporan){
        try (Connection conn = DatabaseHandler.connect()) {
            String sqlManageLaporan = "SELECT * FROM managelaporan WHERE ID_Laporan = ?";

            var preparedStatement = conn.prepareStatement(sqlManageLaporan);
            preparedStatement.setInt(1, idLaporan);

            var rs = preparedStatement.executeQuery();
            String adminNote = "";
            if (rs.next()) {
                adminNote = rs.getString("admin_note");
            }

            return adminNote;

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return "";
    }


    public boolean saveManageLaporan(int idLaporan, String newAdminNote, String statusLaporan){
        if (!getAdminNote(idLaporan).equalsIgnoreCase("")) {
            try (Connection conn = DatabaseHandler.connect()) {
                String sqlManageLaporanUpdate = "UPDATE managelaporan SET ID_Admin = ?, admin_note = ? WHERE ID_Laporan = ?";
    
                var preparedStatement = conn.prepareStatement(sqlManageLaporanUpdate);
                preparedStatement.setInt(1, SingletonManger.getInstance().getLoggedInUser().getIdUser());
                preparedStatement.setString(2, newAdminNote);
                preparedStatement.setInt(3, idLaporan);
                
                int answer = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin dengan tindakan tersebut?", "Konfirmasi Update Laporan", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    int rowAffected = preparedStatement.executeUpdate();
                    boolean resultStatus = updateStatusLaporan(idLaporan, statusLaporan);
                    if (rowAffected > 0 && resultStatus) {
                        JOptionPane.showMessageDialog(null, "Update berhasil dilakukan!");
                        return true;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            }
        }else{
            try (Connection conn = DatabaseHandler.connect()) {
                String sqlManageLaporanAdd = "INSERT INTO managelaporan (ID_Laporan, ID_Admin, admin_note, assign_date) VALUES(?, ?, ?, CURRENT_DATE())";
    
                var preparedStatement = conn.prepareStatement(sqlManageLaporanAdd);
                preparedStatement.setInt(1, idLaporan);
                preparedStatement.setInt(2, SingletonManger.getInstance().getLoggedInUser().getIdUser());
                preparedStatement.setString(3, newAdminNote);
                
                int answer = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin dengan tindakan tersebut?", "Konfirmasi Manage Laporan", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    int rowAffected = preparedStatement.executeUpdate();
                    boolean resultStatus = updateStatusLaporan(idLaporan, statusLaporan);
                    if (rowAffected > 0 && resultStatus) {
                        JOptionPane.showMessageDialog(null, "Penambahan data berhasil dilakukan!");
                        return true;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            }
        }

        return false;
    }


    public boolean updateStatusLaporan(int idLaporan, String status){
        try (Connection conn = DatabaseHandler.connect()) {
            String sqlUpdateStatusLaporan = "";

            if (status.equalsIgnoreCase("Done")) {
                sqlUpdateStatusLaporan = "UPDATE laporan SET statusLaporan = ?, finishAt = CURRENT_DATE() WHERE ID_Laporan = ?";
            }else{
                sqlUpdateStatusLaporan = "UPDATE laporan SET statusLaporan = ?, finishAt = NULL WHERE ID_Laporan = ?";
            }

            var preparedStatement = conn.prepareStatement(sqlUpdateStatusLaporan);                
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, idLaporan);

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return false;
    }
}

