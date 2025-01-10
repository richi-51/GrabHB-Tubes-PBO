import Controller.AuthController;
import View.LoginForm;
import View.RegisterForm;

public class MainApp {

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        RegisterForm registerForm = new RegisterForm();
        new AuthController(loginForm, registerForm);
        loginForm.setVisible(true);
    }
}