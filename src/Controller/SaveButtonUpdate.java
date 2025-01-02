package Controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Class.User.User;

public class SaveButtonUpdate {
    public SaveButtonUpdate(String nama, String username, String email, String phoneNum, String password, String profilePhoto, boolean isDriver, String jnsKend, String namaKendaraan, String platNomor, int kapasitasKendaraan) {
        User user = SingletonManger.getInstance().getLoggedInUser();
        try (Connection conn = DatabaseHandler.connect()){
            String sqlUpdateProfile = "UPDATE users SET name = ?, username = ?, password = ?, email = ?, profilePhoto = ?, updateProfileA WHERE ID_User = ?";

            var preparedStmtUser = conn.prepareStatement(sqlUpdateProfile);
            preparedStmtUser.setString(1, nama);
            preparedStmtUser.setString(2, username);
            preparedStmtUser.setString(3, password);
            preparedStmtUser.setString(4, email);
            preparedStmtUser.setString(5, profilePhoto);
            preparedStmtUser.setInt(6, user.getIdUser());

            int rowUserAffected = preparedStmtUser.executeUpdate();


            String sqlUpdateTlp = "UPDATE notlp SET phoneNumber = ? WHERE phoneNumber = ? AND ID_User = ?";

            var preparedStmtNotlp = conn.prepareStatement(sqlUpdateTlp);
            preparedStmtNotlp.setString(1, phoneNum);
            preparedStmtNotlp.setString(2, user.getPhoneNumber());
            preparedStmtNotlp.setInt(3, user.getIdUser());

            int rowTlpAffected = preparedStmtNotlp.executeUpdate();

            if (isDriver) {
                Driver driver = (Driver) user;

                String sqlUpdateVehicle = "UPDATE vehicle SET plateNumber = ?, vehicleName = ?, vehicleType = ?, jumlahSeat = ? WHERE vehicle_ID = ?";

                var preparedStmtVehicleUpdate = conn.prepareStatement(sqlUpdateVehicle);
                preparedStmtVehicleUpdate.setString(1, platNomor);
                preparedStmtVehicleUpdate.setString(2, namaKendaraan);
                preparedStmtVehicleUpdate.setString(3, jnsKend);
                preparedStmtVehicleUpdate.setInt(4, kapasitasKendaraan);
                preparedStmtVehicleUpdate.setInt(5, driver.getVehicle().getVehicle_ID());

                int rowVehicleAffected = preparedStmtVehicleUpdate.executeUpdate();

                if (rowTlpAffected > 0 && rowUserAffected > 0 && rowVehicleAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan", "Update User Profile", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            if (rowTlpAffected > 0 && rowUserAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan", "Update User Profile", JOptionPane.INFORMATION_MESSAGE);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
}
