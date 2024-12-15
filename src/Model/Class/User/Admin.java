package Model.Class.User;

import java.util.Date;

import Model.Enum.UserType;

public class Admin extends User{

    // Constructor
    public Admin(int id_admin, String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType){
        super(id_admin, username, name, password, phoneNumber, email, updateProfileAt, userType);
    }

    // Getter and Setter
    public int getID_admin() {
        return super.getIdUser();
    }

}
