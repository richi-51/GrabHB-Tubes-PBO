package Model.Class.User;

import java.util.Date;

public class Driver {
    private String order;
    private Date tglOrder;
    private double rating;
    private double pendapatan;
    private String keluhan;

    public Driver(String order, Date tglOrder, double rating, double pendapatan, String keluhan) {
        this.order = order;
        this.tglOrder = tglOrder;
        this.rating = rating;
        this.pendapatan = pendapatan;
        this.keluhan = keluhan;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Date getTglOrder() {
        return tglOrder;
    }

    public void setTglOrder(Date tglOrder) {
        this.tglOrder = tglOrder;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(double pendapatan) {
        this.pendapatan = pendapatan;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

}
