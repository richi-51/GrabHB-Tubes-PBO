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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Controller.ManageLaporanController;
import Model.Class.Order.Laporan;
import Model.Enum.StatusLaporan;



public class ManageLaporan extends JPanel {
    private JButton manageButtons[];

    public ManageLaporan(TemplateMenu tmp){
        ArrayList<Laporan> laporans  = new ManageLaporanController().getLaporans();

        setPreferredSize(new Dimension(tmp.getWidthMenuPanels(), tmp.getHeightMenuPanels()-100));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Laporan", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setBorder(null);
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(laporans.size(), 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelLaporan[] = new JPanel[laporans.size()];

        manageButtons = new JButton[laporans.size()];
        

        for (int i = 0; i < laporans.size(); i++) {
            panelLaporan[i] = new JPanel(new GridLayout(5,1));
            panelLaporan[i].setPreferredSize(new Dimension(tmp.getWidthMenuPanels() - 30, 250));
            panelLaporan[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelLaporan[i].add(new JLabel("Isi Keluhan: " + laporans.get(i).getIsiKeluhan()));
            panelLaporan[i].add(new JLabel("Dibuat pada: " + String.valueOf(laporans.get(i).getCreatedAt())));
            panelLaporan[i].add(new JLabel("Status Laporan: " + laporans.get(i).getStatusLaporan().toString()));

            String selesaiPada = laporans.get(i).getFinishAt() != null ? String.valueOf(laporans.get(i).getFinishAt()) : "-";
            panelLaporan[i].add(new JLabel("Selesai pada: " + selesaiPada));
            

            final int index = i;
            manageButtons[i] = new JButton("MANAGE REPORT");
            manageButtons[i].addActionListener(e -> manageLaporanFrame(laporans.get(index)));

            panelLaporan[i].add(manageButtons[i]);

            containerPanel.add(panelLaporan[i]);
        }

        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(containerPanel);

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
    }



    private void manageLaporanFrame(Laporan laporan){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 250);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("MANAGE REPORT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(title, BorderLayout.NORTH);

        JPanel dataLaporan = new JPanel(new GridLayout(2, 2, 10,10));
        dataLaporan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dataLaporan.add(new JLabel("Admin Note: "));
        JTextArea adminNote = new JTextArea(new ManageLaporanController().getAdminNote(laporan.getID_laporan()));
        dataLaporan.add(adminNote);

        dataLaporan.add(new JLabel("Report status: "));
        JComboBox<String> reportStatus = new JComboBox<>(new String[]{"Waiting", "On_Process", "Done"});

        if (laporan.getStatusLaporan().toString().equalsIgnoreCase("Waiting")) {
            reportStatus.setSelectedIndex(0);
        }else if (laporan.getStatusLaporan().toString().equalsIgnoreCase("On_Process")) {
            reportStatus.setSelectedIndex(1);
        }else{
            reportStatus.setSelectedIndex(2);
        }

        dataLaporan.add(reportStatus);

        
        
        JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));
        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = new ManageLaporanController().saveManageLaporan(laporan.getID_laporan(), adminNote.getText(), reportStatus.getSelectedItem().toString());

                System.out.println(laporan.getStatusLaporan().toString());
                if (result) {
                    if (reportStatus.getSelectedItem().toString().equalsIgnoreCase("Waiting")) {
                        laporan.setStatusLaporan(StatusLaporan.WAITING);
                    }else if (reportStatus.getSelectedItem().toString().equalsIgnoreCase("On_Process")) {
                        laporan.setStatusLaporan(StatusLaporan.ON_PROCESS);
                    }else{
                        laporan.setStatusLaporan(StatusLaporan.DONE);
                    }
                }
                System.out.println(laporan.getStatusLaporan().toString());

                frame.dispose();

            }
        });
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(saveButton);


        frame.add(dataLaporan, BorderLayout.CENTER);
        frame.add(panelButton, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
}
