package Model.Enum;

public enum TypeCarOrder {
    HEMAT(0.95), REGULER(1.0), XL(1.1), FAST_TRACK(1.3);
    private double priceValue;

    
    private TypeCarOrder(double priceValue){
        this.priceValue = priceValue;
    }
    
    public double getPriceValue() {
        return priceValue;
    }
    
}
