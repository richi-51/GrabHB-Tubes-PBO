package Model.Class.Vehicle;

public class Car extends Vehicle{
    private int jumlahSeat;

    // Constructor
    public Car(int iD_Driver, int vehicle_ID, String vehicleName, String plateNumber, int jumlahSeat) {
        super(iD_Driver, vehicle_ID, vehicleName, plateNumber);
        this.jumlahSeat = jumlahSeat;
    }

    // Getter and Setter
    public int getJumlahSeat() {
        return jumlahSeat;
    }

    public void setJumlahSeat(int jumlahSeat) {
        this.jumlahSeat = jumlahSeat;
    }


}
