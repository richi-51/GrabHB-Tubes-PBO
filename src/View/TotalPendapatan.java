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

import Controller.PendapatanController;
import Model.Class.Order.Order;

public class TotalPendapatan extends JPanel {
    ArrayList<Order> orders = new ArrayList<>();
    JButton block_UnblockButton[];
    JButton verifyButton[];
    JPanel panelSorting;

    final int WIDTH_PANEL;
    final int HEIGHT_PANEL;

    public TotalPendapatan(TemplateMenu tmp, boolean isDriver){
        this.WIDTH_PANEL = tmp.getWidthMenuPanels();
        this.HEIGHT_PANEL = tmp.getHeightMenuPanels();

        setSize(WIDTH_PANEL, (int)(HEIGHT_PANEL/3));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("PENDAPATAN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setPreferredSize(new Dimension(WIDTH_PANEL, (int)(HEIGHT_PANEL/3)));
        panelUtama.setBorder(null);


        // Buat untuk sorting
        panelSorting = new JPanel(new FlowLayout(25, 20, 100));
        
        JComboBox<String> daysSort = new JComboBox<>(new PendapatanController().getDayOfOrder(isDriver));
        daysSort.setPreferredSize(new Dimension(125, 50));

        JComboBox<String> monthSort = new JComboBox<>(new PendapatanController().getMonthsOfOrder(isDriver));
        monthSort.setPreferredSize(new Dimension(125, 50));
        
        JComboBox<String> yearSort = new JComboBox<>(new PendapatanController().getYearsOfOrder(isDriver));
        yearSort.setPreferredSize(new Dimension(125, 50));

        
        JButton sortButton = new JButton("GO");
        sortButton.setPreferredSize(new Dimension(125, 50));
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelUtama.setViewportView(showRevenue(daysSort.getSelectedItem().toString(), monthSort.getSelectedItem().toString(), yearSort.getSelectedItem().toString(), isDriver));
                panelUtama.revalidate();
                panelUtama.repaint();
            }
        });
        
        
        panelSorting.add(daysSort);
        panelSorting.add(monthSort);
        panelSorting.add(yearSort);
        panelSorting.add(sortButton);
        
        
        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(showRevenue("Tanggal: ", "Bulan ke-", "Tahun: ", isDriver));

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
    }


    private JPanel showRevenue(String day, String month, String year, boolean isDriver){
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(2, 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 10));
        
        containerPanel.add(panelSorting);

        JLabel text = new JLabel("Total Pendapatan Anda: " + new PendapatanController().getRevenueAdmin(day, month, year), JLabel.CENTER);
        if (isDriver) {
            text.setText("Total Pendapatan Anda: " + new PendapatanController().getRevenueDriver(day, month, year));
        }
        text.setFont(new Font("Arial", Font.BOLD, 26));
        text.setForeground(Color. RED);

        containerPanel.add(text);

        return containerPanel;
    }
}
