package vehicles;

import exceptions.*;

public abstract class LandVehicle extends Vehicle {
    private int numWheels;

    public LandVehicle(String id, String model, double maxSpeed, double currentMileage, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage);
        this.numWheels = numWheels;
    }

    public int getNumWheels() {
        return numWheels;
    }


    @Override
    public double estimateJourneyTime(double distance) {
        double base = baseEstimateTime(distance);
        return base * 1.1; // +10% for traffic
    }
}