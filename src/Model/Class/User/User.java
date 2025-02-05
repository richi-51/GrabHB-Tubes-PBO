
package Model.Class.User;
import Model.Enum.UserType;
import java.util.Date;

public abstract class User {
    private int ID_User;
    private String username;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private Date updateProfileAt; // untuk waktu update profile
    private UserType userType; // buat enum tipe user
    private String profilePicPath;

    public User(int id_user, String username, String name, String password, String phoneNumber, String email, Date updateProfileAt, UserType userType, String picPath) {
        this.ID_User = id_user;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.updateProfileAt = updateProfileAt;
        this.userType = userType;
        this.profilePicPath = picPath;
    }


    // Getter and Setter
    public int getIdUser() {
        return ID_User;
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
    public Date getUpdateProfileAt() {
        return updateProfileAt;
    }
    public void setUpdateProfileAt(Date updateProfileAt) {
        this.updateProfileAt = updateProfileAt;
    }
    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public String getProfilePicPath() {
        return profilePicPath;
    }
    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }
    
}
