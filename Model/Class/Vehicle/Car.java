package Model.Class.Vehicle;

public class Car extends Vehicle{
    private String carSeat;

    public Car(int driverID, String plateNumber, int vehicleID, String carSeat) {
        super(driverID, plateNumber, vehicleID);
        this.carSeat = carSeat;
    }

    public String getcarSeat() {
        return carSeat;
    }

    public void setcarSeat(String carSeat) {
        this.carSeat = carSeat;
    }

    
}
