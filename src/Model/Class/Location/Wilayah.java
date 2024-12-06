package Model.Class.Location;

public class Wilayah{
    private int ID_wilayah;
    private String kelurahan;
    private String kecamatan;
    private String kota;
    private double garisLintang;
    private double garisBujur;

    // Constructor
    public Wilayah(int iD_wilayah, String kelurahan, String kecamatan, String kota, double garisLintang, double garisBujur) {
        ID_wilayah = iD_wilayah;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.kota = kota;
        this.garisLintang = garisLintang;
        this.garisBujur = garisBujur;
    }


    // Getter and Setter 
    public int getID_wilayah() {
        return ID_wilayah;
    }
    public void setID_wilayah(int iD_wilayah) {
        ID_wilayah = iD_wilayah;
    }
    public String getKelurahan() {
        return kelurahan;
    }
    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }
    public String getKecamatan() {
        return kecamatan;
    }
    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }
    public String getKota() {
        return kota;
    }
    public void setKota(String kota) {
        this.kota = kota;
    }
    public double getGarisLintang() {
        return garisLintang;
    }
    public void setGarisLintang(double garisLintang) {
        this.garisLintang = garisLintang;
    }
    public double getGarisBujur() {
        return garisBujur;
    }
    public void setGarisBujur(double garisBujur) {
        this.garisBujur = garisBujur;
    }
}