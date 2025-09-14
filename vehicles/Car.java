// Car.java
package vehicles;

import exceptions.*;
import interfaces.*;


public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final int passengerCapacity = 5;
    private int currentPassengers = 0;
    private boolean maintenanceNeeded = false;

    public Car(String id, String model, double maxSpeed, double currentMileage, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance must be non-negative");
        double efficiency = calculateFuelEfficiency();
        double required = distance / efficiency;
        if (fuelLevel < required) throw new InsufficientFuelException("Not enough fuel for Car " + getId());
        fuelLevel -= required;
        addMileage(distance);
        System.out.printf("Car %s: Driving on road for %.2f km. Fuel consumed: %.2f L%n", getId(), distance, required);
        if (getCurrentMileage() > 10000) maintenanceNeeded = true;
    }

    @Override
    public double calculateFuelEfficiency() { return 15.0; }

    // FuelConsumable
    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException("Refuel amount must be > 0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException, InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Distance must be non-negative");
        double needed = distance / calculateFuelEfficiency();
        if (fuelLevel < needed) throw new InsufficientFuelException("Not enough fuel");
        fuelLevel -= needed;
        return needed;
    }

    // PassengerCarrier
    @Override
    public void boardPassengers(int count) throws OverloadException {
        if (count < 0) return;
        if (currentPassengers + count > passengerCapacity)
            throw new OverloadException("Exceed passenger capacity");
        currentPassengers += count;
    }
    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count < 0) throw new InvalidOperationException("Invalid count");
        if (count > currentPassengers) throw new InvalidOperationException("Not enough passengers to disembark");
        currentPassengers -= count;
    }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getCurrentPassengers() { return currentPassengers; }

    // Maintainable
    @Override public void scheduleMaintenance() { maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded || getCurrentMileage() > 10000; }
    @Override public void performMaintenance() { maintenanceNeeded = false; System.out.println("Car " + getId() + " maintenance performed."); }
}
