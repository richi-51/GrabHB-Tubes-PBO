package Model.Class.Vehicle;

public abstract class Vehicle {
    private int driverID;
    private String plateNumber;
    private int vehicleID;
    
    public Vehicle(int driverID, String plateNumber, int vehicleID) {
        this.driverID = driverID;
        this.plateNumber = plateNumber;
        this.vehicleID = vehicleID;
    }
    public int getDriverID() {
        return driverID;
    }
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public int getVehicleID() {
        return vehicleID;
    }
    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }
    
}
