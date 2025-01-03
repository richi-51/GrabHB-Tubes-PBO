package Controller;

import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import Model.Enum.UserType;
import View.UpdateAvailabilityPage;
import Model.Class.Db.DatabaseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateAvailability {
    private UpdateAvailabilityPage updateAvailabilityView;

    public UpdateAvailability(UpdateAvailabilityPage updateAvailabilityView) {
        this.updateAvailabilityView = updateAvailabilityView;
        initializeAvailability();
        this.updateAvailabilityView.addChangeAvailabilityListener(new ChangeAvailabilityAction());
    }

    private void initializeAvailability() {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        
        if (loggedInUser == null) {
            try (Connection conn = DatabaseHandler.connect()) {
                // Query untuk mendapatkan data pengguna dari database
                String query = "SELECT ID_User, username, name, password, email, availabilityDriver FROM users WHERE name = 'Adrian'";
                PreparedStatement statement = conn.prepareStatement(query);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Ambil data dari hasil query
                    int idUser = resultSet.getInt("ID_User");
                    String username = resultSet.getString("username");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    // Buat objek Driver dan set atributnya
                    loggedInUser = new Driver(idUser, username, name, password, email, null, UserType.DRIVER, null, DriverStatus.ONLINE, null, 9, null, null, 4, null);
    
                    // Simpan ke SingletonManger
                    SingletonManger.getInstance().setLoggedInUser(loggedInUser);
    
                    updateAvailabilityView.setAvailabilityStatus("Online");
                    updateAvailabilityView.setButtonLabel("Offline");

                    String updateQuery = "UPDATE users SET availabilityDriver = ? WHERE ID_User = ?";
                    PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                    updateStatement.setString(1, "Online");
                    updateStatement.setInt(2, idUser);

                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated <= 0) {
                        JOptionPane.showMessageDialog(updateAvailabilityView,
                                "Gagal mengatur status awal pengguna di database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(updateAvailabilityView, 
                        "Tidak ada data pengguna di database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(updateAvailabilityView, 
                    "Error saat mengambil data pengguna dari database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener untuk perubahan status dan tombol status
    private class ChangeAvailabilityAction implements ActionListener { 
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ambil pengguna dari Singleton
            Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
    
            if (loggedInUser != null) {
                int driverId = loggedInUser.getIdUser();
            
                try (Connection conn = DatabaseHandler.connect()) {
                    // Periksa apakah koneksi berhasil
                    if (conn == null) {
                        throw new SQLException("Koneksi ke database gagal!");
                    }
                    // Ambil status availability saat ini
                    DriverStatus availability = loggedInUser.getAvailability();
    
                    String newAvailability = "";
    
                    // Tentukan status dan tombol barunya
                    if (availability == DriverStatus.OFFLINE) {
                        newAvailability = "Online";
                        loggedInUser.setAvailability(DriverStatus.ONLINE);
                        updateAvailabilityView.setAvailabilityStatus("Online");
                        updateAvailabilityView.setButtonLabel("Offline");
                    } else if (availability == DriverStatus.ONLINE) {
                        newAvailability = "Offline";
                        loggedInUser.setAvailability(DriverStatus.OFFLINE);
                        updateAvailabilityView.setAvailabilityStatus("Offline");
                        updateAvailabilityView.setButtonLabel("Online");
                    }
    
                    // Update status di database
                    String query = "UPDATE users SET availabilityDriver = ? WHERE ID_User = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, newAvailability);
                    statement.setInt(2, driverId);
    
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(updateAvailabilityView, "Status berhasil diubah.");
                    } else {
                        JOptionPane.showMessageDialog(updateAvailabilityView, "Gagal mengubah status.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(updateAvailabilityView, 
                            "Error saat mengubah status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(updateAvailabilityView, 
                        "User tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}
