package Model.Class.State;

import Model.Enum.DriverStatus;
import Model.Interface.DriverState;

public class OnlineState implements DriverState {
    @Override
    public void changeAvailability(DriverContext context) {
        context.getDriver().setAvailability(DriverStatus.OFFLINE);
        context.setState(new OfflineState());
    }

    @Override
    public String getStatus() {
        return "Online";
    }

    @Override
    public String getButtonLabel() {
        return "Offline";
    }
}