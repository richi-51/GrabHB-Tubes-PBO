package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import Model.Class.Db.DatabaseHandler;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import View.ShowDriverRatingPage;

public class ShowDriverRating {
    private ShowDriverRatingPage showDriverRatingPage;

    public ShowDriverRating(ShowDriverRatingPage showDriverRatingPage) {
        this.showDriverRatingPage = showDriverRatingPage;
        showRating();
    }

    public void showRating() {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());

        if (loggedInUser != null) {
            int driverId = loggedInUser.getIdUser();

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
