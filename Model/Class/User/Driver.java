package Model.Class.User;
import java.util.Date;

import Model.Class.Vehicle.*;
import Model.Class.Order.Order;
import Model.Enum.*;
public class Driver extends User{
    private int ID_Driver;
    private StatusAcc statusAcc;
    private DriverStatus availability; // untuk program aja
    private Date createdAccAt;

    private Vehicle vehicle;
    
    private Order order;
    private double rating; // untuk program aja   
    
    // Constructor
    public Driver(String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType, int iD_Driver, StatusAcc statusAcc, DriverStatus availability, Date createdAccAt, Vehicle vehicle, Order order, double rating) {
        super(username, name, password, phoneNumber, email, updateProfileAt, userType);
        ID_Driver = iD_Driver;
        this.statusAcc = statusAcc;
        this.availability = availability;
        this.createdAccAt = createdAccAt;
        this.vehicle = vehicle;
        this.order = order;
        this.rating = rating;
    }

    // Getter and Setter
    public int getID_Driver() {
        return ID_Driver;
    }
    public void setID_Driver(int iD_Driver) {
        ID_Driver = iD_Driver;
    }
    public StatusAcc getStatusAcc() {
        return statusAcc;
    }
    public void setStatusAcc(StatusAcc statusAcc) {
        this.statusAcc = statusAcc;
    }
    public DriverStatus getAvailability() {
        return availability;
    }
    public void setAvailability(DriverStatus availability) {
        this.availability = availability;
    }
    public Date getCreatedAccAt() {
        return createdAccAt;
    }
    public void setCreatedAccAt(Date createdAccAt) {
        this.createdAccAt = createdAccAt;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

}
