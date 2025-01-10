package Model.Enum;

public enum TypeBikeOrder {
    HEMAT(0.95), REGULER(1.0), XL(1.1), ELECTRIC (1.05);

    private double priceValue;

    
    private TypeBikeOrder(double priceValue) {
        this.priceValue = priceValue;
    }
   
    public double getPriceValue() {
        return priceValue;
    }
}
