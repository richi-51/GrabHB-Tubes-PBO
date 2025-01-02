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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.ManageCustDriverController;
import Model.Class.User.Customer;
import Model.Enum.OrderStatus;
import Model.Enum.StatusAcc;

public class ManageCustomer extends JPanel {
    ArrayList<Customer> customers = new ArrayList<>();
    JButton block_UnblockButton[];

    public ManageCustomer(TemplateMenu tmp){
        this.customers = new ManageCustDriverController().getDataCustomers();

        setPreferredSize(new Dimension(tmp.getWidthMenuPanels(), tmp.getHeightMenuPanels()-100));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Customers", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setBorder(null);
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(customers.size(), 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelCustomers[] = new JPanel[customers.size()];

        block_UnblockButton = new JButton[customers.size()];

        for (int i = 0; i < customers.size(); i++) {
            panelCustomers[i] = new JPanel(new GridLayout(5,1));
            panelCustomers[i].setPreferredSize(new Dimension(tmp.getWidthMenuPanels() - 30, 150));
            panelCustomers[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelCustomers[i].add(new JLabel("Nama: " + customers.get(i).getName()));
            panelCustomers[i].add(new JLabel("Email: " + customers.get(i).getEmail()));
            panelCustomers[i].add(new JLabel("Tanggal Bergabung: " + customers.get(i).getCreatedAccAt()));

            int jmlhTransaksi = 0;
            for (int j = 0; j < customers.get(i).getOrder().size(); j++) {
                if (customers.get(j).getOrder().get(j).getOrder_status() == OrderStatus.COMPLETE) {
                    jmlhTransaksi++;
                }
            }
            
            panelCustomers[i].add(new JLabel("Jumlah Transaksi: " + jmlhTransaksi));

            final int index = i;
            block_UnblockButton[i] = new JButton(String.valueOf(customers.get(i).getStatusAcc()));
            
            block_UnblockButton[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result = new ManageCustDriverController().updateStatusAccUser(customers.get(index).getID_Customer(), customers.get(index).getStatusAcc());

                    if (result) {
                        if(customers.get(index).getStatusAcc() == StatusAcc.BLOCKED){
                            customers.get(index).setStatusAcc(StatusAcc.UNBLOCKED);
                        }else{
                            customers.get(index).setStatusAcc(StatusAcc.BLOCKED);
                        }
                        block_UnblockButton[index].setText(String.valueOf(customers.get(index).getStatusAcc()));
                    }else{
                        JOptionPane.showMessageDialog(null, "Perubahan gagal disimpan!", "Error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            panelCustomers[i].add(block_UnblockButton[i]);

            containerPanel.add(panelCustomers[i]);
        }

        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(containerPanel);

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
    }
}
