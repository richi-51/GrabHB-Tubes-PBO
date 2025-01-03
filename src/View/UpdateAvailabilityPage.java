package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UpdateAvailabilityPage extends JFrame {
    private JButton changeAvailability; // Tombol untuk mengubah status
    private JTextField descAvailability; // Menampilkan status availability
    private JLabel statusLabel;

    public UpdateAvailabilityPage() {
        setTitle("Update Availability");
        setSize(350, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Menggunakan GridBagLayout
        setLocationRelativeTo(null);

        // Inisialisasi komponen
        statusLabel = new JLabel("Your Status:");
        descAvailability = new JTextField("Online");
        descAvailability.setEditable(false); // Hanya untuk tampilan, tidak bisa diinput
        changeAvailability = new JButton("Offline");
        JLabel statusDescLabel = new JLabel("\u25CF Anda kembali aktif"); // Simbol bulatan dan deskripsi

        // Pengaturan GridBagConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan komponen ke frame
        c.gridx = 0;
        c.gridy = 0;
        add(statusLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        add(descAvailability, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        add(statusDescLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(changeAvailability, c);
    }

    // Mengatur listener untuk tombol
    public void addChangeAvailabilityListener(ActionListener listener) {
        changeAvailability.addActionListener(listener);
    }

    // Mengatur teks pada JTextField (status availability)
    public void setAvailabilityStatus(String status) {
        descAvailability.setText(status);
    }

    // Mengatur teks pada tombol
    public void setButtonLabel(String label) {
        changeAvailability.setText(label);
    }

    public String getAvailabilityStatus() {
        return descAvailability.getText();
    }
}
