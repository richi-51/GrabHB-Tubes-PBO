package Controller;

import java.util.Date;

import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;
import Model.Class.User.Customer;

public class CustomerController {
    private Customer customer;

    public CustomerController(Customer customer) {
        this.customer = customer;
    }

    public String viewOrder() {
        if (customer.getOrder() == null) {
            return "Belum ada order";
        } else {
           Order currentOrder = customer.getOrder();
           return "Order " + currentOrder.getID_order() +
               ", Pickup Location: " + currentOrder.getPickUpLoc() +
                ", Destination: " + currentOrder.getDestination() +
                ", Price: " + currentOrder.getPrice();
        }
    }

    public void makeOrder(Order order) {
        if (customer.getOrder() == null) {
            customer.setOrder(order);
        }
    }

    public void seeBalance(Ovo ovoE_money) {
        customer.getOvoE_money();
    }

    public void statusAcc(Date createdAccAt) {
        customer.getCreatedAccAt();
    }

    public void seeHistory(){
        Order historyOrder = customer.getOrder();
        historyOrder.getID_order();
        historyOrder.getID_driver();
        historyOrder.getPickUpLoc();
        historyOrder.getDestination();
        historyOrder.getOrder_date();
        historyOrder.getOrder_status();
        historyOrder.getPrice();
        historyOrder.getServiceType();
    }
}