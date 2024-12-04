package Model.Class.Transaction;

import Model.Class.Location.Lokasi;
import Model.Class.User.Customer;
import Model.Class.User.Driver;
import Model.Enum.StatusTransaction;
import java.time.LocalDateTime;

public class Transaction {
    private String ID_Transaction;
    private StatusTransaction statusTransaction;
    private String cancellationReason;
    private double rating;
    private Lokasi pickupLocation;
    private Lokasi dropoffLocation;
    private double distance;
    private LocalDateTime orderDateTime;
    private Customer customer;
    private Driver driver;
    private Payment payment;

    public Transaction(String iD_Transaction, StatusTransaction statusTransaction, String cancellationReason,
            double rating, Lokasi pickupLocation, Lokasi dropoffLocation, double distance, LocalDateTime orderDateTime,
            Customer customer, Driver driver, Payment payment) {
        ID_Transaction = iD_Transaction;
        this.statusTransaction = statusTransaction;
        this.cancellationReason = cancellationReason;
        this.rating = rating;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.distance = distance;
        this.orderDateTime = orderDateTime;
        this.customer = customer;
        this.driver = driver;
        this.payment = payment;
    }

    public String getID_Transaction() {
        return ID_Transaction;
    }

    public void setID_Transaction(String iD_Transaction) {
        ID_Transaction = iD_Transaction;
    }

    public StatusTransaction getStatusTransaction() {
        return statusTransaction;
    }

    public void setStatusTransaction(StatusTransaction statusTransaction) {
        this.statusTransaction = statusTransaction;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Lokasi getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Lokasi pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Lokasi getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(Lokasi dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }


}
