package Model.Class.Order;

import java.util.Date;

public class Voucher {
    private int ID_Voucher;
    private String kodeVoucher;
    private double jumlahPotongan;
    private Date valid_from;
    private Date valid_to;
    private Date created_at;
    private Date updated_at;

    // Constructors
    public Voucher(int iD_Voucher, String kodeVoucher, double jumlahPotongan, Date valid_from, Date valid_to, Date created_at, Date updated_at) {
        ID_Voucher = iD_Voucher;
        this.kodeVoucher = kodeVoucher;
        this.jumlahPotongan = jumlahPotongan;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    // Getter and Setter
    public int getID_Voucher() {
        return ID_Voucher;
    }
    public void setID_Voucher(int iD_Voucher) {
        ID_Voucher = iD_Voucher;
    }
    public String getKodeVoucher() {
        return kodeVoucher;
    }
    public void setKodeVoucher(String kodeVoucher) {
        this.kodeVoucher = kodeVoucher;
    }
    public double getJumlahPotongan() {
        return jumlahPotongan;
    }
    public void setJumlahPotongan(double jumlahPotongan) {
        this.jumlahPotongan = jumlahPotongan;
    }
    public Date getValid_from() {
        return valid_from;
    }
    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }
    public Date getValid_to() {
        return valid_to;
    }
    public void setValid_to(Date valid_to) {
        this.valid_to = valid_to;
    }
    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public Date getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    
}
