package Model.Class.User;
import java.util.ArrayList;
import java.util.Date;

import Model.Class.Vehicle.*;
import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;
import Model.Enum.*;
public class Driver extends User{
    private StatusAcc statusAcc;
    private StatusVerification verificationStatus;

    private DriverStatus availability; // untuk program aja
    private Date createdAccAt;

    private Vehicle vehicle;
    
    private ArrayList<Order> order = new ArrayList<>();
    private double rating; // untuk program aja   
    private Ovo ovoDriver;

    // Constructor
    public Driver(int id_driver, String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType, String picPath, StatusAcc statusAcc, DriverStatus availability, Date createdAccAt, Vehicle vehicle, ArrayList<Order> order, double rating, Ovo ovo, StatusVerification verificationStatus) {
        super(id_driver, username, name, password, phoneNumber, email, updateProfileAt, userType, picPath);
        this.statusAcc = statusAcc;
        this.availability = availability;
        this.createdAccAt = createdAccAt;
        this.vehicle = vehicle;
        this.order = order;
        this.rating = rating;
        this.ovoDriver = ovo;
        this.verificationStatus = verificationStatus;
    }

    // Getter and Setter
    public int getID_Driver() {
        return super.getIdUser();
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
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
    public ArrayList<Order> getOrder() {
        return order;
    }
    public void setOrder(ArrayList<Order> order) {
        this.order = order;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public Ovo getOvoDriver() {
        return ovoDriver;
    }

    public void setOvoDriver(Ovo ovoDriver) {
        this.ovoDriver = ovoDriver;
    }

    public StatusVerification getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(StatusVerification verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

}
