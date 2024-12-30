package View;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Controller.SaveButtonUpdate;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Class.User.User;
import Model.Class.Vehicle.Car;

public class UpdateProfile extends JPanel {
    private JTextField nameField, usernameField, emailField, phoneNumberField, platNo, namaKend, kapasitasKend,passwordField;
    private JButton saveButton;
    private JRadioButton carType, motorcycleType;
    private ButtonGroup vehicleType;
    private JLabel vehicleLabel, platNoLabel, namaKendLabel, kapasitasKendLabel, profileLabel;
    private String pathFotoProfile;
    private User user = SingletonManger.getInstance().getLoggedInUser();

    
    public UpdateProfile(TemplateMenu templateMenu) {
        setSize(templateMenu.getWidthMenuPanels(), templateMenu.getHeightMenuPanels());
        setLayout(new BorderLayout());        

        // set default profile pic
        pathFotoProfile = templateMenu.getAbsolutePathFoto("../GrabHB-Tubes-PBO/src/Asset/Profile Picture Default.png");

        // ubah profile pic jika sudah di-set sebelumnya
        String path = templateMenu.getAbsolutePathFoto(user.getProfilePicPath());
        if (!path.equalsIgnoreCase("")) {
            pathFotoProfile = path;
        }

        if (user instanceof Driver) {
            Driver driver = (Driver) user;
            String jnsKendaraan = (driver.getVehicle() instanceof Car) ? "Car" : "Motorcycle";

            add(createProfileDisplay(driver.getName(), driver.getUsername(), driver.getEmail(), driver.getPhoneNumber(), driver.getPassword(), true, templateMenu.getWidthMenuPanels(), templateMenu.getHeightMenuPanels(), jnsKendaraan, driver.getVehicle().getVehicleName(), driver.getVehicle().getPlateNumber(), driver.getVehicle().getJumlahSeat()), BorderLayout.CENTER);
        }else{
            add(createProfileDisplay(user.getName(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getPassword(), false, templateMenu.getWidthMenuPanels(), templateMenu.getHeightMenuPanels(), "", "", "", 0), BorderLayout.CENTER);
        }

    }


    public JScrollPane createProfileDisplay(String nama, String username, String email, String phoneNum, String password, boolean isDriver, int widthPanel, int heightPanel, String jnsKend, String namaKendaraan, String platNomor, int kapasitasKendaraan){
        // Panel Utama
        JPanel panelUtama = new JPanel();
        panelUtama.setSize(widthPanel, heightPanel);
        panelUtama.setLayout(new BorderLayout());

        JLabel title = new JLabel("USER PROFILE", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panelUtama.add(title, BorderLayout.NORTH);

        // Panel Data (form + profile)
        JPanel panelData = new JPanel(null);

        // Wadah Foto dan Sapaan
        JPanel profilePanel = new JPanel(new BorderLayout());
        int profilePanelWidth = (int)(widthPanel/5);
        int profilePanelHeight = ((int) (heightPanel / 3) - 30);
        int xProfilePanel = (int)((widthPanel - profilePanelWidth)/2);
        int yProfilePanel = title.getHeight() + 25;
        profilePanel.setBounds(xProfilePanel, yProfilePanel, profilePanelWidth, profilePanelHeight);

        // Bagian Foto
        profileLabel = new JLabel();
        profileLabel.setSize(profilePanel.getWidth(), (int) (profilePanel.getHeight() * 0.75));
        profileLabel.setBorder(BorderFactory.createEmptyBorder(10, 0,30,-40));

        JButton changeFotoButton = new JButton("Change Photo");
        changeFotoButton.addActionListener(e-> pilihFileGambar());

        profilePanel.add(profileLabel, BorderLayout.CENTER);
        profilePanel.add(changeFotoButton, BorderLayout.SOUTH);

        panelData.add(profilePanel, BorderLayout.NORTH);

        // Untuk menentukan jumlah row sesuai user
        int jmlhRow = isDriver ? 10 : 5;

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(jmlhRow, 2, 10, 10));
        
        int formPanelWidth = widthPanel-50;
        int formPanelHeight = heightPanel - profilePanelHeight - 100;
        int xFormPanel = 25;
        int yFormPanel = (heightPanel-formPanelHeight) - 85;
        formPanel.setBounds(xFormPanel, yFormPanel, formPanelWidth, formPanelHeight);
    
        System.out.println("Height formPanelUpdate: " + formPanelHeight);
        System.out.println("Height profilePanel: " + profilePanelHeight);
        
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField(nama);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(username);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(email);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(phoneNum);
        formPanel.add(phoneNumberField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JTextField(password);
        formPanel.add(passwordField);


        saveButton = new JButton("Save");

        if (isDriver) {
            vehicleLabel = new JLabel("Jenis Kendaraan: ");
            formPanel.add(vehicleLabel);

            JPanel vehicleTypePanel = new JPanel(new BorderLayout());
            carType = new JRadioButton("Car");
            carType.setActionCommand("Car");
            carType.addActionListener(e -> setCapacity1ForMotor());

            motorcycleType = new JRadioButton("Motorcycle");
            motorcycleType.setActionCommand("Motorcycle");
            motorcycleType.addActionListener(e-> setCapacity1ForMotor());

            // Set buat jenis kendaraan yang dipilih
            if (jnsKend.equalsIgnoreCase("Car")) {
                carType.setSelected(true);
            }else{
                motorcycleType.setSelected(true);
            }

            vehicleType = new ButtonGroup();
            vehicleType.add(carType);
            vehicleType.add(motorcycleType);

            vehicleTypePanel.add(carType, BorderLayout.WEST);
            vehicleTypePanel.add(motorcycleType, BorderLayout.EAST);
            formPanel.add(vehicleTypePanel);

            namaKendLabel = new JLabel("Nama Kendaraan: ");
            formPanel.add(namaKendLabel);
            namaKend = new JTextField(namaKendaraan);
            formPanel.add(namaKend);

            platNoLabel = new JLabel("Plat Nomor: ");
            formPanel.add(platNoLabel);
            platNo = new JTextField(platNomor);
            formPanel.add(platNo);

            kapasitasKendLabel = new JLabel("Kapasitas Penumpang: ");
            formPanel.add(kapasitasKendLabel);
            kapasitasKend = new JTextField(String.valueOf(kapasitasKendaraan));
            formPanel.add(kapasitasKend);
        }
        int saveButtonWidth = (int)(widthPanel/2);
        int saveButtonHeight = 25;
        int xSaveButton = (int)((widthPanel-saveButtonWidth)/2);
        int ySaveButton = (heightPanel-saveButtonHeight) - 65;

        saveButton.setBounds(xSaveButton,ySaveButton, saveButtonWidth, saveButtonHeight);

        saveButton.addActionListener(e -> new SaveButtonUpdate(nama, username, email, phoneNum, password, pathFotoProfile, isDriver, jnsKend, namaKendaraan, platNomor, kapasitasKendaraan));
        
        panelData.add(formPanel);
        panelData.add(saveButton);

        panelUtama.add(panelData, BorderLayout.CENTER);
        
        JScrollPane scroll = new JScrollPane(panelUtama);
        scroll.setBorder(null);

        SwingUtilities.invokeLater(()-> tampilkanGambar(pathFotoProfile));
        return scroll;
    }
    
    private void setCapacity1ForMotor(){
        if (motorcycleType.isSelected()) {
            kapasitasKend.setText("1");
            kapasitasKend.setEnabled(false);
        }else{
            kapasitasKend.setEnabled(true);

        }
    }

    // Fungsi untuk memilih dan menyimpan file
    private void pilihFileGambar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Filter hanya untuk file gambar
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }
            
            @Override
            public String getDescription() {
                return "File Gambar (*.jpg, *.jpeg, *.png)";
            }
        });

        int result = fileChooser.showOpenDialog(null);
        // Jika pengguna memilih file
        if (result == JFileChooser.APPROVE_OPTION) {
            pathFotoProfile = fileChooser.getSelectedFile().getAbsolutePath();

            // Update profile path user
            user.setProfilePicPath(pathFotoProfile);

            // Update tampilan profile sesuai dengan file yang dipilih
            tampilkanGambar(pathFotoProfile);
            this.revalidate();
            this.repaint();

        }
    }

    private void tampilkanGambar(String profilePic) {
        // Menampilkan gambar pada JLabel
        ImageIcon imageIcon = new ImageIcon(profilePic);
        // Menyesuaikan ukuran gambar dengan ukuran label
        Image image = imageIcon.getImage().getScaledInstance(profileLabel.getWidth(), profileLabel.getHeight(),
                Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(image));
    }



    // Getter and Setter 
    public String getName() {
        return nameField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getVehicleType(){
        if (carType.isSelected()) {
            return carType.getActionCommand();
        }else{
            return motorcycleType.getActionCommand();
        }
    }

    public String getPlatNo(){
        return platNo.getText();
    }

    public String getNamaKend(){
        return namaKend.getText();
    }

    public String getKapasitasKend(){
        return kapasitasKend.getText();
    }
}
