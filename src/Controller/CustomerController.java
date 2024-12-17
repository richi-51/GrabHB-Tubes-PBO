package Controller;

import java.util.Date;

import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;
import Model.Class.User.Customer;
import Model.Enum.OrderStatus;
import Model.Class.Order.Laporan;

public class CustomerController {
    private Customer customer;
    private Order order;
    private Laporan keluhan;

    public CustomerController(Customer customer) {
        this.customer = customer;
    }

    public String viewOrder() {
        if (customer.getOrder() == null) {
            return "Belum ada order";
        } else {
           order = customer.getOrder();
           return "Order " + order.getID_order() +
               ", Pickup Location: " + order.getPickUpLoc() +
                ", Destination: " + order.getDestination() +
                ", Price: " + order.getPrice();
        }
    }

    public void makeOrder(Order order) { // tinggal update order
        if (customer.getOrder() == null) {
            customer.setOrder(order);
            order.setDestination(null);
            order.setPickUpLoc(null);
            order.setPaymentMethod(null);
            order.setVoucher(null);
            order.getServiceType();
        }
    }

    public void seeBalance(Ovo ovoE_money) {
        customer.getID_Customer();
        customer.getOvoE_money();
    }
    
    public void statusAcc(Date createdAccAt) {
        customer.getID_Customer();
        customer.getCreatedAccAt();
    }

    public void makeReport(){
        // if (order.getOrder_status() == OrderStatus.COMPLETE) {
        // }
        order = customer.getOrder();
        order.getKeluhan();
        keluhan.setIsiKeluhan(null);  
    }

    public void seeHistory(){
        order = customer.getOrder();
        order.getID_order();
        order.getID_driver();
        order.getPickUpLoc();
        order.getDestination();
        order.getOrder_date();
        order.getOrder_status();
        order.getPrice();
        order.getServiceType();
    }
}