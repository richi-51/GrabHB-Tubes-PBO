package Model.Class.Vehicle;

public class Motorcycle extends Vehicle{
    private double engineCC;

    // Constructor
    public Motorcycle(int iD_Driver, int vehicle_ID, String vehicleName, String plateNumber, double engineCC) {
        super(iD_Driver, vehicle_ID, vehicleName, plateNumber);
        this.engineCC = engineCC;
    }

    // Getter and Setter
    public double getEngineCC() {
        return engineCC;
    }

    public void setEngineCC(double engineCC) {
        this.engineCC = engineCC;
    }

}
