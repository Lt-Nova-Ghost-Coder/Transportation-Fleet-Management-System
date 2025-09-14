// Truck.java
package vehicles;

import exceptions.*;
import interfaces.*;

public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final double cargoCapacity = 5000.0;
    private double currentCargo = 0.0;
    private boolean maintenanceNeeded = false;

    public Truck(String id, String model, double maxSpeed, double currentMileage, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance must be non-negative");
        double eff = calculateFuelEfficiency();
        // adjust efficiency if >50% loaded
        if (currentCargo > (cargoCapacity * 0.5)) eff *= 0.9; // reduce efficiency by 10%
        if (eff <= 0) throw new InvalidOperationException("Invalid fuel efficiency");
        double required = distance / eff;
        if (fuelLevel < required) throw new InsufficientFuelException("Not enough fuel for Truck " + getId());
        fuelLevel -= required;
        addMileage(distance);
        System.out.printf("Truck %s: Hauling cargo for %.2f km. Fuel consumed: %.2f L%n", getId(), distance, required);
        if (getCurrentMileage() > 10000) maintenanceNeeded = true;
    }

    @Override public double calculateFuelEfficiency() { return 8.0; }

    // FuelConsumable
    @Override public void refuel(double amount) throws InvalidOperationException { if (amount <= 0) throw new InvalidOperationException("Refuel>0"); fuelLevel += amount; }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException, InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Negative distance");
        double eff = calculateFuelEfficiency();
        if (currentCargo > (cargoCapacity * 0.5)) eff *= 0.9;
        double needed = distance / eff;
        if (fuelLevel < needed) throw new InsufficientFuelException("Not enough fuel");
        fuelLevel -= needed;
        return needed;
    }

    // CargoCarrier
    @Override public void loadCargo(double weight) throws OverloadException {
        if (weight < 0) return;
        if (currentCargo + weight > cargoCapacity) throw new OverloadException("Cargo overload");
        currentCargo += weight;
    }
    @Override public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight < 0) throw new InvalidOperationException("Invalid weight");
        if (weight > currentCargo) throw new InvalidOperationException("Not enough cargo to unload");
        currentCargo -= weight;
    }
    @Override public double getCargoCapacity() { return cargoCapacity; }
    @Override public double getCurrentCargo() { return currentCargo; }

    // Maintainable
    @Override public void scheduleMaintenance() { maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded || getCurrentMileage() > 10000; }
    @Override public void performMaintenance() { maintenanceNeeded = false; System.out.println("Truck " + getId() + " maintenance performed."); }
}
