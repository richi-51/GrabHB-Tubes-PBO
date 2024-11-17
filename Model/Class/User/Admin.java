package Model.Class.User;

import java.util.Date;

import Model.Enum.UserType;

public class Admin extends User{
    private int ID_admin;

    // Constructor
    public Admin(String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType, int id_admin){
        super(username, name, password, phoneNumber, email, updateProfileAt, userType);
        this.ID_admin = id_admin;
    }

    // Getter and Setter
    public int getID_admin() {
        return ID_admin;
    }

    public void setID_admin(int iD_admin) {
        ID_admin = iD_admin;
    }

}
