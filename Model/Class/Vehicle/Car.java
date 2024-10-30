package Model.Class.Vehicle;

public class Car extends Vehicle {
    private int carSeat;

    public Car(int driverID, String plateNumber, int vehicleID, int carSeat) {
        super(driverID, plateNumber, vehicleID);
        this.carSeat = carSeat;
    }

    public int getCarSeat() {
        return carSeat;
    }

    public void setCarSeat(int carSeat) {
        this.carSeat = carSeat;
    }

}
