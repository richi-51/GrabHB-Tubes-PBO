package Controller;
import Model.Class.Order.Order;
import Model.Class.User.Driver;
import Model.Enum.DriverStatus;

public class DriverController {
    private Driver driver;

    public DriverController(Driver driver) {
        this.driver = driver;
    }

    public boolean updateAvailability(){
        if (driver.getAvailability() == DriverStatus.OFFLINE) {
            driver.setAvailability(DriverStatus.ONLINE);
            return true;
        } else {
            driver.setAvailability(DriverStatus.OFFLINE);
            return false;
        }
    }

    public String lihatRating(){
        return "Rating Anda: " + driver.getRating();
    }

    public String[] historyOrder(Order[] order){
        String[] history_order = new String[order.length];
        for (int i = 0; i < history_order.length; i++) {
            history_order[i] = "ID Order: " +  order[i].getID_order() + "\nID Driver: " + order[i].getID_driver() + "\nID Customer: " + order[i].getID_Customer() + "\nVoucher: " + order[i].getVoucher() + "\nKeluhan: " + order[i].getKeluhan() + "\nLokasi: " + order[i].getPickUpLoc() + "\nDestination: " + order[i].getDestination() + "\nService Type: " + order[i].getServiceType() + "\nOrder Status: " + order[i].getOrder_status() + "\nOrder Date: " + order[i].getOrder_date() + "\nUpdate Order: " + order[i].getUpdateOrder() + "\nPayment Method: " + order[i].getPaymentMethod() + "\nPrice: " + order[i].getPrice(); 
        }
        return history_order;
    }
}
