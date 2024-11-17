package Model.Class.Vehicle;

public class Vehicle {
    private int ID_Driver;
    private int vehicle_ID;

    private String vehicleName;
    private String plateNumber;

    // Constructor
    public Vehicle(int iD_Driver, int vehicle_ID, String vehicleName, String plateNumber) {
        ID_Driver = iD_Driver;
        this.vehicle_ID = vehicle_ID;
        this.vehicleName = vehicleName;
        this.plateNumber = plateNumber;
    }


    // Getter and Setter
    public int getID_Driver() {
        return ID_Driver;
    }
    public void setID_Driver(int iD_Driver) {
        ID_Driver = iD_Driver;
    }
    public int getVehicle_ID() {
        return vehicle_ID;
    }
    public void setVehicle_ID(int vehicle_ID) {
        this.vehicle_ID = vehicle_ID;
    }
    public String getVehicleName() {
        return vehicleName;
    }
    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
