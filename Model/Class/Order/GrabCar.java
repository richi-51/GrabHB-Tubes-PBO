package Model.Class.Order;

import java.util.Date;

import Model.Class.Location.Lokasi;
import Model.Enum.OrderStatus;
import Model.Enum.PaymentMethod;
import Model.Enum.ServiceType;
import Model.Enum.TypeCarOrder;

public class GrabCar extends Order{
    private TypeCarOrder orderType;

    // Constructor
    public GrabCar(int iD_order, int iD_driver, int iD_Customer, Voucher voucher, Laporan keluhan, Lokasi pickUpLoc, Lokasi destination, ServiceType serviceType, OrderStatus order_status, Date order_date, Date updateOrder, PaymentMethod paymentMethod, double price, TypeCarOrder orderType) {
        super(iD_order, iD_driver, iD_Customer, voucher, keluhan, pickUpLoc, destination, serviceType, order_status, order_date, updateOrder, paymentMethod, price);
        this.orderType = orderType;
    }

    // Getter and Setter
    public TypeCarOrder getOrderType() {
        return orderType;
    }

    public void setOrderType(TypeCarOrder orderType) {
        this.orderType = orderType;
    }


}
