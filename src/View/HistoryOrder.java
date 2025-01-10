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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Controller.HistoryOrderController;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Order;
import Model.Class.User.Driver;
import Model.Class.Vehicle.Car;
<<<<<<< HEAD
=======
import Model.Enum.OrderStatus;
>>>>>>> 1f3a179cdf68c3b63e5dbafc0d736bdb32f8bb59

public class HistoryOrder extends JPanel{
    private JPanel panelSorting;
    private double rating;

    private final int WIDTH_PANEL;
    private final int HEIGHT_PANEL;

    public HistoryOrder(TemplateMenu tmp, boolean isDriver, boolean isAdmin, boolean isCustomer){
        this.WIDTH_PANEL = tmp.getWidthMenuPanels();
        this.HEIGHT_PANEL = tmp.getHeightMenuPanels();

        setSize(WIDTH_PANEL, (int)(HEIGHT_PANEL/3));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("TRANSACTION", JLabel.CENTER);
        if (isCustomer || isDriver) {
            title.setText("HISTORY ORDERS");
        }
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setPreferredSize(new Dimension(WIDTH_PANEL, (int)(HEIGHT_PANEL/3)));
        panelUtama.setBorder(null);


        // Buat untuk sorting
        panelSorting = new JPanel(new FlowLayout(25, 20, 100));
        
        JComboBox<String> daysSort = new JComboBox<>(new HistoryOrderController().getDayOfOrder(isDriver, isCustomer));
        daysSort.setPreferredSize(new Dimension(125, 50));

        JComboBox<String> monthSort = new JComboBox<>(new HistoryOrderController().getMonthsOfOrder(isDriver, isCustomer));
        monthSort.setPreferredSize(new Dimension(125, 50));
        
        JComboBox<String> yearSort = new JComboBox<>(new HistoryOrderController().getYearsOfOrder(isDriver, isCustomer));
        yearSort.setPreferredSize(new Dimension(125, 50));

        
        JButton sortButton = new JButton("GO");
        sortButton.setPreferredSize(new Dimension(125, 50));
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelUtama.setViewportView(showHistoryOrder(daysSort.getSelectedItem().toString(), monthSort.getSelectedItem().toString(), yearSort.getSelectedItem().toString(), isAdmin, isDriver));
                panelUtama.revalidate();
                panelUtama.repaint();
            }
        });
        
        
        panelSorting.add(daysSort);
        panelSorting.add(monthSort);
        panelSorting.add(yearSort);
        panelSorting.add(sortButton);
        
        
        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(showHistoryOrder("Tanggal: ", "Bulan ke-", "Tahun: ", isAdmin, isDriver));

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);


        // Tambahin button rating driver dan report this order, juga buat frame barunya
        if (isCustomer) {
            
        }
    }


    private JPanel showHistoryOrder(String day, String month, String year, boolean isAdmin, boolean isDriver){
        ArrayList<Order> orderSorted;
        if (isAdmin) {
            orderSorted = new HistoryOrderController().getHistoryOrderAdmin(day, month, year);
        }else if(isDriver){
            orderSorted = new HistoryOrderController().getHistoryOrderDriverCust(day, month, year, false);
        }else{
            orderSorted = new HistoryOrderController().getHistoryOrderDriverCust(day, month, year, true);
        }

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(orderSorted.size() + 1, 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        containerPanel.add(panelSorting);

        JPanel panelOrders[] = new JPanel[orderSorted.size()];
        JPanel panelButton[] = new JPanel[orderSorted.size()];
        JButton ratingButton []= new JButton[orderSorted.size()];
        JButton reportButton[] = new JButton[orderSorted.size()];

        for (int i = 0; i < orderSorted.size(); i++) {
            panelOrders[i] = new JPanel(new GridLayout(12,1));
            panelOrders[i].setPreferredSize(new Dimension(WIDTH_PANEL - 30, 200));
            panelOrders[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelOrders[i].add(new JLabel("ID_Order: " + orderSorted.get(i).getID_order()));
            panelOrders[i].add(new JLabel("Order Date: " + orderSorted.get(i).getOrder_date()));
            panelOrders[i].add(new JLabel("Pick-Up Location: " + orderSorted.get(i).getPickUpLoc().getAlamat() + ", " + orderSorted.get(i).getPickUpLoc().getKota()));
            panelOrders[i].add(new JLabel("Destination Location: " + orderSorted.get(i).getDestination().getAlamat() + ", " + orderSorted.get(i).getDestination().getKota()));
            panelOrders[i].add(new JLabel("Service Type: " + orderSorted.get(i).getServiceType().toString()));

            if(orderSorted.get(i) instanceof GrabBike){
                GrabBike gb = (GrabBike)orderSorted.get(i);
                panelOrders[i].add(new JLabel("Order Type: " + gb.getOrderType().toString()));
            }else{
                GrabCar gc = (GrabCar)orderSorted.get(i);
                panelOrders[i].add(new JLabel("Order Type: " + gc.getOrderType().toString()));
            }
            panelOrders[i].add(new JLabel("Order Status: " + orderSorted.get(i).getOrder_status().toString()));
            panelOrders[i].add(new JLabel("Payment Method: " + orderSorted.get(i).getPaymentMethod().toString()));
            panelOrders[i].add(new JLabel("Price: " + orderSorted.get(i).getPrice()));
            panelOrders[i].add(new JLabel("Voucher: " + orderSorted.get(i).getVoucher().getKodeVoucher()));
            panelOrders[i].add(new JLabel("Potongan Voucher: " + orderSorted.get(i).getVoucher().getJumlahPotongan()));

            
<<<<<<< HEAD
            final int index = i;
            panelButton[i] = new JPanel(new GridLayout(1, 2, 15,15));

            ratingButton[i] = new JButton("Rating This Order");
            ratingButton[i].addActionListener(e-> showRatingDriver(new HistoryOrderController().getDetailDriver(orderSorted.get(index).getID_driver()), orderSorted.get(index).getID_order()));

            reportButton[i] = new JButton("Report This Order");
            reportButton[i].addActionListener(e-> showReportOrder(orderSorted.get(index).getID_order()));
            
            panelButton[i].add(ratingButton[i]);
            panelButton[i].add(reportButton[i]);
            
            panelOrders[i].add(panelButton[i]);
=======
            
            
            final int index = i;
            panelButton[i] = new JPanel(new GridLayout(1, 2, 15,15));
            
            if(orderSorted.get(i).getOrder_status() == OrderStatus.COMPLETE){
                ratingButton[i] = new JButton("Rating This Order");
                ratingButton[i].addActionListener(e-> showRatingDriver(new HistoryOrderController().getDetailDriver(orderSorted.get(index).getID_driver()), orderSorted.get(index).getID_order()));
    
                reportButton[i] = new JButton("Report This Order");
                reportButton[i].addActionListener(e-> showReportOrder(orderSorted.get(index).getID_order()));
                
                panelButton[i].add(ratingButton[i]);
                panelButton[i].add(reportButton[i]);
                
                panelOrders[i].add(panelButton[i]);
            }
            
>>>>>>> 1f3a179cdf68c3b63e5dbafc0d736bdb32f8bb59
            containerPanel.add(panelOrders[i]);

        }

        return containerPanel;
    }




    public void showRatingDriver(Driver driver, int id_order){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Details Driver", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(title, BorderLayout.NORTH);

        JPanel dataDriver = new JPanel(new GridLayout(9, 1, 10,10));
        dataDriver.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataDriver.add(new JLabel("Nama: " + driver.getName()));
        dataDriver.add(new JLabel("==============================================================="));
        dataDriver.add(new JLabel("DATA KENDARAAN", JLabel.CENTER));
        dataDriver.add(new JLabel("Nama Kendaraan: " + driver.getVehicle().getVehicleName()));

        String jnsKendaraan = (driver.getVehicle() instanceof Car) ? "Car" : "Motorcycle";
        dataDriver.add(new JLabel("Jenis Kendaraan: " + jnsKendaraan));
        dataDriver.add(new JLabel("Plat Nomor: " + driver.getVehicle().getPlateNumber()));

        JPanel panelWadahRating = new JPanel(new GridLayout(1,2,10,10));
        JLabel ratingLabel = new JLabel("Rating: ");

        JPanel panelSlider = new JPanel(new BorderLayout());
        // Slider dengan rentang 0 hingga 50 (untuk mewakili 0.0 hingga 5.0)
        JSlider slider = new JSlider(0, 50, 25); // Default 2.5
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel label = new JLabel("Berikan Rating: ");
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rating = slider.getValue() / 10.0; // Konversi ke desimal
                label.setText("Berikan Rating: " + rating);
            }
        });

        panelSlider.add(slider, BorderLayout.CENTER);
        panelSlider.add(label, BorderLayout.SOUTH);

        panelWadahRating.add(ratingLabel);
        panelWadahRating.add(panelSlider);
        dataDriver.add(panelWadahRating);


        JPanel panelUlasan = new JPanel(new GridLayout(1,2,10,10));
        panelUlasan.add(new JLabel("Ulasan: "));
        JTextArea ulasanField = new JTextArea(3,5);
        panelUlasan.add(ulasanField);
        dataDriver.add(panelUlasan);


        JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));

        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(e-> {
            frame.dispose();
            new HistoryOrderController().updateRating(id_order, rating, ulasanField.getText());
        });

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(saveButton);


        dataDriver.add(panelButton);
        frame.add(dataDriver);
        
        frame.setVisible(true);
    }


    public void showReportOrder(int id_order){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Report Order", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(title, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new GridLayout(2, 2, 10,10));
        reportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        reportPanel.add(new JLabel("Keluhan: "));
        JTextArea keluhanField = new JTextArea(5,5);
        reportPanel.add(keluhanField);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton saveButton = new JButton("REPORT");
        saveButton.addActionListener(e-> {
            frame.dispose();
            new HistoryOrderController().insertOrderReport(id_order, keluhanField.getText());
        });

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(saveButton);


        frame.add(reportPanel, BorderLayout.CENTER);
        frame.add(panelButton, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }

    

}