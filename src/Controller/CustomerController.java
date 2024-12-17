package Controller;

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
        }
        return "Current Order: " + customer.getOrder();
    }

    public void makeOrder(Order order) {
        customer.setOrder(order);
    }

    public void seeBalance(Ovo ovoE_money){
        customer.getOvoE_money();
    }
}