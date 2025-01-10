import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GrabApp {
    public static List<Order> orders = new ArrayList<>(); // Shared order list

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerFrame();
            new DriverFrame();
        });
    }
}

// Customer JFrame
class CustomerFrame extends JFrame {
    private JTextField customerNameField;
    private JComboBox<String> orderTypeComboBox;
    private JTextArea statusArea;

    public CustomerFrame() {
        setTitle("Customer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(20, 20, 150, 20);
        add(nameLabel);

        customerNameField = new JTextField();
        customerNameField.setBounds(150, 20, 200, 20);
        add(customerNameField);

        JLabel orderTypeLabel = new JLabel("Order Type:");
        orderTypeLabel.setBounds(20, 60, 150, 20);
        add(orderTypeLabel);

        orderTypeComboBox = new JComboBox<>(new String[]{"Grabbike", "Grabcar"});
        orderTypeComboBox.setBounds(150, 60, 200, 20);
        add(orderTypeComboBox);

        JButton orderButton = new JButton("Order");
        orderButton.setBounds(150, 100, 100, 30);
        orderButton.addActionListener(this::placeOrder);
        add(orderButton);

        statusArea = new JTextArea();
        statusArea.setBounds(20, 150, 350, 100);
        statusArea.setEditable(false);
        add(statusArea);

        setVisible(true);
    }

    private void placeOrder(ActionEvent e) {
        String customerName = customerNameField.getText();
        String orderType = (String) orderTypeComboBox.getSelectedItem();

        if (customerName.isEmpty()) {
            statusArea.setText("Please enter your name.");
            return;
        }

        Order order = new Order(customerName, orderType);
        synchronized (GrabApp.orders) {
            GrabApp.orders.add(order);
        }

        statusArea.setText("Order placed. Waiting for driver...\n");

        // Monitor order status
        new Thread(() -> {
            while (!order.isTaken()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            SwingUtilities.invokeLater(() -> {
                statusArea.append("Driver " + order.getDriverName() + " has accepted your order.");
            });
        }).start();
    }
}

// Driver JFrame
class DriverFrame extends JFrame {
    private JTextArea availableOrdersArea;

    public DriverFrame() {
        setTitle("Driver");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel ordersLabel = new JLabel("Available Orders:");
        ordersLabel.setBounds(20, 20, 150, 20);
        add(ordersLabel);

        availableOrdersArea = new JTextArea();
        availableOrdersArea.setBounds(20, 50, 350, 150);
        availableOrdersArea.setEditable(false);
        add(availableOrdersArea);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(20, 210, 100, 30);
        refreshButton.addActionListener(e -> updateOrderList());
        add(refreshButton);

        JButton acceptButton = new JButton("Accept Order");
        acceptButton.setBounds(150, 210, 150, 30);
        acceptButton.addActionListener(this::acceptOrder);
        add(acceptButton);

        setVisible(true);

        // Auto-refresh orders every 3 seconds
        new Timer(3000, e -> updateOrderList()).start();
    }

    private void updateOrderList() {
        String sb = "";
        synchronized (GrabApp.orders) {
            for (int i = 0; i < GrabApp.orders.size(); i++) {
                Order order = GrabApp.orders.get(i);
                if (!order.isTaken()) {
                    sb += i + ". " + order.getCustomerName() +
                            " - " + order.getOrderType() + "\n";
                }
            }
        }
        availableOrdersArea.setText(sb);
    }

    private void acceptOrder(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Enter order number to accept:");
        if (input == null || input.isEmpty()) return;

        try {
            int orderIndex = Integer.parseInt(input);
            synchronized (GrabApp.orders) {
                if (orderIndex >= 0 && orderIndex < GrabApp.orders.size()) {
                    Order order = GrabApp.orders.get(orderIndex);
                    if (!order.isTaken()) {
                        String driverName = JOptionPane.showInputDialog(this, "Enter your name:");
                        if (driverName != null && !driverName.isEmpty()) {
                            order.assignDriver(driverName);
                            updateOrderList();
                            JOptionPane.showMessageDialog(this, "Order accepted!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Order already taken.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid order number.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }
}
