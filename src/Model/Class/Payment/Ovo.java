package Model.Class.Payment;

public class Ovo {
    private int walletId;
    private double saldo;
    private double coins;

    // apakah perlu tambah password untuk verifikasi akun Ovo?


    // Constructor
    public Ovo(int walletId, double saldo, double coins) {
        this.walletId = walletId;
        this.saldo = saldo;
        this.coins = coins;
    }

    
    // Getter and setter
    public int getWalletId() {
        return walletId;
    }
    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    public double getCoins() {
        return coins;
    }
    public void setCoins(double coins) {
        this.coins = coins;
    }
}
