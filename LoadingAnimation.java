import javax.swing.*;
import java.awt.*;

public class LoadingAnimation extends JFrame {
    private int angle = 0; // Sudut rotasi lingkaran kecil
    
    public LoadingAnimation() {
        setTitle("Loading...");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel untuk menggambar animasi
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gambar lingkaran besar
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawOval(100, 100, 100, 100);
                
                // Gambar lingkaran kecil (animasi)
                double radian = Math.toRadians(angle);
                int x = (int) (150 + 40 * Math.cos(radian));
                int y = (int) (150 + 40 * Math.sin(radian));
                g2d.setColor(Color.BLUE);
                g2d.fillOval(x - 10, y - 10, 20, 20);
            }
        };

        add(panel);

        // Timer untuk mengupdate animasi
        Timer timer = new Timer(50, e -> {
            angle += 5; // Rotasi 5 derajat setiap 50 ms
            if (angle >= 360) angle = 0;
            panel.repaint();
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingAnimation loading = new LoadingAnimation();
            loading.setVisible(true);
        });
    }
}
