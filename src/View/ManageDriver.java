package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.ManageCustDriverController;
import Model.Class.User.Driver;
import Model.Class.Vehicle.Car;
import Model.Enum.OrderStatus;
import Model.Enum.StatusAcc;
import Model.Enum.StatusVerification;

public class ManageDriver extends JPanel {
    ArrayList<Driver> drivers = new ArrayList<>();
    JButton block_UnblockButton[];
    JButton verifyButton[];
    JPanel panelSorting;

    final int WIDTH_PANEL;
    final int HEIGHT_PANEL;

    public ManageDriver(TemplateMenu tmp){
        this.drivers = new ManageCustDriverController().getDataDrivers();
        this.WIDTH_PANEL = tmp.getWidthMenuPanels();
        this.HEIGHT_PANEL = tmp.getHeightMenuPanels();

        setSize(WIDTH_PANEL, HEIGHT_PANEL);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Drivers", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setPreferredSize(new Dimension(WIDTH_PANEL, HEIGHT_PANEL - 50));
        panelUtama.setBorder(null);


        // Buat untuk sorting
        panelSorting = new JPanel(new FlowLayout(50, 100, 50));
        JComboBox<String> sortOptions = new JComboBox<>(new String[]{"Sort Drivers by", "Verified", "Unverified"});
        sortOptions.setPreferredSize(new Dimension(150, 50));
        
        JButton sortButton = new JButton("GO");
        sortButton.setPreferredSize(new Dimension(150, 50));
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sortOptions.getSelectedItem().toString().equalsIgnoreCase("Sort Drivers by")) {
                    panelUtama.setViewportView(showDataDriver(drivers));
                }else if(sortOptions.getSelectedItem().toString().equalsIgnoreCase("Verified")){
                    panelUtama.setViewportView(showDataDriver(getDataSortByVerified()));
                }else{
                    panelUtama.setViewportView(showDataDriver(getDataSortByUnverified()));
                }
                panelUtama.revalidate();
                panelUtama.repaint();
            }
        });
        
        
        panelSorting.add(sortOptions);
        panelSorting.add(sortButton);
        
        
        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(showDataDriver(drivers));

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
    }


    private JPanel showDataDriver(ArrayList<Driver> drivers_Sorted){
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(drivers_Sorted.size() + 1, 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        containerPanel.add(panelSorting);

        JPanel panelDrivers[] = new JPanel[drivers_Sorted.size()];

        block_UnblockButton = new JButton[drivers_Sorted.size()];
        verifyButton = new JButton[drivers_Sorted.size()];
        for (int i = 0; i < drivers_Sorted.size(); i++) {
            panelDrivers[i] = new JPanel(new GridLayout(5,1));
            panelDrivers[i].setPreferredSize(new Dimension(WIDTH_PANEL - 30, 150));
            panelDrivers[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelDrivers[i].add(new JLabel("Nama: " + drivers_Sorted.get(i).getName()));
            panelDrivers[i].add(new JLabel("Email: " + drivers_Sorted.get(i).getEmail()));
            panelDrivers[i].add(new JLabel("Tanggal Bergabung: " + drivers_Sorted.get(i).getCreatedAccAt()));

            int jmlhTransaksi = 0;
            for (int j = 0; j < drivers_Sorted.get(i).getOrder().size(); j++) {
                if (drivers_Sorted.get(j).getOrder().get(j).getOrder_status() == OrderStatus.COMPLETE) {
                    jmlhTransaksi++;
                }
            }
            
            panelDrivers[i].add(new JLabel("Jumlah Order yang diambil: " + jmlhTransaksi));

            final int index = i;

            JPanel buttonPanel = new JPanel(new GridLayout(1,2, 10,10));
            block_UnblockButton[i] = new JButton(String.valueOf(drivers_Sorted.get(i).getStatusAcc()));
            
            block_UnblockButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result = new ManageCustDriverController().updateStatusAccUser(drivers_Sorted.get(index).getID_Driver(), drivers_Sorted.get(index).getStatusAcc());

                    if (result) {
                        if(drivers_Sorted.get(index).getStatusAcc() == StatusAcc.BLOCKED){
                            drivers_Sorted.get(index).setStatusAcc(StatusAcc.UNBLOCKED);
                        }else{
                            drivers_Sorted.get(index).setStatusAcc(StatusAcc.BLOCKED);
                        }
                        block_UnblockButton[index].setText(String.valueOf(drivers.get(index).getStatusAcc()));
                    }
                }
            });

            verifyButton[i] = new JButton(String.valueOf(drivers_Sorted.get(i).getVerificationStatus()));
            if(drivers_Sorted.get(i).getVerificationStatus().toString().equalsIgnoreCase("Verified")){
                verifyButton[i].setEnabled(false);
            }else{
                verifyButton[i].addActionListener(e -> showDetailDriver(drivers_Sorted.get(index), verifyButton[index]));
            }

            buttonPanel.add(block_UnblockButton[i]);
            buttonPanel.add(verifyButton[i]);
            panelDrivers[i].add(buttonPanel);

            containerPanel.add(panelDrivers[i]);
        }

        return containerPanel;
    }

    private void showDetailDriver(Driver driver, JButton verifyButton){
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

    private ArrayList<Driver> getDataSortByVerified(){
        ArrayList<Driver> dataSorted = new ArrayList<Driver>();
        for (Driver driver : drivers) {
            if (driver.getVerificationStatus() == StatusVerification.VERIFIED) {
                dataSorted.add(driver);
            }
        }

        return dataSorted;
    }
    private ArrayList<Driver> getDataSortByUnverified(){
        ArrayList<Driver> dataSorted = new ArrayList<Driver>();
        for (Driver driver : drivers) {
            if (driver.getVerificationStatus() == StatusVerification.UNVERIFIED) {
                dataSorted.add(driver);
            }
        }

        return dataSorted;
    }
}
