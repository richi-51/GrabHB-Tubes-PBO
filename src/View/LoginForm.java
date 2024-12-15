package View;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField usernameEmailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Constructor
    public LoginForm() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel buat form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        formPanel.add(new JLabel("Username / Email:"));
        usernameEmailField = new JTextField();
        formPanel.add(usernameEmailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        JLabel titleLabel = new JLabel("WELCOME TO GRABHB", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20,10,0,10));
        
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getUsernameEmail() {
        return usernameEmailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }
}
