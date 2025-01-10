package View;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

class CircularImageLabel extends JLabel {
    private BufferedImage image;

    public CircularImageLabel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath)); // Membaca gambar dari file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            int diameter = Math.min(getWidth(), getHeight());
            BufferedImage circleImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();

            g2.setClip(new Ellipse2D.Double(0, 0, diameter, diameter));
            g2.drawImage(image, 0, 0, diameter, diameter, null);
            g2.dispose();

            g2d.drawImage(circleImage, 0, 0, null);
            g2d.dispose();
        } else {
            super.paintComponent(g);
        }
    }
}

public class ShowDriverRatingPage extends JFrame {
    private JLabel driverUserName, rate, profileLabel, star, reviews;
    private JPanel profilePanel;
    private String profilePic = "src/Images/user.png";
    private String starPic = "src/Images/star.png"; // Pastikan file star.png ada di folder ini
    private final int WIDTH_FRAME = 900;
    private final int HEIGHT_FRAME = 600;

    public ShowDriverRatingPage() {
        setTitle("Show Driver Rating");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel driverRatePanel = new JPanel();
        driverRatePanel.setLayout(new BoxLayout(driverRatePanel, BoxLayout.Y_AXIS));
        driverRatePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        driverRatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));

        profilePanel = new JPanel();

        profileLabel = new JLabel();
        CircularImageLabel profileLabel = new CircularImageLabel(profilePic);
        profileLabel.setPreferredSize(new Dimension(120, 120));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        driverUserName = new JLabel();
        driverUserName.setFont(new Font("Arial", Font.BOLD, 28));
        driverUserName.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(profileLabel);
        profilePanel.add(driverUserName);

        JPanel ratePanel = new JPanel();
        ratePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        star = new JLabel();
        tampilkanGambar(star, starPic, 29, 29);

        rate = new JLabel();
        rate.setFont(new Font("Arial", Font.PLAIN, 28));
        reviews = new JLabel("");
        reviews.setFont(new Font("Arial", Font.PLAIN, 28));

        ratePanel.add(star);
        ratePanel.add(rate);
        ratePanel.add(reviews);

        driverRatePanel.add(profilePanel);
        driverRatePanel.add(ratePanel);

        add(driverRatePanel);
        setVisible(true);
    }

    private void tampilkanGambar(JLabel fotoPanel, String pathFile, int width, int height) {
        try {
            ImageIcon imageIcon = new ImageIcon(pathFile);
            Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            fotoPanel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            System.out.println("Gambar tidak ditemukan: " + pathFile);
        }
    }

    public void setDriverUserName(String name){
        driverUserName.setText(name);
    }

    public void setRate(String rateText){
        rate.setText(rateText);
    }

    public void setReviews(String reviewText){
        reviews.setText(reviewText);
    }

    public static void main(String[] args) {
        new ShowDriverRatingPage();
    }
}