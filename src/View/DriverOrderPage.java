package View;

import Controller.DriverOrderController;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Class.Order.Order;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DriverOrderPage implements DriverOrderController.OrderView {

    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;

    private DriverOrderController controller;
    private JList<Order> orderList;
    private JTextArea orderDetailsArea;

    public DriverOrderPage() {
        controller = new DriverOrderController(this);
        showOrderPage();
    }

    private void showOrderPage() {
        Driver loggedInUser = (Driver)(SingletonManger.getInstance().getLoggedInUser());
        
        JFrame frame = createFrame();
        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, FRAME_HEIGHT));

        orderList = new JList<>();
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.setCellRenderer(new OrderListCellRenderer());
        orderList.addListSelectionListener(e -> displayOrderDetails(orderList.getSelectedValue()));
        JScrollPane listScrollPane = new JScrollPane(orderList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        orderDetailsArea = new JTextArea();
        orderDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(orderDetailsArea);
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.addActionListener(e -> {
            try {
                double driverSaldo = loggedInUser.getOvoE_money().getSaldo();
                double driverCoins = loggedInUser.getOvoE_money().getCoins();

                Order currentOrder = controller.getCurrentOrder(loggedInUser.getID_Driver());
                if (currentOrder == null) {
                    Order selectedOrder = orderList.getSelectedValue();
                    if (selectedOrder != null) {
                        controller.confirmOrder(selectedOrder.getID_order());
                        selectedOrder.setID_driver(loggedInUser.getID_Driver());
                        selectedOrder.setOrder_status(OrderStatus.ON_PROCESS);
                        frame.dispose();
                        new DriverPage();

                        // if (displayConfirm("Saldo: " + driverSaldo + "\nKoin: " + driverCoins + "\n\nOrder Dikonfirmasi??") == JOptionPane.YES_OPTION) {
                        //     controller.confirmOrder(selectedOrder.getID_order());
                        //     selectedOrder.setID_driver(loggedInUser.getID_Driver());
                        //     selectedOrder.setOrder_status(OrderStatus.ON_PROCESS);
                        //     frame.dispose();
                        //     new DriverPage();
                        // }else{
                        //     displayError("Order gagal di ambil");
                        //     showOrderPage();
                        // }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "There's Ongoing Order", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                displayError("Error loading current order: " + ex.getMessage());
            }

        });
        buttonPanel.add(confirmButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            new DriverPage(); // balik lagi jadi Welcome Driver
        });
        buttonPanel.add(backButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        controller.loadOrders();
    }

    private JFrame createFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int start_x = screenWidth / 2 - (FRAME_WIDTH / 2);
        int start_y = screenHeight / 2 - (FRAME_HEIGHT / 2);

        JFrame frame = new JFrame("View Orders");
        frame.setBounds(start_x, start_y, FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    private void displayOrderDetails(Order order) {
        if (order != null) {
            String serviceType = (order.getServiceType() == ServiceType.GRABBIKE)?"GrabBike":"GrabCar";
            orderDetailsArea.setText(
                "Service Type: " + serviceType  + "\n"
                + "Price: Rp" + order.getPrice() + "\n"
                + "Order Date: " + order.getOrder_date() + "\n"
                + "Order Status: " + order.getOrder_status()
            );
        } else {
            orderDetailsArea.setText("");
        }
    }

    @Override
    public void displayOrders(List<Order> orders) {
        DefaultListModel<Order> listModel = new DefaultListModel<>();
        for (Order order : orders) {
            listModel.addElement(order);
        }
        orderList.setModel(listModel);
    }

    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public void displayIncomes(String incomes) {
        
    }

    @Override
    public int displayConfirm(String question){
        return JOptionPane.showConfirmDialog(null, question, "Confirm", JOptionPane.YES_NO_OPTION);
    }

    private static class OrderListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Order) {
                Order order = (Order) value;
                String displayText = String.format("%s - $%.2f", order.getServiceType(), order.getPrice());
                label.setText(displayText);
            }
            return label;
        }
    }
}
