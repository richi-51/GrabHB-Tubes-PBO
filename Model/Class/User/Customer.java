package Model.Class.User;

import Model.Enum.UserType;

public class Customer extends User {
    private double eMoney;
    String order;
    double rateDriver;
    String report;
    double expenses;

    public Customer(int iD_user, String username, String name, String password, String phoneNumber, String email,
            boolean blocked, UserType userType, double eMoney, String order, double rateDriver, String report,
            double expenses) {
        super(iD_user, username, name, password, phoneNumber, email, blocked, userType);
        this.eMoney = eMoney;
        this.order = order;
        this.rateDriver = rateDriver;
        this.report = report;
        this.expenses = expenses;
    }

    public double geteMoney() {
        return eMoney;
    }

    public void seteMoney(double eMoney) {
        this.eMoney = eMoney;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public double getRateDriver() {
        return rateDriver;
    }

    public void setRateDriver(double rateDriver) {
        this.rateDriver = rateDriver;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

}
