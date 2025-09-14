// Main.java
package main;
import vehicles.*;
import exceptions.*;


import java.util.*;
import interfaces.*;


public class Main {
    private static FleetManager manager = new FleetManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Demo: create one of each type (simple values)
        try {
            Car car = new Car("C001", "Toyota", 120.0, 0.0, 4);
            car.refuel(50);
            manager.addVehicle(car);

            Truck truck = new Truck("T001", "Ashok", 100.0, 0.0, 6);
            truck.refuel(200);
            manager.addVehicle(truck);

            Bus bus = new Bus("B001", "Volvo", 90.0, 0.0, 6);
            bus.refuel(300);
            manager.addVehicle(bus);

            Airplane plane = new Airplane("A001", "Boeing", 900.0, 0.0, 10000.0);
            plane.refuel(5000);
            manager.addVehicle(plane);

            CargoShip ship = new CargoShip("S001", "Maersk", 40.0, 0.0, false);
            ship.refuel(10000);
            manager.addVehicle(ship);
        } catch (Exception e) {
            System.out.println("Demo vehicle creation failed: " + e.getMessage());
        }

        // Simple demo journey
        System.out.println("=== Demo: start journeys (100 km) ===");
        manager.startAllJourneys(100.0);
        System.out.println(manager.generateReport());

        // Start CLI
        cliLoop();
    }

    private static void cliLoop() {
        while (true) {
            System.out.println("\n--- Fleet CLI ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Exit");
            System.out.print("Choice: ");
            String line = sc.nextLine();
            int choice = -1;
            try { choice = Integer.parseInt(line.trim()); } catch (Exception e) { System.out.println("Invalid choice"); continue; }
            try {
                switch (choice) {
                    case 1: addVehicleCLI(); break;
                    case 2: removeVehicleCLI(); break;
                    case 3: startJourneyCLI(); break;
                    case 4: refuelAllCLI(); break;
                    case 5: manager.maintainAll(); break;
                    case 6: System.out.println(manager.generateReport()); break;
                    case 7: saveFleetCLI(); break;
                    case 8: loadFleetCLI(); break;
                    case 9: searchTypeCLI(); break;
                    case 10: listMaintenanceCLI(); break;
                    case 11: System.out.println("Bye!"); return;
                    default: System.out.println("Unknown option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void addVehicleCLI() {
        try {
            System.out.print("Enter type (Car/Truck/Bus/Airplane/CargoShip): ");
            String type = sc.nextLine().trim();
            System.out.print("ID: "); String id = sc.nextLine().trim();
            System.out.print("Model: "); String model = sc.nextLine().trim();
            System.out.print("MaxSpeed: "); double maxSpeed = Double.parseDouble(sc.nextLine().trim());
            System.out.print("CurrentMileage: "); double mileage = Double.parseDouble(sc.nextLine().trim());

            Vehicle v = null;
            switch (type.toLowerCase()) {
                case "car":
                    System.out.print("NumWheels: "); int nw = Integer.parseInt(sc.nextLine().trim());
                    v = new Car(id, model, maxSpeed, mileage, nw);
                    break;
                case "truck":
                    System.out.print("NumWheels: "); nw = Integer.parseInt(sc.nextLine().trim());
                    v = new Truck(id, model, maxSpeed, mileage, nw);
                    break;
                case "bus":
                    System.out.print("NumWheels: "); nw = Integer.parseInt(sc.nextLine().trim());
                    v = new Bus(id, model, maxSpeed, mileage, nw);
                    break;
                case "airplane":
                    System.out.print("MaxAltitude: "); double alt = Double.parseDouble(sc.nextLine().trim());
                    v = new Airplane(id, model, maxSpeed, mileage, alt);
                    break;
                case "cargoship":
                    System.out.print("HasSail (true/false): "); boolean hs = Boolean.parseBoolean(sc.nextLine().trim());
                    v = new CargoShip(id, model, maxSpeed, mileage, hs);
                    break;
                default:
                    System.out.println("Unknown type");
                    return;
            }
            manager.addVehicle(v);
            System.out.println("Added " + v.getId());
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    private static void removeVehicleCLI() {
        try {
            System.out.print("Enter ID to remove: ");
            String id = sc.nextLine().trim();
            manager.removeVehicle(id);
            System.out.println("Removed " + id);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void startJourneyCLI() {
        try {
            System.out.print("Enter distance (km): ");
            double d = Double.parseDouble(sc.nextLine().trim());
            manager.startAllJourneys(d);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void refuelAllCLI() {
        try {
            System.out.print("Enter amount to refuel each vehicle (L): ");
            double a = Double.parseDouble(sc.nextLine().trim());
            for (Vehicle v : manager.getFleet()) {
                if (v instanceof FuelConsumable) {
                    try { ((FuelConsumable) v).refuel(a); } catch (Exception ex) { System.out.println("Refuel fail for " + v.getId() + ": " + ex.getMessage()); }
                }
            }
            System.out.println("Refueled applicable vehicles.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void saveFleetCLI() {
        System.out.print("Filename to save: ");
        String fn = sc.nextLine().trim();
        manager.saveToFile(fn);
    }

    private static void loadFleetCLI() {
        System.out.print("Filename to load: ");
        String fn = sc.nextLine().trim();
        manager.loadFromFile(fn);
    }

    private static void searchTypeCLI() {
        System.out.print("Type to search (Car/Truck/Bus/Airplane/CargoShip): ");
        String t = sc.nextLine().trim();
        Class<?> cls = null;
        switch (t.toLowerCase()) {
            case "car": cls = Car.class; break;
            case "truck": cls = Truck.class; break;
            case "bus": cls = Bus.class; break;
            case "airplane": cls = Airplane.class; break;
            case "cargoship": cls = CargoShip.class; break;
            default: System.out.println("Unknown type"); return;
        }
        List<Vehicle> results = manager.searchByType(cls);
        System.out.println("Found " + results.size() + " vehicles:");
        for (Vehicle v : results) v.displayInfo();
    }

    private static void listMaintenanceCLI() {
        List<Vehicle> list = manager.getVehiclesNeedingMaintenance();
        if (list.isEmpty()) System.out.println("No vehicles need maintenance.");
        else {
            for (Vehicle v : list) {
                System.out.println(v.getId() + " (" + v.getClass().getSimpleName() + ") mileage: " + v.getCurrentMileage());
            }
        }
    }
}
