package Model.Class.State;

import Model.Class.User.Driver;
import Model.Enum.DriverStatus;
import Model.Interface.DriverState;

public class DriverContext {
    private DriverState state;
    private Driver driver;

    public DriverContext(Driver driver) {
        this.driver = driver;
        setState(driver.getAvailability() == DriverStatus.ONLINE ? new OnlineState() : new OfflineState());
    }

    public void setState(DriverState state) {
        this.state = state;
    }

    public DriverState getState() {
        return state;
    }

    public void changeAvailability() {
        state.changeAvailability(this);
    }

    public Driver getDriver() {
        return driver;
    }
}

