package Model.Class.Location;

public class Lokasi extends Wilayah{
    private String alamat;


    public Lokasi(int iD_wilayah, String kelurahan, String kecamatan, String kota, double garisLintang, double garisBujur, String alamat){
        super(iD_wilayah, kelurahan, kecamatan, kota, garisLintang, garisBujur);
        this.alamat = alamat;
    }

    // Getter and Setter
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }




}
