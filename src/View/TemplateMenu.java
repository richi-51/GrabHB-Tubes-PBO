package View;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Controller.AuthController;
import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Admin;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Class.Vehicle.Car;
import Model.Class.Vehicle.Vehicle;
import Model.Enum.ServiceType;
import Model.Enum.StatusVerification;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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
    private String profilePic;
    // Sesuaikan lagi, seperti di komputer kalian
    // Harus mencari tahu apakah path bisa tanpa dari root direktori
    
    private JLabel greetLabel, profileLabel;
    private JPanel sideBarPanel, buttonPanel, profilePanel;

    private final int WIDTH_FRAME = 900;
    private final int HEIGHT_FRAME = 600;
    private final int WIDTH_SIDEBAR = WIDTH_FRAME / 4;

    private CardLayout cardLayout;
    private JPanel menuPanels;

    public TemplateMenu(){

    }

    public TemplateMenu(String titleFrame, String menuUser[], Component panelMenus[], String greetMenu) {
        // set default profile pic
        profilePic = getAbsolutePathFoto("../GrabHB-Tubes-PBO/src/Asset/Profile Picture Default.png");

        // ubah profile pic jika sudah di-set sebelumnya
        if (SingletonManger.getInstance().getLoggedInUser().getProfilePicPath() != null) {
            setProfilePicPath(SingletonManger.getInstance().getLoggedInUser().getProfilePicPath());   
        }

        setTitle(titleFrame);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // nanti diubah ke do nothing
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        // Panel Utama Output
        cardLayout = new CardLayout();
        menuPanels = new JPanel(cardLayout);
        menuPanels.setBounds(WIDTH_SIDEBAR, 0, WIDTH_FRAME-15 - WIDTH_SIDEBAR, HEIGHT_FRAME-35);
        menuPanels.setBorder(BorderFactory.createLineBorder(Color.black,1, true));


        // Tambahkan Homepage Menu
        JLabel greetMenuLabel = new JLabel(greetMenu.toUpperCase(), SwingConstants.CENTER);
        greetMenuLabel.setFont(new Font("Arial", Font.BOLD, 25));
        menuPanels.add(greetMenuLabel, "Home");
        add(menuPanels);

        // Sidebar
        sideBarPanel = new JPanel();
        sideBarPanel.setBounds(0, 0, WIDTH_SIDEBAR, HEIGHT_FRAME-35);
        sideBarPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        sideBarPanel.setBorder(BorderFactory.createLineBorder(Color.black,1, true));
        // sideBarPanel.setBackground(Color.green);

        // Wadah Foto dan Sapaan
        profilePanel = new JPanel(new BorderLayout());
        profilePanel.setSize(WIDTH_SIDEBAR - 30, ((int) ((HEIGHT_FRAME-35) / 3) - 30));

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
        System.out.println("Height Panel Template: " + menuPanels.getHeight());
        setVisible(true);
    }
    
    public void addMenus(Component panelMenu[], String[] menuNames){
        
        for (int i = 0; i < buttons.length-1; i++) {
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
        // Untuk Button LogOut
        buttons[buttons.length-1] = new JButton("LogOut");
        buttons[buttons.length-1].addActionListener(e -> {
            this.dispose();

            // Hapus Singletonnya
            SingletonManger.getInstance().setLoggedInUser(null);

            // Tampilkan menu LogIn
            LoginForm loginView = new LoginForm();
            RegisterForm registerView = new RegisterForm();
            new AuthController(loginView, registerView);

            loginView.setVisible(true);

        });
    }

    
    private void tampilkanGambar() {
        // Menampilkan gambar pada JLabel
        ImageIcon imageIcon = new ImageIcon(profilePic);
        // Menyesuaikan ukuran gambar dengan ukuran label
        Image image = imageIcon.getImage().getScaledInstance(profileLabel.getWidth(), profileLabel.getHeight(),
                Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(image));
    }

    public String getAbsolutePathFoto(String relativePath) {
        File file = new File(relativePath);
        System.out.println("Relative path: " + relativePath);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        System.out.println("File tidak ditemukan");
        return "";
    }
    
    public void setMenuInUsed(String menuName){
        cardLayout.show(menuPanels, menuName);
    }

    public void setProfilePicPath(String profilePicPath){
        String path = getAbsolutePathFoto(profilePicPath);
        if (!path.equalsIgnoreCase("")) {
            profilePic = path;
        }
    }

    public int getWidthMenuPanels(){
        return WIDTH_FRAME-15 - WIDTH_SIDEBAR;
    }
    public int getHeightMenuPanels(){
        return HEIGHT_FRAME-35;
    }
    public void closeFrame(){
        this.dispose();
    }


    public static void main(String[] args) {
        Admin admin = new Admin(3, "username_Admin", "Admin-1", "12345", "08765321879", "admin@root.com", null, null, "");

        Vehicle vehicle = new Car(1, "Toyota Alya", "D123XX", 3);
        Driver driver = new Driver(1, "driver_username", "driver_name", "12345", "0814355465776", "driver_email@coba.com", null, null, "", null, null, null, vehicle, null, 0, null, StatusVerification.UNVERIFIED)

        Ovo ovo = new Ovo(2, 5000, 10);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(1, 1, ERROR, null, null, null, null, null, null, null, null, null, 0, 0, null));
        Customer customer = new Customer(2, "Customer_username", "Customer_name", "1234567", "08191234567658", "customer_email@coba.com", null, null, "", null, null, ovo, orders);
        SingletonManger.getInstance().setLoggedInUser(customer);

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

        TemplateMenu tmp = new TemplateMenu();

        Component panels[] = {panel1, panel2, panel3, new TotalPendapatan(tmp, true), new ManageLaporan(tmp), new ManageVoucher(tmp), new HistoryOrder(tmp, false, false, true), new ManageDriver(tmp), new UpdateProfile(tmp), null};

        tmp = new TemplateMenu("trial", new String[]{"Manage Customers", "Accept Order", "Update My Availability", "Panel-4", "Panel-5", "Panel-6", "Panel-7", "Panel-8", "Panel-9"}, panels, "Welcome to Admin Panel");
    }
}
