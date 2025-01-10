package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.*;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Controller.VoucherController;
import Model.Class.DateLabelFormatter;
import Model.Class.Order.Voucher;
import Model.Class.Singleton.SingletonManger;

public class ManageVoucher extends JPanel {
    private JButton updateButton[], deleteButton[];
    private JDatePickerImpl valid_ToPckr, valid_fromPckr, validToPckrAdd, validFromPckrAdd;

    public ManageVoucher(TemplateMenu tmp){
        ArrayList<Voucher> vouchers  = new VoucherController().getVouchers();

        setPreferredSize(new Dimension(tmp.getWidthMenuPanels(), tmp.getHeightMenuPanels()-100));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Vouchers", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        JScrollPane panelUtama = new JScrollPane();
        panelUtama.setBorder(null);
        
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(vouchers.size(), 1, 0 , 10));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelVouchers[] = new JPanel[vouchers.size()];

        updateButton = new JButton[vouchers.size()];
        deleteButton = new JButton[vouchers.size()];
        

        for (int i = 0; i < vouchers.size(); i++) {
            panelVouchers[i] = new JPanel(new GridLayout(9,1));
            panelVouchers[i].setPreferredSize(new Dimension(tmp.getWidthMenuPanels() - 30, 250));
            panelVouchers[i].setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

            panelVouchers[i].add(new JLabel("Kode Voucher: " + vouchers.get(i).getKodeVoucher()));
            panelVouchers[i].add(new JLabel("Jenis Layanan: " + vouchers.get(i).getServiceType().toString()));
            panelVouchers[i].add(new JLabel("Jumlah Potongan: " + vouchers.get(i).getJumlahPotongan()));
            panelVouchers[i].add(new JLabel("Valid dari: " + String.valueOf(vouchers.get(i).getValid_from())));
            panelVouchers[i].add(new JLabel("Valid sampai: " + String.valueOf(vouchers.get(i).getValid_to())));
            panelVouchers[i].add(new JLabel("Dibuat pada: " + String.valueOf(vouchers.get(i).getCreated_at())));
            panelVouchers[i].add(new JLabel("Dibuat/Diedit oleh: " + vouchers.get(i).getDibuat_dieditOleh()));
            panelVouchers[i].add(new JLabel("Terakhir di-update pada: " + String.valueOf(vouchers.get(i).getUpdated_at())));

            

            final int index = i;
            JPanel panelButton = new JPanel(new GridLayout(1, 2, 10, 10));
            panelButton.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
            
            updateButton[i] = new JButton("UPDATE");
            updateButton[i].addActionListener(e -> updateVoucherFrame(vouchers.get(index).getID_Voucher(), vouchers.get(index).getKodeVoucher(), vouchers.get(index).getServiceType().toString(), vouchers.get(index).getJumlahPotongan(), vouchers.get(index).getValid_from(), vouchers.get(index).getValid_to()));

            deleteButton[i] = new JButton("DELETE");
            deleteButton[i].addActionListener(e -> new VoucherController().deleteVoucher(vouchers.get(index).getID_Voucher()));

            panelButton.add(updateButton[i]);
            panelButton.add(deleteButton[i]);
            panelVouchers[i].add(panelButton);

            containerPanel.add(panelVouchers[i]);
        }

        // Masukkan containerPanel ke JScrollPane
        panelUtama.setViewportView(containerPanel);

        // Masukkan JScrollPane ke panel utama
        add(panelUtama, BorderLayout.CENTER);
        
        // Add Voucher Button
        JButton addButton = new JButton("ADD NEW VOUCHER");
        addButton.addActionListener(e-> addVoucherFrame());
        add(addButton, BorderLayout.SOUTH);

    }



    private void updateVoucherFrame(int id_voucher, String kodeVoucher, String serviceType, double jmlhPotongan, Date validFrom, Date validTo){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JLabel title = new JLabel("Update Voucher", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(title, BorderLayout.NORTH);

        JPanel dataVoucher = new JPanel(new GridLayout(6, 2, 10,10));
        dataVoucher.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataVoucher.add(new JLabel("Kode Voucher: "));
        JTextField voucherCode = new JTextField(kodeVoucher);
        dataVoucher.add(voucherCode);

        dataVoucher.add(new JLabel("Jenis Layanan: "));
        JTextField jenisLayanan = new JTextField(serviceType);
        dataVoucher.add(jenisLayanan);
        
        dataVoucher.add(new JLabel("Jumlah Potongan: "));
        JTextField jumlahPtngn = new JTextField(String.valueOf(jmlhPotongan));
        dataVoucher.add(jumlahPtngn);

        dataVoucher.add(new JLabel("Valid dari: "));
        valid_fromPckr = createDatePicker(valid_fromPckr);
        setDatePicker(validFrom, valid_fromPckr);
        dataVoucher.add(valid_fromPckr);

        dataVoucher.add(new JLabel("Valid sampai: "));
        valid_ToPckr = createDatePicker(valid_ToPckr);
        setDatePicker(validTo, valid_ToPckr);
        dataVoucher.add(valid_ToPckr);
        
        
        JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));
        panelButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton updateVoucherButton = new JButton("UPDATE VOUCHER");
        updateVoucherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ubah nilai dari datePicker ke String
                Date validDari = (Date) ((UtilDateModel) (valid_fromPckr.getModel())).getValue();
                String formattedValidDari = formatDate(validDari, "yyyy-MM-dd");

                Date validSampai = (Date) ((UtilDateModel) (valid_ToPckr.getModel())).getValue();
                String formattedValidSampai = formatDate(validSampai, "yyyy-MM-dd");

                // Panggil fungsi updateVoucher
                new VoucherController().updateVoucher(id_voucher, SingletonManger.getInstance().getLoggedInUser().getIdUser(), voucherCode.getText(), jenisLayanan.getText(), Double.parseDouble(jumlahPtngn.getText()), java.sql.Date.valueOf(formattedValidDari), java.sql.Date.valueOf(formattedValidSampai));
                frame.dispose();
            }
        });
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(updateVoucherButton);


        frame.add(dataVoucher, BorderLayout.CENTER);
        frame.add(panelButton, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    private void addVoucherFrame(){
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(500, 350);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Add Voucher", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        frame.add(title, BorderLayout.NORTH);

        JPanel dataVoucher = new JPanel(new GridLayout(6, 2, 10,10));
        dataVoucher.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataVoucher.add(new JLabel("Kode Voucher: "));
        JTextField voucherCode = new JTextField();
        dataVoucher.add(voucherCode);

        dataVoucher.add(new JLabel("Jenis Layanan: "));
        JTextField jenisLayanan = new JTextField();
        dataVoucher.add(jenisLayanan);
        
        dataVoucher.add(new JLabel("Jumlah Potongan: "));
        JTextField jumlahPtngn = new JTextField();
        dataVoucher.add(jumlahPtngn);

        dataVoucher.add(new JLabel("Valid dari: "));
        validFromPckrAdd = createDatePicker(validFromPckrAdd);
        dataVoucher.add(validFromPckrAdd);

        dataVoucher.add(new JLabel("Valid sampai: "));
        validToPckrAdd = createDatePicker(validToPckrAdd);
        dataVoucher.add(validToPckrAdd);

        
        JPanel panelButton = new JPanel(new GridLayout(1,2,15,15));
        JButton addVoucherButton = new JButton("ADD THIS NEW VOUCHER");
        addVoucherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ubah nilai dari datePicker ke String
                Date validDari = (Date) ((UtilDateModel) (validFromPckrAdd.getModel())).getValue();
                String formattedValidDari = formatDate(validDari, "yyyy-MM-dd");

                Date validSampai = (Date) ((UtilDateModel) (validToPckrAdd.getModel())).getValue();
                String formattedValidSampai = formatDate(validSampai, "yyyy-MM-dd");

                // Panggil fungsi updateVoucher
                new VoucherController().addVoucher(SingletonManger.getInstance().getLoggedInUser().getIdUser(), voucherCode.getText(), jenisLayanan.getText(), Double.parseDouble(jumlahPtngn.getText()), java.sql.Date.valueOf(formattedValidDari), java.sql.Date.valueOf(formattedValidSampai));
                frame.dispose();

            }
        });
        JButton cancelButton = new JButton("CANCEL");
        cancelButton.addActionListener(e-> frame.dispose());

        panelButton.add(cancelButton);
        panelButton.add(addVoucherButton);


        frame.add(dataVoucher, BorderLayout.CENTER);
        frame.add(panelButton, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }

    private String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }


    // Untuk membuat datePicker
    private JDatePickerImpl createDatePicker(JDatePickerImpl datePicker) {
        // Properti untuk Date Picker
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        // Membuat Date Picker
        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        return datePicker;
    }


    public void setDatePicker(Date tgl, JDatePickerImpl datePicker){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tgl);

            // Set tanggal ke JDatePicker
            datePicker.getModel().setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.getModel().setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
