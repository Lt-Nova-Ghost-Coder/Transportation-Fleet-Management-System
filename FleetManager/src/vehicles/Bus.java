// Bus.java
package vehicles;

import exceptions.*;
import interfaces.*;

public class Bus extends LandVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    private double fuelLevel = 0.0;
    private final int passengerCapacity = 50;
    private int currentPassengers = 0;
    private final double cargoCapacity = 500.0;
    private double currentCargo = 0.0;
    private boolean maintenanceNeeded = false;

    public Bus(String id, String model, double maxSpeed, double currentMileage, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, currentMileage, numWheels);
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) throw new InvalidOperationException("Distance negative");
        double eff = calculateFuelEfficiency();
        double required = distance / eff;
        if (fuelLevel < required) throw new InsufficientFuelException("Not enough fuel for Bus " + getId());
        fuelLevel -= required;
        addMileage(distance);
        System.out.printf("Bus %s: Transporting passengers and cargo for %.2f km. Fuel consumed: %.2f L%n", getId(), distance, required);
        if (getCurrentMileage() > 10000) maintenanceNeeded = true;
    }

    @Override public double calculateFuelEfficiency() { return 10.0; }

    // FuelConsumable
    @Override public void refuel(double amount) throws InvalidOperationException { if (amount <= 0) throw new InvalidOperationException("Refuel>0"); fuelLevel += amount; }
    @Override public double getFuelLevel() { return fuelLevel; }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException, InvalidOperationException {
        if (distance < 0) throw new InvalidOperationException("Negative distance");
        double needed = distance / calculateFuelEfficiency();
        if (fuelLevel < needed) throw new InsufficientFuelException("Not enough fuel");
        fuelLevel -= needed;
        return needed;
    }

    // PassengerCarrier
    @Override public void boardPassengers(int count) throws OverloadException {
        if (count < 0) return;
        if (currentPassengers + count > passengerCapacity) throw new OverloadException("Passenger overload");
        currentPassengers += count;
    }
    @Override public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count < 0) throw new InvalidOperationException("Invalid count");
        if (count > currentPassengers) throw new InvalidOperationException("Not enough passengers");
        currentPassengers -= count;
    }
    @Override public int getPassengerCapacity() { return passengerCapacity; }
    @Override public int getCurrentPassengers() { return currentPassengers; }

    // CargoCarrier
    @Override public void loadCargo(double weight) throws OverloadException { if (weight < 0) return; if (currentCargo + weight > cargoCapacity) throw new OverloadException("Cargo overload"); currentCargo += weight; }
    @Override public void unloadCargo(double weight) throws InvalidOperationException { if (weight < 0) throw new InvalidOperationException("Invalid weight"); if (weight > currentCargo) throw new InvalidOperationException("Not enough cargo"); currentCargo -= weight; }
    @Override public double getCargoCapacity() { return cargoCapacity; }
    @Override public double getCurrentCargo() { return currentCargo; }

    // Maintainable
    @Override public void scheduleMaintenance() { maintenanceNeeded = true; }
    @Override public boolean needsMaintenance() { return maintenanceNeeded || getCurrentMileage() > 10000; }
    @Override public void performMaintenance() { maintenanceNeeded = false; System.out.println("Bus " + getId() + " maintenance performed."); }
}
