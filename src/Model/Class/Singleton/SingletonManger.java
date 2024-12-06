package Model.Class.Singleton;

import Model.Class.User.User;

public class SingletonManger {
    private static SingletonManger instance;
    private User loggedInUser;

    private SingletonManger() {}

    public static SingletonManger getInstance() {
        if (instance == null) {
            instance = new SingletonManger();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
