package View;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Admin;

import java.awt.*;
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

            // Menyesuaikan ukuran lingkaran dengan ukuran komponen
            int diameter = Math.min(getWidth(), getHeight());
            BufferedImage circleImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleImage.createGraphics();

            // Membuat lingkaran menggunakan clip
            g2.setClip(new Ellipse2D.Double(0, 0, diameter, diameter));
            g2.drawImage(image, 0, 0, diameter, diameter, null);
            g2.dispose();

            // Menggambar gambar lingkaran ke label
            g2d.drawImage(circleImage, 0, 0, null);
            g2d.dispose();
        } else {
            super.paintComponent(g);
        }
    }
}


public class TemplateMenu extends JFrame {
    private JButton buttons[];
    private String profilePic = "D:\\Trace Maze-1 BFS.png";
    private JLabel greetLabel, profileLabel;
    private JPanel sideBarPanel, buttonPanel, profilePanel;

    private final int WIDTH_FRAME = 900;
    private final int HEIGHT_FRAME = 600;
    private final int WIDTH_SIDEBAR = WIDTH_FRAME / 4;

    private CardLayout cardLayout;
    private JPanel menuPanels;

    public TemplateMenu(String titleFrame, String menuUser[], JPanel panelMenus[], String greetMenu) {
        setTitle(titleFrame);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // nanti diubah ke do nothing
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel Utama Output
        cardLayout = new CardLayout();
        menuPanels = new JPanel(cardLayout);
        menuPanels.setBounds(WIDTH_SIDEBAR, 0, WIDTH_FRAME - WIDTH_SIDEBAR, HEIGHT_FRAME);
        menuPanels.setBorder(BorderFactory.createLineBorder(Color.black,1, true));


        // Tambahkan Homepage Menu
        JLabel greetMenuLabel = new JLabel(greetMenu.toUpperCase(), SwingConstants.CENTER);
        greetMenuLabel.setFont(new Font("Arial", Font.BOLD, 25));
        menuPanels.add(greetMenuLabel, "Home");
        add(menuPanels);

        // Sidebar
        sideBarPanel = new JPanel();
        sideBarPanel.setBounds(0, 0, WIDTH_SIDEBAR, HEIGHT_FRAME);
        sideBarPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        sideBarPanel.setBorder(BorderFactory.createLineBorder(Color.black,1, true));
        // sideBarPanel.setBackground(Color.green);

        // Wadah Foto dan Sapaan
        profilePanel = new JPanel(new BorderLayout());
        profilePanel.setSize(WIDTH_SIDEBAR - 30, ((int) (HEIGHT_FRAME / 3) - 30));

        // Bagian Foto
        profileLabel = new CircularImageLabel(profilePic);
        profileLabel.setSize(profilePanel.getWidth(), (int) (profilePanel.getHeight() * 0.75));
        profileLabel.setBorder(BorderFactory.createEmptyBorder(10, 0,30,-40));

        
        // Bagian Sapaan
        greetLabel = new JLabel("Hello, " + SingletonManger.getInstance().getLoggedInUser().getName(), SwingConstants.CENTER);
        greetLabel.setFont(new Font("Arial", Font.BOLD, 16));
        greetLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        profilePanel.add(profileLabel, BorderLayout.CENTER);
        profilePanel.add(greetLabel, BorderLayout.SOUTH);

        // Menambahkan menu-menu user dan action button
        buttons = new JButton[menuUser.length];
        addMenus(panelMenus, menuUser);
        

        // Menambahkan semua button ke buttonPanel
        buttonPanel = new JPanel(new GridLayout(buttons.length, 1, 0, 10));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }

        sideBarPanel.add(profilePanel, BorderLayout.NORTH);
        sideBarPanel.add(buttonPanel, BorderLayout.CENTER);
        add(sideBarPanel);

        // Patut dipertanyakan ada perbedaan width dan height antara sebelum setVisible
        // dan sesudahnya
        SwingUtilities.invokeLater(this::tampilkanGambar);

        setVisible(true);
    }
    
    public void addMenus(JPanel panelMenu[], String[] menuNames){
        
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(menuNames[i]);
            final String menuName = menuNames[i];

            this.menuPanels.add(panelMenu[i], menuName);
            // buttons[i].addActionListener(e -> cardLayout.show(menuPanels, menuName));

            this.buttons[i].addActionListener(e -> {
                System.out.println("Button clicked: " + menuName);
                cardLayout.show(menuPanels, menuName);
            });
            System.out.println("Current panel: " + cardLayout);

            
        }
    }

    private void tampilkanGambar() {
        // Menampilkan gambar pada JLabel
        ImageIcon imageIcon = new ImageIcon(profilePic);
        // Menyesuaikan ukuran gambar dengan ukuran label
        Image image = imageIcon.getImage().getScaledInstance(profileLabel.getWidth(), profileLabel.getHeight(),
                Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(image));
    }
    
    public static void main(String[] args) {
        SingletonManger.getInstance().setLoggedInUser(new Admin(0, null, "Admin-1", null, null, null, null, null));

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("This is the Manage Customers panel."));
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("This is the Manage Drivers panel."));
        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("This is the Manage Vouchers panel."));
        JPanel panel4 = new JPanel();
        panel4.add(new JLabel("This is the fourth panel."));
        JPanel panel5 = new JPanel();
        panel5.add(new JLabel("This is the fifth panel."));
        JPanel panel6 = new JPanel();
        panel6.add(new JLabel("This is the sixth panel."));
        JPanel panel7 = new JPanel();
        panel7.add(new JLabel("This is the seventh panel."));
        JPanel panel8 = new JPanel();
        panel8.add(new JLabel("This is the eighth panel."));
        JPanel panel9 = new JPanel();
        panel9.add(new JLabel("This is the ninth panel."));
        JPanel panel10 = new JPanel();
        panel10.add(new JLabel("This is the ninth panel."));

        JPanel panels[] = {panel1, panel2, panel3, panel4, panel5, panel6, panel7, panel8, panel9, panel10};


        new TemplateMenu("trial", new String[]{"Manage Customers", "Manage Drivers", "Manage Vouchers", "Panel-4", "Panel-5", "Panel-6", "Panel-7", "Panel-8", "Panel-9", "Panel-10"}, panels, "Welcome to Admin Panel");
    }

}
