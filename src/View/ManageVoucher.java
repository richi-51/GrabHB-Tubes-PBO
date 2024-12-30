package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.ManageCustDriverController;
import Controller.VoucherController;
import Model.Class.Order.Voucher;
import Model.Class.User.Customer;
import Model.Class.Vehicle.Car;
import Model.Enum.OrderStatus;
import Model.Enum.StatusAcc;
import Model.Enum.StatusVerification;

public class ManageVoucher extends JPanel {
    JButton updateButton[], deleteButton[];

    public ManageVoucher(TemplateMenu tmp){
        ArrayList<Voucher> vouchers  = new VoucherController().getVouchers();

        setPreferredSize(new Dimension(tmp.getWidthMenuPanels(), tmp.getHeightMenuPanels()-100));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Vouchers", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setBorder(null);
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(vouchers.size(), 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelVouchers[] = new JPanel[vouchers.size()];

        updateButton = new JButton[vouchers.size()];
        deleteButton = new JButton[vouchers.size()];
        

        for (int i = 0; i < vouchers.size(); i++) {
            panelVouchers[i] = new JPanel(new GridLayout(9,1));
            panelVouchers[i].setPreferredSize(new Dimension(tmp.getWidthMenuPanels() - 30, 250));
            panelVouchers[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelVouchers[i].add(new JLabel("Kode Voucher: " + vouchers.get(i).getKodeVoucher()));
            panelVouchers[i].add(new JLabel("Jenis Layanan: " + vouchers.get(i).getServiceType().toString()));
            panelVouchers[i].add(new JLabel("Jumlah Potongan: " + vouchers.get(i).getJumlahPotongan()));
            panelVouchers[i].add(new JLabel("Valid dari: " + String.valueOf(vouchers.get(i).getValid_from())));
            panelVouchers[i].add(new JLabel("Valid sampai: " + String.valueOf(vouchers.get(i).getValid_to())));
            panelVouchers[i].add(new JLabel("Dibuat pada: " + String.valueOf(vouchers.get(i).getCreated_at())));
            panelVouchers[i].add(new JLabel("Dibuat/Diedit oleh: " + vouchers.get(i).getDibuatOleh()));
            panelVouchers[i].add(new JLabel("Terakhir di-update pada: " + String.valueOf(vouchers.get(i).getUpdated_at())));

            

            final int index = i;
            updateButton[i] = new JButton("UPDATE");
            
            updateButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result = new ManageCustDriverController().updateStatusAccUser(customers.get(index).getID_Customer(), customers.get(index).getStatusAcc());

                    if (result) {
                        if(customers.get(index).getStatusAcc() == StatusAcc.BLOCKED){
                            customers.get(index).setStatusAcc(StatusAcc.UNBLOCKED);
                        }else{
                            customers.get(index).setStatusAcc(StatusAcc.BLOCKED);
                        }
                        updateButton[index].setText(String.valueOf(customers.get(index).getStatusAcc()));
                    }else{
                        JOptionPane.showMessageDialog(null, "Perubahan gagal disimpan!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            panelVouchers[i].add(updateButton[i]);

            containerPanel.add(panelVouchers[i]);
        }

        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(containerPanel);

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
    }



    private void updateVoucherFrame(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Details Driver", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(title, BorderLayout.NORTH);

        JPanel dataDriver = new JPanel(new GridLayout(12, 1, 10,10));
        dataDriver.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataDriver.add(new JLabel("Nama: " + driver.getName()));
        dataDriver.add(new JLabel("Username: " + driver.getUsername()));
        dataDriver.add(new JLabel("Email: " + driver.getEmail()));
        dataDriver.add(new JLabel("Tanggal Bergabung: " + driver.getCreatedAccAt()));
        dataDriver.add(new JLabel("Status Akun: " + driver.getStatusAcc().toString()));
        dataDriver.add(new JLabel("==============================================================="));
        dataDriver.add(new JLabel("DATA KENDARAAN", JLabel.CENTER));
        dataDriver.add(new JLabel("Nama Kendaraan: " + driver.getVehicle().getVehicleName()));

        String jnsKendaraan = (driver.getVehicle() instanceof Car) ? "Car" : "Motorcycle";
        dataDriver.add(new JLabel("Jenis Kendaraan: " + jnsKendaraan));
        dataDriver.add(new JLabel("Plat Nomor: " + driver.getVehicle().getPlateNumber()));
        dataDriver.add(new JLabel("Kapasitas Kendaraan: " + driver.getVehicle().getJumlahSeat()));

        JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));

        JButton verifyDriverButton = new JButton("THIS DRIVER IS VERIFIED");
        verifyDriverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = new ManageCustDriverController().updateStatusVerifyUser(driver.getID_Driver(), driver.getVerificationStatus());

                if (result) {
                    driver.setVerificationStatus(StatusVerification.VERIFIED);
                    verifyButton.setText(String.valueOf(driver.getVerificationStatus()));
                    verifyButton.setEnabled(false);
                    frame.dispose();
                }
            }
        });
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(verifyDriverButton);


        dataDriver.add(panelButton);
        frame.add(dataDriver);
        
        frame.setVisible(true);
    }
}
