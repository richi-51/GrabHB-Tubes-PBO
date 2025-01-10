package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UpdateAvailabilityPage extends JPanel {
    private JButton changeAvailability; // Tombol untuk mengubah status
    private JTextField descAvailability; // Menampilkan status availability
    private JLabel statusLabel;
    private JLabel statusDescLabel;

    public UpdateAvailabilityPage(TemplateMenu tmp) {
        setSize(tmp.getWidthMenuPanels(), tmp.getHeightMenuPanels());
        setLayout(new GridBagLayout()); // Menggunakan GridBagLayout

        // Inisialisasi komponen
        statusLabel = new JLabel("Your Status:");
        descAvailability = new JTextField("Online");
        descAvailability.setEditable(false); // Hanya untuk tampilan, tidak bisa diinput
        changeAvailability = new JButton("Offline");

        // Mengatur simbol bulatan hijau menggunakan HTML
        statusDescLabel = new JLabel("<html><span style='color:green;'>&#9679;</span> Anda kembali aktif</html>");
        statusDescLabel.setOpaque(true);
        statusDescLabel.setBackground(Color.WHITE);
        statusDescLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        statusDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusDescLabel.setVerticalAlignment(SwingConstants.CENTER);

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

        // Menambahkan ActionListener untuk tombol
        changeAvailability.addActionListener(e -> toggleAvailability());
    }

    // Mengatur listener untuk tombol
    public void addChangeAvailabilityListener(ActionListener listener) {
        if (listener == null) {
            System.out.println("Listener is null!"); // Debug log
        } else {
            changeAvailability.addActionListener(listener);
            System.out.println("Listener added to changeAvailability button."); // Debug log
        }
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
    public String getStatusDesc(){
        return statusDescLabel.getText();
    }

    public void setStatusDesc(String label){
        statusDescLabel.setText(label);
    }

    // Metode untuk mengganti status dan label tombol
    private void toggleAvailability() {
        if (getAvailabilityStatus().equals("Online")) {
            setAvailabilityStatus("Offline");
            setButtonLabel("Online");
            setStatusDesc("<html><span style='color:red;'>&#9679;</span> Anda sedang tidak aktif</html>f");
        } else {
            setAvailabilityStatus("Online");
            setButtonLabel("Offline");
            setStatusDesc("<html><span style='color:green;'>&#9679;</span> Anda kembali aktif</html>");
        }
    }

}