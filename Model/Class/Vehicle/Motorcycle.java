package Model.Class.Vehicle;

public class Motorcycle extends Vehicle {
    private int engineCC;

    public Motorcycle(int driverID, String plateNumber, int vehicleID, int engineCC) {
        super(driverID, plateNumber, vehicleID);
        this.engineCC = engineCC;
    }

    public int getEngineCC() {
        return engineCC;
    }

    public void setEngineCC(int engineCC) {
        this.engineCC = engineCC;
    }
    
}
