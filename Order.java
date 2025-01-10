public class Order {
    private String customerName;
    private String orderType; // "Grabbike" atau "Grabcar"
    private String driverName; // Driver yang mengambil order, null jika belum ada
    
    public Order(String customerName, String orderType) {
        this.customerName = customerName;
        this.orderType = orderType;
        this.driverName = null;
    }

    public synchronized boolean isTaken() {
        return driverName != null;
    }

    public synchronized void assignDriver(String driverName) {
        this.driverName = driverName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getDriverName() {
        return driverName;
    }
}
