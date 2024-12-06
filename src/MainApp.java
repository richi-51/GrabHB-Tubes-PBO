import Controller.AuthController;
import View.LoginForm;
import View.RegisterForm;

public class MainApp {
    public static void main(String[] args) {
        LoginForm loginView = new LoginForm();
        RegisterForm registerView = new RegisterForm();
        new AuthController(loginView, registerView);

        loginView.setVisible(true);
    }
}
