import Controller.*;
import View.LoginForm;
import View.RegisterForm;
import View.ShowDriverRatingPage;
import View.UpdateAvailabilityPage;

public class MainApp {
    public static void main(String[] args) {
        // LoginForm loginView = new LoginForm();
        // RegisterForm registerView = new RegisterForm();
        // new AuthController(loginView, registerView);
        // loginView.setVisible(true);

        UpdateAvailabilityPage updateAvailability = new UpdateAvailabilityPage();
        new UpdateAvailability(updateAvailability);
        updateAvailability.setVisible(true);

        // ShowDriverRatingPage showDriverRating = new ShowDriverRatingPage();
        // new ShowDriverRating(showDriverRating);
        // showDriverRating.setVisible(true);
    }
}

