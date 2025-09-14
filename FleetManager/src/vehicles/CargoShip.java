// CargoShip.java
package vehicles;

import exceptions.*;
import interfaces.*;

public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {
    private final double cargoCapacity = 50000.0;
    private double currentCargo = 0.0;
    private boolean maintenanceNeeded = false;
    private Double fuelLevel; // null means hasSail=true and no fuel

    public CargoShip(String id, String model, double maxSpeed, double currentMileage, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, hasSail);
        if (hasSail) fuelLevel = null;
        else fuelLevel = 0.0;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance negative");
        double eff = calculateFuelEfficiency();
        if (eff <= 0) {
            // If sails-only, no fuel consumption; assume can move
            addMileage(distance);
            System.out.printf("CargoShip %s: Sailing with cargo for %.2f km using sail. Fuel consumed: 0.0 L%n", getId(), distance);
        } else {
            double required = distance / eff;
            if (fuelLevel == null || fuelLevel < required) throw new InsufficientFuelException("Not enough fuel for CargoShip " + getId());
            fuelLevel -= required;
            addMileage(distance);
            System.out.printf("CargoShip %s: Sailing with cargo for %.2f km. Fuel consumed: %.2f L%n", getId(), distance, required);
        }
        if (getCurrentMileage() > 10000) maintenanceNeeded = true;
    }

    @Override
    public double calculateFuelEfficiency() {
        if (hasSail()) {
            // if has sail but also fuelLevel==null treat as sails only => 0
            if (fuelLevel == null) return 0.0;
        }
        return 4.0;
    }

    // CargoCarrier
    @Override public void loadCargo(double weight) throws OverloadException { if (weight < 0) return; if (currentCargo + weight > cargoCapacity) throw new OverloadException("Cargo overload"); currentCargo += weight; }
    @Override public void unloadCargo(double weight) throws InvalidOperationException { if (weight < 0) throw new InvalidOperationException("Invalid weight"); if (weight > currentCargo) throw new InvalidOperationException("Not enough cargo"); currentCargo -= weight; }
    @Override public double getCargoCapacity() { return cargoCapacity; }
    @Override public double getCurrentCargo() { return currentCargo; }

    // Maintainable
    @Override public void scheduleMaintenance() { maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded || getCurrentMileage() > 10000; }
    @Override public void performMaintenance() { maintenanceNeeded = false; System.out.println("CargoShip " + getId() + " maintenance performed."); }

    // FuelConsumable (only if ship has fuel)
    @Override public void refuel(double amount) throws InvalidOperationException {
        if (fuelLevel == null) throw new InvalidOperationException("This ship uses sail and has no fuel tank");
        if (amount <= 0) throw new InvalidOperationException("Refuel>0");
        fuelLevel += amount;
    }
    @Override public double getFuelLevel() {
        if (fuelLevel == null) return 0.0;
        return fuelLevel;
    }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException, InvalidOperationException {
        if (fuelLevel == null) throw new InvalidOperationException("No fuel tank");
        if (distance < 0) throw new InvalidOperationException("Negative distance");
        double needed = distance / calculateFuelEfficiency();
        if (fuelLevel < needed) throw new InsufficientFuelException("Not enough fuel");
        fuelLevel -= needed;
        return needed;
    }
}
