package Model.Class.Order;
import Model.Class.Location.*;
import Model.Enum.*;
import java.util.Date;


public class Order {
    private int ID_order;
    private int ID_driver;
    private int ID_Customer;
    private Voucher voucher;

    private Lokasi pickUpLoc;
    private Lokasi destination;
    private ServiceType serviceType;
    private OrderStatus order_status;
    private Date order_date;
    private Date updateOrder; // jika ganti lokasi destinasi
    private PaymentMethod paymentMethod; // buat function untuk mengecek jika dia OVO
    private double price;
    private double rating;
    private String ulasan;

    // Constructor
    public Order(int iD_order, int iD_driver, int iD_Customer, Voucher voucher, Lokasi pickUpLoc, Lokasi destination, ServiceType serviceType, OrderStatus order_status, Date order_date, Date updateOrder, PaymentMethod paymentMethod, double price, double rating, String ulasan) {
        ID_order = iD_order;
        ID_driver = iD_driver;
        ID_Customer = iD_Customer;
        this.voucher = voucher;
        this.pickUpLoc = pickUpLoc;
        this.destination = destination;
        this.serviceType = serviceType;
        this.order_status = order_status;
        this.order_date = order_date;
        this.updateOrder = updateOrder;
        this.paymentMethod = paymentMethod;
        this.price = price;
        this.rating = rating;
        this.ulasan =ulasan;
    }
    // Getter and setter
    public int getID_order() {
        return ID_order;
    }
    public void setID_order(int iD_order) {
        ID_order = iD_order;
    }
    public int getID_driver() {
        return ID_driver;
    }
    public void setID_driver(int iD_driver) {
        ID_driver = iD_driver;
    }
    public int getID_Customer() {
        return ID_Customer;
    }
    public void setID_Customer(int iD_Customer) {
        ID_Customer = iD_Customer;
    }
    public Voucher getVoucher() {
        return voucher;
    }
    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
    public Lokasi getPickUpLoc() {
        return pickUpLoc;
    }
    public void setPickUpLoc(Lokasi pickUpLoc) {
        this.pickUpLoc = pickUpLoc;
    }
    public Lokasi getDestination() {
        return destination;
    }
    public void setDestination(Lokasi destination) {
        this.destination = destination;
    }
    public ServiceType getServiceType() {
        return serviceType;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    public OrderStatus getOrder_status() {
        return order_status;
    }
    public void setOrder_status(OrderStatus order_status) {
        this.order_status = order_status;
    }
    public Date getOrder_date() {
        return order_date;
    }
    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }
    public Date getUpdateOrder() {
        return updateOrder;
    }
    public void setUpdateOrder(Date updateOrder) {
        this.updateOrder = updateOrder;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    
    



}
