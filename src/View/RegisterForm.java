package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterForm extends JFrame {
    private JTextField nameField, usernameField, emailField, phoneNumberField, platNo;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JCheckBox driverRegister;
    private JRadioButton carType, motorcycleType;
    private ButtonGroup vehicleType;
    private JLabel vehicleLabel, platNoLabel;

    public RegisterForm() {
        setTitle("Register");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
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

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        vehicleLabel = new JLabel("Jenis Kendaraan: ");
        formPanel.add(vehicleLabel);

        JPanel vehicleTypePanel = new JPanel();
        carType = new JRadioButton("Car");
        carType.setActionCommand("Car");

        motorcycleType = new JRadioButton("Motorcycle");
        motorcycleType.setActionCommand("Motorcycle");

        vehicleType = new ButtonGroup();
        vehicleType.add(carType);
        vehicleType.add(motorcycleType);

        vehicleTypePanel.add(carType);
        vehicleTypePanel.add(motorcycleType);
        formPanel.add(vehicleTypePanel);

        platNoLabel = new JLabel("Plat Nomor: ");
        formPanel.add(platNoLabel);
        platNo = new JTextField();
        formPanel.add(platNo);

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

        platNo.setVisible(isSelected);
        platNoLabel.setVisible(isSelected);
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

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
}
