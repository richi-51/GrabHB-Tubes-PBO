package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import Model.Enum.UserType;
import View.ShowDriverRatingPage;

public class ShowDriverRating {
    private ShowDriverRatingPage showDriverRatingPage;

    public ShowDriverRating(ShowDriverRatingPage showDriverRatingPage) {
        this.showDriverRatingPage = showDriverRatingPage;
        initializeAvailability();
        showRating();
    }

    private void initializeAvailability() {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        
        if (loggedInUser == null) {
            try (Connection conn = DatabaseHandler.connect()) {
                // Query untuk mendapatkan data pengguna dari database
                String query = "SELECT ID_User, username, name, password, email, availabilityDriver FROM users WHERE username = 'johndoe'";
                PreparedStatement statement = conn.prepareStatement(query);
    
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    // Ambil data dari hasil query
                    int idUser = resultSet.getInt("ID_User");
                    String username = resultSet.getString("username");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    System.out.println(idUser);
                    System.out.println(username);

                    // Buat objek Driver dan set atributnya
                    loggedInUser = new Driver(idUser, username, name, password, email, null, UserType.DRIVER, null, DriverStatus.ONLINE, null, 1, null, null, 4, null);
    
                    // Simpan ke SingletonManger
                    SingletonManger.getInstance().setLoggedInUser(loggedInUser);

                    showDriverRatingPage.setDriverUserName(username);
                } else {
                    JOptionPane.showMessageDialog(showDriverRatingPage, 
                        "Tidak ada data pengguna di database!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(showDriverRatingPage, 
                    "Error saat mengambil data pengguna dari database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showRating() {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());

        if (loggedInUser != null) {
            int driverId = loggedInUser.getIdUser();

            System.out.println(driverId);

            try (Connection conn = DatabaseHandler.connect()){
                String driverUserName = loggedInUser.getUsername();
                showDriverRatingPage.setDriverUserName(driverUserName);

                // Ambil rata-rata rating
                String rateQuery = "SELECT ROUND(AVG(o.rating), 1) AS rating FROM `order` o INNER JOIN users u ON o.ID_Driver = u.ID_User WHERE o.ID_Driver = ?";
                try (PreparedStatement stmt = conn.prepareStatement(rateQuery)) {
                    stmt.setInt(1, driverId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String rate = rs.getString("rating");
                            showDriverRatingPage.setRate(rate != null ? rate : "N/A");
                        } else {
                            showDriverRatingPage.setRate("N/A");
                        }
                    }
                }

                // Ambil jumlah penilaian
                String reviewsQuery = "SELECT COUNT(o.rating) AS penilaian FROM `order` o WHERE o.ID_Driver = ?";
                try (PreparedStatement stmt = conn.prepareStatement(reviewsQuery)) {
                    stmt.setInt(1, driverId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String penilaian = rs.getString("penilaian");
                            showDriverRatingPage.setReviews(penilaian != null ? "("+ penilaian + " Penilaian)": "(0 Penilaian)");
                        } else {
                            showDriverRatingPage.setReviews("(0 Penilaian)");
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(showDriverRatingPage, 
                    "Error koneksi", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(showDriverRatingPage, 
                    "User tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
        }       
    }
}
