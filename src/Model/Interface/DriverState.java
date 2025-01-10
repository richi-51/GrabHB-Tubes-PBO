package Model.Interface;

import Model.Class.State.DriverContext;

public interface DriverState {
    void changeAvailability(DriverContext context);
    String getStatus();
    String getButtonLabel();
}
