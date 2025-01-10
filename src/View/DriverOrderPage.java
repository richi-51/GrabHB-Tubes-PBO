package View;

import Controller.CompleteOrder;
import Controller.ConfirmOrder;
import Controller.LoadOrderList;
import Model.Class.Order.Order;
import Model.Class.Singleton.SingletonManger;
import Model.Class.User.Driver;
import Model.Enum.OrderStatus;
import Model.Class.Db.DatabaseHandler;
import Model.Enum.DriverStatus;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DriverOrderPage extends JPanel {

    private final int FRAME_WIDTH;
    private final int FRAME_HEIGHT;

    private JList<Order> orderList;
    private JTextArea orderDetailsArea;
    private JButton confirmButton, completeButton;
    private List<Order> orders;
    private double saldo = ((Driver)(SingletonManger.getInstance().getLoggedInUser())).getOvoDriver().getSaldo();

    public DriverOrderPage(TemplateMenu tmp) {

        this.FRAME_WIDTH = tmp.getWidthMenuPanels();
        this.FRAME_HEIGHT = tmp.getHeightMenuPanels();

        Driver loggedInUser = (Driver) SingletonManger.getInstance().getLoggedInUser();

        setUpFrame();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, FRAME_HEIGHT));

        LoadOrderList loadOrderList = new LoadOrderList(this);
        orders = loadOrderList.loadOrders();
        System.out.println("Jumlah pesanan: " + orders.size());

        Order[] orderArray = orders.toArray(new Order[0]);
        orderList = new JList<>(orderArray);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderList.setCellRenderer(new OrderListCellRenderer());
        orderList.addListSelectionListener(e -> displayOrderDetails(orderList.getSelectedValue()));
        JScrollPane listScrollPane = new JScrollPane(orderList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        JLabel saldoLabel = new JLabel("Saldo Ovo: " + saldo);
        leftPanel.add(saldoLabel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        orderDetailsArea = new JTextArea();
        orderDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(orderDetailsArea);
        rightPanel.add(detailsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        
        confirmButton = new JButton("Confirm Order");
        confirmButton.addActionListener(e -> {
            try {
                Order selectedOrder = orderList.getSelectedValue();
                if (selectedOrder != null) {
                    ConfirmOrder order = new ConfirmOrder();
                    double result = order.confirmOrder(selectedOrder.getID_order(), loggedInUser, saldo);

                    if (result > 0) {
                        confirmOrder(selectedOrder, loggedInUser);
                        saldo = result;
                    }
                    saldoLabel.setText("Saldo Ovo: " + saldo);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an order to confirm.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        completeButton = new JButton("Complete Order");
        completeButton.addActionListener(e ->{
            try {
                Order selectedOrder = orderList.getSelectedValue();
                if (selectedOrder != null) {
                    CompleteOrder order = new CompleteOrder();
                    order.completeOrder(selectedOrder.getID_order(), loggedInUser, null);
                    completeOrder(selectedOrder);
                    refreshOrderList();
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an order to confirm.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(completeButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void setUpFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int startX = screenSize.width / 2 - (FRAME_WIDTH / 2);
        int startY = screenSize.height / 2 - (FRAME_HEIGHT / 2);

        setBounds(startX, startY, FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new BorderLayout());
    }

    private void displayOrderDetails(Order order) {
        if (order != null) {
            orderDetailsArea.setText(
                "Service Type: " + order.getServiceType() + "\n" +
                "Price: Rp" + order.getPrice() + "\n" +
                "Order Date: " + order.getOrder_date() + "\n" +
                "Order Status: " + order.getOrder_status()
            );
        } else {
            orderDetailsArea.setText("");
        }
    }

    private void refreshOrderList() {
        LoadOrderList loadOrderList = new LoadOrderList(this);
        orders = loadOrderList.loadOrders();
        Order[] orderArray = orders.toArray(new Order[0]);
        orderList.setListData(orderArray);
        orderDetailsArea.setText("");
    }

    private void confirmOrder(Order selectedOrder, Driver loggedInUser) throws SQLException {
        if (selectedOrder != null) {
            selectedOrder.setID_driver(loggedInUser.getID_Driver());
            selectedOrder.setOrder_status(OrderStatus.ON_PROCESS);            
            JOptionPane.showMessageDialog(this, "Order confirmed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void completeOrder(Order selectedOrder) throws SQLException {
        if (selectedOrder != null) {
            selectedOrder.setOrder_status(OrderStatus.COMPLETE);            
            JOptionPane.showMessageDialog(this, "Order completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static class OrderListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Order) {
                Order order = (Order) value;
                String displayText = String.format("%s - Rp%.2f", order.getServiceType(), order.getPrice());
                label.setText(displayText);
            }
            return label;
        }
    }

}
