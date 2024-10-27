package Model.Class.User;
import Model.Enum.UserType;

public abstract class User {
    private int ID_user;
    private String username;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private boolean blocked;
    private UserType userType; // buat enum tipe user
    
    // Constructor

    public User(int iD_user, String username, String name, String password, String phoneNumber, String email, boolean blocked, UserType userType) {
        ID_user = iD_user;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.blocked = blocked;
        this.userType = userType;
    }


    // Getter and Setter
    public int getID_user() {
        return ID_user;
    }
    public void setID_user(int iD_user) {
        ID_user = iD_user;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }


}
