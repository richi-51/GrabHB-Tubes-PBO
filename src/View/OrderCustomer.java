package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Controller.ManageCustDriverController;
import Controller.OrderCustomerController;
import Model.Class.Location.Lokasi;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Enum.ServiceType;


public class OrderCustomer extends JPanel {
    private ArrayList<Driver> drivers = new ArrayList<>();
    private JButton block_UnblockButton[];
    private JButton verifyButton[];
    private JPanel panelSorting;
    private JComboBox<String> custVouchers, pickUpLoc, destinationLoc, typeOrder, paymentMethod;
    JLabel priceLabel;

    private final int WIDTH_PANEL;
    private final int HEIGHT_PANEL;

    private int price = 0;
    private int chance = 1;

    public OrderCustomer(TemplateMenu tmp, String titleMenu, String orderType[], ServiceType serviceType) {
        this.drivers = new ManageCustDriverController().getDataDrivers();
        this.WIDTH_PANEL = tmp.getWidthMenuPanels();
        this.HEIGHT_PANEL = tmp.getHeightMenuPanels();

        setSize(WIDTH_PANEL, HEIGHT_PANEL);
        setLayout(new BorderLayout());

        JLabel title = new JLabel(titleMenu, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setPreferredSize(new Dimension(WIDTH_PANEL, HEIGHT_PANEL - 50));
        panelUtama.setBorder(null);

        // Buat untuk sorting
        // panelSorting = new JPanel(new FlowLayout(50, 100, 50));
        // JComboBox<String> sortOptions = new JComboBox<>(new String[]{"Sort Drivers
        // by", "Verified", "Unverified"});
        // sortOptions.setPreferredSize(new Dimension(150, 50));

        // JButton sortButton = new JButton("GO");
        // sortButton.setPreferredSize(new Dimension(150, 50));
        // sortButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // if (sortOptions.getSelectedItem().toString().equalsIgnoreCase("Sort Drivers
        // by")) {
        // panelUtama.setViewportView(showDataDriver(drivers));
        // }else
        // if(sortOptions.getSelectedItem().toString().equalsIgnoreCase("Verified")){
        // panelUtama.setViewportView(showDataDriver(getDataSortByVerified()));
        // }else{
        // panelUtama.setViewportView(showDataDriver(getDataSortByUnverified()));
        // }
        // panelUtama.revalidate();
        // panelUtama.repaint();
        // }
        // });

        // panelSorting.add(sortOptions);
        // panelSorting.add(sortButton);

        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(showFormOrder(orderType, serviceType));

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);

        // tambahin button order dan selesaikan fungsinya
        JButton orderNowButton = new JButton("ORDER NOW!");
        orderNowButton.addActionListener(e -> {
            final String voucher = custVouchers.getSelectedItem().toString().split(": Potongan ")[0];
            final Lokasi pu = new OrderCustomerController().getLocation(pickUpLoc.getSelectedItem().toString().split(", ")[0]);
            final Lokasi ds = new OrderCustomerController().getLocation(destinationLoc.getSelectedItem().toString().split(", ")[0]);

            new OrderCustomerController().insertNewOrder(voucher, pu, ds, serviceType, paymentMethod.getSelectedItem().toString(), price, typeOrder.getSelectedItem().toString());
            
            
            System.out.println("Order terbaru: " + ((Customer) (SingletonManger.getInstance().getLoggedInUser())).getOrder().get(((Customer) (SingletonManger.getInstance().getLoggedInUser())).getOrder().size()-1).getOrder_date());
        });

        add(orderNowButton, BorderLayout.SOUTH);



    }

    private JPanel showFormOrder(String orderType[], ServiceType serviceType) {
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(6, 2, 10, 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // containerPanel.add(panelSorting);

        double saldoOvo = ((Customer) (SingletonManger.getInstance().getLoggedInUser())).getOvoE_money().getSaldo();

        containerPanel.add(new JLabel("Pick Up Location: "));
        pickUpLoc = new JComboBox<>(new OrderCustomerController().getAllLocation());
        containerPanel.add(pickUpLoc);

        containerPanel.add(new JLabel("Destination Location: "));
        destinationLoc = new JComboBox<>(new OrderCustomerController().getAllLocation());

        destinationLoc.addActionListener(e -> {
            if (pickUpLoc.getSelectedItem().toString().split(", ")[0]
                    .equalsIgnoreCase(destinationLoc.getSelectedItem().toString().split(", ")[0])) {
                JOptionPane.showMessageDialog(null, "Maaf Lokasi Destinasi Anda sama", "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                destinationLoc.setForeground(Color.red);
            } else {
                destinationLoc.setForeground(Color.black);
            }
        });

        containerPanel.add(destinationLoc);

        containerPanel.add(new JLabel("Order Type:"));
        typeOrder = new JComboBox<>(orderType);

        typeOrder.addActionListener(e -> {
            price = new OrderCustomerController().getPrice(typeOrder.getSelectedItem().toString(), serviceType,
                    new OrderCustomerController().getLocation(pickUpLoc.getSelectedItem().toString().split(", ")[0]),
                    new OrderCustomerController()
                            .getLocation(destinationLoc.getSelectedItem().toString().split(", ")[0]),
                    0);

            priceLabel.setText("Price: " + price);
            priceLabel.revalidate();
            priceLabel.repaint();

            custVouchers.setModel(new DefaultComboBoxModel<>(new OrderCustomerController()
            .getAllVoucher(SingletonManger.getInstance().getLoggedInUser().getIdUser(), serviceType, price)));
            
        });

        containerPanel.add(typeOrder);

        containerPanel.add(new JLabel("Payment method:"));
        paymentMethod = new JComboBox<>(new String[] { "", "Cash", "Ovo" });
        paymentMethod.addActionListener(e -> {
            if (paymentMethod.getSelectedItem().toString().equalsIgnoreCase("Ovo")) {
                if (saldoOvo < price) {
                    JOptionPane.showMessageDialog(null, "Maaf saldo OVO Anda tidak cukup", "Insufficient Balance",
                            JOptionPane.WARNING_MESSAGE);
                    paymentMethod.setSelectedItem("Cash");
                }
            }
        });
        containerPanel.add(paymentMethod);

        containerPanel.add(new JLabel("Voucher:"));
        custVouchers = new JComboBox<>(new OrderCustomerController()
                .getAllVoucher(SingletonManger.getInstance().getLoggedInUser().getIdUser(), serviceType, price));

        custVouchers.addActionListener(e -> {
            if (custVouchers.getSelectedItem().toString().equalsIgnoreCase("I have another voucher code")
                    || custVouchers.getSelectedItem().toString().equalsIgnoreCase("I have voucher code")) {
                addNewVoucherFromCustomer();
            } else if (custVouchers.getSelectedItem().toString().equalsIgnoreCase("I don't want to use my voucher")) {
                price = new OrderCustomerController().getPrice(typeOrder.getSelectedItem().toString(), serviceType,
                        new OrderCustomerController()
                                .getLocation(pickUpLoc.getSelectedItem().toString().split(", ")[0]),
                        new OrderCustomerController()
                                .getLocation(destinationLoc.getSelectedItem().toString().split(", ")[0]),
                        0);

                priceLabel.setText("Price: " + price);
                priceLabel.revalidate();
                priceLabel.repaint();
            } else {
                String st1 = custVouchers.getSelectedItem().toString().split(": Potongan ")[1];

                int disc = Integer.parseInt(st1);

                price = new OrderCustomerController().getPrice(typeOrder.getSelectedItem().toString(), serviceType,
                        new OrderCustomerController()
                                .getLocation(pickUpLoc.getSelectedItem().toString().split(", ")[0]),
                        new OrderCustomerController()
                                .getLocation(destinationLoc.getSelectedItem().toString().split(", ")[0]),
                        disc);

                priceLabel.setText("Price: " + price);
                priceLabel.revalidate();
                priceLabel.repaint();
            }
        });

        containerPanel.add(custVouchers);
        containerPanel.add(new JLabel("Ovo Balance: " + saldoOvo));

        priceLabel = new JLabel("Price: " + price);

        containerPanel.add(priceLabel);
        return containerPanel;
    }

    private void addNewVoucherFromCustomer() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(350, 150);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Add Voucher", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(title, BorderLayout.NORTH);

        JPanel addVoucherForCust = new JPanel(new GridLayout(2, 2, 10, 10));
        addVoucherForCust.add(new JLabel("Kode Voucher: "));
        JTextField kodeVoucherField = new JTextField();
        addVoucherForCust.add(kodeVoucherField);

        // JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));

        JButton validateVoucherButton = new JButton("VALIDATE THIS CODE");
        validateVoucherButton.addActionListener(e -> {
            if (!new OrderCustomerController().validateVoucherCode(kodeVoucherField.getText(),
                    SingletonManger.getInstance().getLoggedInUser().getIdUser()) && chance < 3) {
                JOptionPane.showMessageDialog(null, "Maaf Kode Voucher tidak ada :(", "Information Message",
                        JOptionPane.ERROR_MESSAGE);
                ++chance;
            } else {
                JOptionPane.showMessageDialog(null, "Yukkk Istirahat dulu, dicoba lagi nanti :)", "Information Message",
                        JOptionPane.INFORMATION_MESSAGE);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        chance = 1;

                        // Buat tanda aja, nanti dihapus karena customer sebaiknya tidak tahu
                        JOptionPane.showMessageDialog(null, "Kesempatan sudah direset, silakan coba lagi!",
                                "Information Message", JOptionPane.INFORMATION_MESSAGE);
                        timer.cancel();
                    }
                }, 5 * 60 * 1000);

            }
        });
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e -> frame.dispose());

        addVoucherForCust.add(cancelButton);
        addVoucherForCust.add(validateVoucherButton);

        // addVoucherForCust.add(panelButton);
        frame.add(addVoucherForCust);

        frame.setVisible(true);
    }
}
