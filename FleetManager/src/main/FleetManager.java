// FleetManager.java
package main;

// Self created java files imports
import vehicles.Vehicle;
import vehicles.Car;
import vehicles.Bus;
import vehicles.Airplane;
import vehicles.CargoShip;
import vehicles.Truck;

import exceptions.*;
import interfaces.*;


// Normal imports
import java.io.*;
import java.util.*;


public class FleetManager {
    private List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        for (Vehicle one : fleet) {
            if (one.getId().equals(v.getId())) throw new InvalidOperationException("Duplicate ID: " + v.getId());
        }
        fleet.add(v);
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        Iterator<Vehicle> it = fleet.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(id)) { it.remove(); return;}
        }
        throw new InvalidOperationException("Vehicle not found: " + id);
    }

    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            }
            catch (InsufficientFuelException | InvalidOperationException e) {
                System.out.printf("Error for %s (%s): %s%n", v.getId(), v.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {
        double total = 0.0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                FuelConsumable f = (FuelConsumable) v;
                try {
                    // Note: consumeFuel mutates fuel level; here we only estimate (call consumeFuel then restore) - but simpler: estimate without mutating if method existed
                    // We'll attempt to use consumeFuel but then that's destructive. Safer approach: compute needed = distance / efficiency (if possible)
                    double eff = v.calculateFuelEfficiency();
                    if (eff > 0) total += distance / eff;
                } catch (Exception e) {
                    // ignore
                }
            }
        }
        return total;
    }

    public void maintainAll() {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) m.performMaintenance();
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type) {
        List<Vehicle> res = new ArrayList<>();
        for (Vehicle v : fleet) if (type.isInstance(v)) res.add(v);
        return res;
    }

    public void sortFleetByEfficiency() { Collections.sort(fleet); }

    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fleet Report\n");
        sb.append("Total vehicles: ").append(fleet.size()).append("\n");
        Map<String, Integer> counts = new HashMap<>();
        double totalEfficiency = 0;
        int effCount = 0;
        double totalMileage = 0;
        int maintenanceCount = 0;
        for (Vehicle v : fleet) {
            String type = v.getClass().getSimpleName();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
            double eff = v.calculateFuelEfficiency();
            if (eff > 0) { totalEfficiency += eff; effCount++; }
            totalMileage += v.getCurrentMileage();
            if (v instanceof Maintainable) {
                if (((Maintainable) v).needsMaintenance()) maintenanceCount++;
            }
        }
        sb.append("By type:\n");
        for (String t : counts.keySet()) sb.append("  ").append(t).append(": ").append(counts.get(t)).append("\n");
        sb.append("Average efficiency: ").append(effCount > 0 ? (totalEfficiency / effCount) : 0).append("\n");
        sb.append("Total mileage: ").append(totalMileage).append("\n");
        sb.append("Vehicles needing maintenance: ").append(maintenanceCount).append("\n");
        return sb.toString();
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        List<Vehicle> res = new ArrayList<>();
        for (Vehicle v : fleet) if (v instanceof Maintainable && ((Maintainable) v).needsMaintenance()) res.add(v);
        return res;
    }

    // Persistence:

    // ================== Persistence ==================
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle v : fleet) {
                pw.println(serializeVehicle(v));
            }
            System.out.println("Fleet saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            fleet.clear();
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Vehicle v = createVehicleFromCSV(line);
                    if (v != null) fleet.add(v);
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line);
                }
            }
            System.out.println("Fleet loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    // ================== Serialization ==================
    private String serializeVehicle(Vehicle v) {
        StringBuilder sb = new StringBuilder();
        if (v instanceof Car) {
            Car c = (Car) v;
            sb.append("Car,").append(c.getId()).append(",")
                    .append(c.getModel()).append(",")
                    .append(c.getMaxSpeed()).append(",")
                    .append(c.getNumWheels()).append(",")
                    .append(c.getCurrentMileage()).append(",")
                    .append(c.getPassengerCapacity()).append(",")
                    .append(c.getCurrentPassengers());
        } else if (v instanceof Truck) {
            Truck t = (Truck) v;
            sb.append("Truck,").append(t.getId()).append(",")
                    .append(t.getModel()).append(",")
                    .append(t.getMaxSpeed()).append(",")
                    .append(t.getNumWheels()).append(",")
                    .append(t.getCurrentMileage()).append(",")
                    .append(t.getCargoCapacity()).append(",")
                    .append(t.getCurrentCargo());
        } else if (v instanceof Bus) {
            Bus b = (Bus) v;
            sb.append("Bus,").append(b.getId()).append(",")
                    .append(b.getModel()).append(",")
                    .append(b.getMaxSpeed()).append(",")
                    .append(b.getNumWheels()).append(",")
                    .append(b.getCurrentMileage()).append(",")
                    .append(b.getPassengerCapacity()).append(",")
                    .append(b.getCurrentPassengers()).append(",")
                    .append(b.getCargoCapacity()).append(",")
                    .append(b.getCurrentCargo());
        } else if (v instanceof Airplane) {
            Airplane a = (Airplane) v;
            sb.append("Airplane,").append(a.getId()).append(",")
                    .append(a.getModel()).append(",")
                    .append(a.getMaxSpeed()).append(",")
                    .append(a.getMaxAltitude()).append(",")
                    .append(a.getCurrentMileage()).append(",")
                    .append(a.getPassengerCapacity()).append(",")
                    .append(a.getCurrentPassengers()).append(",")
                    .append(a.getCargoCapacity()).append(",")
                    .append(a.getCurrentCargo());
        } else if (v instanceof CargoShip) {
            CargoShip cs = (CargoShip) v;
            sb.append("CargoShip,").append(cs.getId()).append(",")
                    .append(cs.getModel()).append(",")
                    .append(cs.getMaxSpeed()).append(",")
                    .append(cs.hasSail()).append(",")
                    .append(cs.getCurrentMileage()).append(",")
                    .append(cs.getCargoCapacity()).append(",")
                    .append(cs.getCurrentCargo());
        }
        return sb.toString();
    }


    // ================== Factory Method ==================
    private Vehicle createVehicleFromCSV(String line) throws Exception {
        String[] parts = line.split(",");
        String type = parts[0];

        switch (type) {
            case "Car":
                return new Car(parts[1], parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[5]),
                        Integer.parseInt(parts[4]));
            case "Truck":
                return new Truck(parts[1], parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[5]),
                        Integer.parseInt(parts[4]));
            case "Bus":
                return new Bus(parts[1], parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[5]),
                        Integer.parseInt(parts[4]));
            case "Airplane":
                return new Airplane(parts[1], parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[5]),
                        Double.parseDouble(parts[4]));
            case "CargoShip":
                return new CargoShip(parts[1], parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[5]),
                        Boolean.parseBoolean(parts[4]));
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }

    public List<Vehicle> getFleet() {
        return fleet;
    }
}
