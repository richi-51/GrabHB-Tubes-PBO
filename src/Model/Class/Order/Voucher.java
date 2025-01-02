package Model.Class.Order;

import java.time.LocalDateTime;
import java.util.Date;

import Model.Enum.ServiceType;

public class Voucher {
    private int ID_Voucher;
    private String kodeVoucher;
    private double jumlahPotongan;
    private ServiceType serviceType;
    private Date valid_from;
    private Date valid_to;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String dibuat_dieditOleh;

    // Constructors
    public Voucher(int iD_Voucher, String kodeVoucher, double jumlahPotongan, ServiceType serviceType, Date valid_from, Date valid_to, LocalDateTime created_at, LocalDateTime updated_at, String dibuat_dieditOleh) {
        this.ID_Voucher = iD_Voucher;
        this.kodeVoucher = kodeVoucher;
        this.jumlahPotongan = jumlahPotongan;
        this.serviceType = serviceType;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.dibuat_dieditOleh = dibuat_dieditOleh;
    }
    // Getter and Setter
    public int getID_Voucher() {
        return ID_Voucher;
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
    
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    public String getDibuat_dieditOleh() {
        return dibuat_dieditOleh;
    }
    public void setDibuat_dieditOleh(String dibuat_dieditOleh) {
        this.dibuat_dieditOleh = dibuat_dieditOleh;
    }
}