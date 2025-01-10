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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.HistoryOrderController;
import Model.Class.Order.GrabBike;
import Model.Class.Order.GrabCar;
import Model.Class.Order.Order;

public class HistoryOrder extends JPanel{
    private JPanel panelSorting;

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

           
        }

        return containerPanel;
    }
}