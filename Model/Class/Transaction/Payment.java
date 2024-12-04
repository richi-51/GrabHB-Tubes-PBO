package Model.Class.Transaction;

import Model.Enum.PaymentMethod;
import Model.Enum.PaymentStatus;
import java.time.LocalDateTime;

public class Payment {
    private String ID_Payment;
    private PaymentMethod paymentMethod;
    private double actualFare;
    private String promoCode;
    private double discountAmount;
    private double totalAmountPaid;
    private LocalDateTime paymentDateTime;
    private PaymentStatus paymentStatus;
    
    public Payment(String ID_Payment, PaymentMethod paymentMethod, double actualFare, String promoCode, 
            double discountAmount, double totalAmountPaid, LocalDateTime paymentDateTime, PaymentStatus paymentStatus) {
        this.ID_Payment = ID_Payment;
        this.paymentMethod = paymentMethod;
        this.actualFare = actualFare;
        this.promoCode = promoCode;
        this.discountAmount = discountAmount;
        this.totalAmountPaid = totalAmountPaid;
        this.paymentDateTime = paymentDateTime;
        this.paymentStatus = paymentStatus;
    }

    public String getID_Payment() {
        return ID_Payment;
    }

    public void setID_Payment(String iD_Payment) {
        ID_Payment = iD_Payment;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getActualFare() {
        return actualFare;
    }

    public void setActualFare(double actualFare) {
        this.actualFare = actualFare;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
