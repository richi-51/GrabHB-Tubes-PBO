import javax.swing.*;
import java.awt.event.ActionEvent;

public class tes extends JFrame {
    private JButton startButton;
    private JTextArea resultArea;

    public tes() {
        setTitle("SwingWorker Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        startButton = new JButton("Start Task");
        startButton.setBounds(20, 20, 120, 30);
        add(startButton);

        resultArea = new JTextArea();
        resultArea.setBounds(20, 70, 350, 180);
        resultArea.setEditable(false);
        add(resultArea);

        startButton.addActionListener(this::startTask);
    }

    private void startTask(ActionEvent e) {
        resultArea.setText("Task started...\n");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate a long-running task
                Thread.sleep(3000);
                publish("Task in progress...");
                Thread.sleep(2000);
                publish("Task completed!");
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Update GUI with intermediate results
                for (String message : chunks) {
                    resultArea.append(message + "\n");
                }
            }

            @Override
            protected void done() {
                resultArea.append("Done!\n");
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            tes frame = new tes();
            frame.setVisible(true);
        });
    }
}
