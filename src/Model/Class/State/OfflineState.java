package Model.Class.State;

import Model.Enum.DriverStatus;
import Model.Interface.DriverState;

public class OfflineState implements DriverState {
    @Override
    public void changeAvailability(DriverContext context) {
        context.getDriver().setAvailability(DriverStatus.ONLINE);
        context.setState(new OnlineState());
    }

    @Override
    public String getStatus() {
        return "Offline";
    }

    @Override
    public String getButtonLabel() {
        return "Online";
    }
}

