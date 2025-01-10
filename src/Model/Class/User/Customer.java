package Model.Class.User;

import java.util.ArrayList;
import java.util.Date;

import Model.Enum.StatusAcc;
import Model.Enum.UserType;
import Model.Class.Order.Order;
import Model.Class.Payment.Ovo;

public class Customer extends User {
    private StatusAcc statusAcc;
    private Date createdAccAt;

    private Ovo ovoE_money;
    private ArrayList<Order> order = new ArrayList<>();


    // Constructor
    public Customer(int id_customer, String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType, String picPath, StatusAcc statusAcc, Date createdAccAt, Ovo ovoE_money, ArrayList<Order> order) {
        super(id_customer, username, name, password, phoneNumber, email, updateProfileAt, userType, picPath);
        this.statusAcc = statusAcc;
        this.createdAccAt = createdAccAt;
        this.ovoE_money = ovoE_money;
        this.order = order;
    }

    
    // Getter and Setter
    public int getID_Customer() {
        return super.getIdUser();
    }
    public StatusAcc getStatusAcc() {
        return statusAcc;
    }
    public void setStatusAcc(StatusAcc statusAcc) {
        this.statusAcc = statusAcc;
    }
    public Date getCreatedAccAt() {
        return createdAccAt;
    }
    public Ovo getOvoE_money() {
        return ovoE_money;
    }
    public void setOvoE_money(Ovo ovoE_money) {
        this.ovoE_money = ovoE_money;
    }
    public ArrayList<Order> getOrder() {
        return order;
    }
    public void setOrder(ArrayList<Order> order) {
        this.order = order;
    }
    
   


}
