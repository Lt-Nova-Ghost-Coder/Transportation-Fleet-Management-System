package vehicles;

import exceptions.*;
import interfaces.*;

public abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    public WaterVehicle(String id, String model, double maxSpeed, double currentMileage, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage);
        this.hasSail = hasSail;
    }

    public boolean hasSail() { return hasSail; }

    @Override
    public double estimateJourneyTime(double distance) {
        double base = baseEstimateTime(distance);
        return base * 1.15; // +15% for currents
    }
}