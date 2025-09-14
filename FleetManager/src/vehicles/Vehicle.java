package vehicles;


import exceptions.*;
import interfaces.*;

public abstract class Vehicle implements Comparable<Vehicle> {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    public Vehicle(String id, String model, double maxSpeed, double currentMileage) throws InvalidOperationException {
        if (id == null || id.trim().isEmpty()) throw new InvalidOperationException("ID must be non-empty");
        this.id = id.trim();
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = currentMileage;
    }

    public String getId() { return id; }
    public String getModel() {
        return model;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getCurrentMileage() { return currentMileage; }
    protected void addMileage(double distance) { currentMileage += distance; }
    public void displayInfo() {
        System.out.printf("%s | Model: %s | MaxSpeed: %.2f km/h | Mileage: %.2f km%n",
                this.getClass().getSimpleName(), model, maxSpeed, currentMileage);
    }

    protected double baseEstimateTime(double distance) {
        if (maxSpeed <= 0) return Double.POSITIVE_INFINITY;
        return distance / maxSpeed;
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;
    public abstract double calculateFuelEfficiency(); // km per liter (0 if not applicable)
    public abstract double estimateJourneyTime(double distance);

    @Override
    public int compareTo(Vehicle other) {
        double a = this.calculateFuelEfficiency();
        double b = other.calculateFuelEfficiency();
        return Double.compare(a, b);
    }
}