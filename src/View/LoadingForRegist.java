package View;
import javax.swing.*;

import java.awt.*;

public class LoadingForRegist extends JFrame {

    private JProgressBar progressBar;

    public LoadingForRegist(String message, String title) {
        setTitle("Saving Data User");
        setSize(200, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Saving your data ...", JLabel.CENTER);
        add(label, BorderLayout.NORTH);

        // Buat progress bar
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); //buat munculin persentase
        add(progressBar, BorderLayout.CENTER);
        setVisible(true);

        startLoading(message, title);
    }

    private void startLoading(String message, String title) {
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(100);
                    publish(i);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
            }

            @Override
            protected void done() {
                int okStatus = JOptionPane.showConfirmDialog(
                        LoadingForRegist.this,
                        message,
                        title,
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (okStatus == JOptionPane.OK_OPTION) {
                    LoadingForRegist.this.dispose();
                }

            }
        };
        worker.execute();
    }
}
