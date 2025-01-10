package Controller;

import Model.Class.Singleton.SingletonManger;
import Model.Class.State.DriverContext;
import Model.Class.User.Driver;
import View.UpdateAvailabilityPage;
import Model.Class.Db.DatabaseHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAvailability {
    private UpdateAvailabilityPage updateAvailabilityView;
    private DriverContext driverContext;

    public UpdateAvailability(UpdateAvailabilityPage updateAvailabilityView) {
        this.updateAvailabilityView = updateAvailabilityView;

        // Validasi pengguna
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(updateAvailabilityView, "User tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println(loggedInUser.getID_Driver());
        // Inisialisasi Context
        driverContext = new DriverContext(loggedInUser);

        // Set initial UI state
        updateAvailabilityView.setAvailabilityStatus(driverContext.getState().getStatus());
        updateAvailabilityView.setButtonLabel(driverContext.getState().getButtonLabel());

        // Add listener
        this.updateAvailabilityView.addChangeAvailabilityListener(new ChangeAvailabilityAction());
    }

    private class ChangeAvailabilityAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    JOptionPane.showMessageDialog(updateAvailabilityView, "Koneksi ke database gagal!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println(driverContext.getDriver().getIdUser());
                // Change state
                driverContext.changeAvailability();

                // Update database
                String query = "UPDATE users SET availabilityDriver = ? WHERE ID_User = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, driverContext.getState().getStatus());
                statement.setInt(2, driverContext.getDriver().getIdUser());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(updateAvailabilityView, "Status berhasil diubah.");
                    updateAvailabilityView.setAvailabilityStatus(driverContext.getState().getStatus());
                    updateAvailabilityView.setButtonLabel(driverContext.getState().getButtonLabel());
                } else {
                    JOptionPane.showMessageDialog(updateAvailabilityView, "Gagal mengubah status.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(updateAvailabilityView, "Error saat mengubah status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
