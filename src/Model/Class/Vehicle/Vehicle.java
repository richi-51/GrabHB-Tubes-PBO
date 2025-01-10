package Model.Class.Vehicle;

public class Vehicle {
    private int vehicle_ID;

    private String vehicleName;
    private String plateNumber;
    private int jumlahSeat;

    // Constructor
    public Vehicle(int vehicle_ID, String vehicleName, String plateNumber, int jumlahSeat) {
        this.vehicle_ID = vehicle_ID;
        this.vehicleName = vehicleName;
        this.plateNumber = plateNumber;
        this.jumlahSeat = jumlahSeat;
    }

    // Getter and Setter
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
    public int getJumlahSeat() {
        return jumlahSeat;
    }
    public void setJumlahSeat(int jumlahSeat) {
        this.jumlahSeat = jumlahSeat;
    }


    
}
