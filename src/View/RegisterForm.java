package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterForm extends JFrame {
    private JTextField nameField, usernameField, emailField, phoneNumberField, platNo, namaKend, kapasitasKend;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JCheckBox driverRegister;
    private JRadioButton carType, motorcycleType;
    private ButtonGroup vehicleType;
    private JLabel vehicleLabel, platNoLabel, namaKendLabel, kapasitasKendLabel;

    public RegisterForm() {
        setTitle("Register");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        formPanel.add(new JLabel("DAFTAR SEBAGAI DRIVER?"));
        driverRegister = new JCheckBox("YES");
        driverRegister.addActionListener(e->toggleVehicleType(driverRegister.isSelected()));
        formPanel.add(driverRegister);


        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        formPanel.add(phoneNumberField);

        formPanel.add(new JLabel("Password:")); // kalau sempat tambahin confirm password dibawahnya
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        vehicleLabel = new JLabel("Jenis Kendaraan: ");
        formPanel.add(vehicleLabel);

        JPanel vehicleTypePanel = new JPanel(new BorderLayout());
        carType = new JRadioButton("Car");
        carType.setActionCommand("Car");
        carType.addActionListener(e -> setCapacity1ForMotor());

        motorcycleType = new JRadioButton("Motorcycle");
        motorcycleType.setActionCommand("Motorcycle");
        motorcycleType.addActionListener(e-> setCapacity1ForMotor());

        vehicleType = new ButtonGroup();
        vehicleType.add(carType);
        vehicleType.add(motorcycleType);

        vehicleTypePanel.add(carType, BorderLayout.WEST);
        vehicleTypePanel.add(motorcycleType, BorderLayout.EAST);
        formPanel.add(vehicleTypePanel);

        namaKendLabel = new JLabel("Nama Kendaraan: ");
        formPanel.add(namaKendLabel);
        namaKend = new JTextField();
        formPanel.add(namaKend);

        platNoLabel = new JLabel("Plat Nomor: ");
        formPanel.add(platNoLabel);
        platNo = new JTextField();
        formPanel.add(platNo);

        kapasitasKendLabel = new JLabel("Kapasitas Penumpang: ");
        formPanel.add(kapasitasKendLabel);
        kapasitasKend = new JTextField();
        formPanel.add(kapasitasKend);

        registerButton = new JButton("Register");
        add(formPanel, BorderLayout.CENTER);
        add(registerButton, BorderLayout.SOUTH);


        // Set Invisible untuk form register driver sebelum di-klik
        SwingUtilities.invokeLater(()->{
            toggleVehicleType(driverRegister.isSelected());
        });
    }

    private void toggleVehicleType(boolean isSelected) {
        carType.setVisible(isSelected);
        motorcycleType.setVisible(isSelected);
        vehicleLabel.setVisible(isSelected);

        namaKendLabel.setVisible(isSelected);
        namaKend.setVisible(isSelected);

        kapasitasKendLabel.setVisible(isSelected);
        kapasitasKend.setVisible(isSelected);

        platNo.setVisible(isSelected);
        platNoLabel.setVisible(isSelected);
    }

    private void setCapacity1ForMotor(){
        if (motorcycleType.isSelected()) {
            kapasitasKend.setText("1");
            kapasitasKend.setEnabled(false);
        }else{
            kapasitasKend.setEnabled(true);

        }
    }

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
        return new String(passwordField.getPassword());
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

    public boolean getIsDriverRegisterSelected(){
        return driverRegister.isSelected();
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
}
