package Controller;

import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import View.UpdateAvailabilityPage;
import Model.Class.State.DriverContext;
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
        driverContext = new DriverContext((Driver) SingletonManger.getInstance().getLoggedInUser());
        updateAvailabilityView.setAvailabilityStatus(driverContext.getState().getStatus());
        updateAvailabilityView.setButtonLabel(driverContext.getState().getButtonLabel());
        this.updateAvailabilityView.addChangeAvailabilityListener(new ChangeAvailabilityAction());
    }

    private class ChangeAvailabilityAction implements ActionListener { 
        @Override
        public void actionPerformed(ActionEvent e) {
            Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
    
            if (loggedInUser != null) {
                int driverId = loggedInUser.getIdUser();
            
                try (Connection conn = DatabaseHandler.connect()) {
                    if (conn == null) {
                        throw new SQLException("Koneksi ke database gagal!");
                    }
                    driverContext.changeAvailability();

                    String newAvailability = driverContext.getState().getStatus();
                    if (newAvailability.equalsIgnoreCase("Offline")) {
                        newAvailability = "Online";
                        loggedInUser.setAvailability(DriverStatus.ONLINE);
                    } else if (newAvailability.equalsIgnoreCase("Online")) {
                        newAvailability = "Offline";
                        loggedInUser.setAvailability(DriverStatus.OFFLINE);
                    }

                    updateAvailabilityView.setAvailabilityStatus(newAvailability);
                    updateAvailabilityView.setButtonLabel(driverContext.getState().getButtonLabel());

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
