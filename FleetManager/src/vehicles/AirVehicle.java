package vehicles;

import exceptions.*;

public abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String id, String model, double maxSpeed, double currentMileage, double maxAltitude) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage);
        this.maxAltitude = maxAltitude;
    }

    public double getMaxAltitude() { return maxAltitude; }

    @Override
    public double estimateJourneyTime(double distance) {
        double base = baseEstimateTime(distance);
        return base * 0.95; // -5% for direct paths
    }
}