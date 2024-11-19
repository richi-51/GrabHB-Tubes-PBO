package Model.Class.Order;

import Model.Enum.StatusLaporan;
import java.util.Date;

public class Laporan {
    private int ID_laporan;
    private String isiKeluhan;
    private StatusLaporan statusLaporan;
    private Date createdAt;
    private Date finishAt;

    // Constructor
    public Laporan(int iD_laporan, String isiKeluhan, StatusLaporan statusLaporan, Date createdAt, Date finishAt) {
        ID_laporan = iD_laporan;
        this.isiKeluhan = isiKeluhan;
        this.statusLaporan = statusLaporan;
        this.createdAt = createdAt;
        this.finishAt = finishAt;
    }
    // Getter and Setter
    public int getID_laporan() {
        return ID_laporan;
    }
    public void setID_laporan(int iD_laporan) {
        ID_laporan = iD_laporan;
    }
    public String getIsiKeluhan() {
        return isiKeluhan;
    }
    public void setIsiKeluhan(String isiKeluhan) {
        this.isiKeluhan = isiKeluhan;
    }
    public StatusLaporan getStatusLaporan() {
        return statusLaporan;
    }
    public void setStatusLaporan(StatusLaporan statusLaporan) {
        this.statusLaporan = statusLaporan;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getFinishAt() {
        return finishAt;
    }
    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }
}
