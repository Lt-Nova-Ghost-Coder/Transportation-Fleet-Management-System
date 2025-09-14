# 🚖 Transportation-Fleet-Management-System - OOP Project

![Java](https://img.shields.io/badge/Java-17-orange)
![OOP](https://img.shields.io/badge/Concepts-Inheritance%2C%20Polymorphism%2C%20Abstraction%2C%20Interfaces-blue)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

A **Fleet Management System** written in Java that demonstrates core  
Object-Oriented Programming concepts along with exception handling and file persistence.

✅ Inheritance  
✅ Polymorphism  
✅ Abstract Classes  
✅ Interfaces  
✅ Exception Handling  
✅ File Persistence (CSV)

---

## 🧬 Object-Oriented Concepts

### 🔹 Inheritance
- `Car`, `Bus`, `Truck` → inherit from **`LandVehicle`**
- `Airplane` → inherits from **`AirVehicle`**
- `CargoShip` → inherits from **`WaterVehicle`**
- All inherit from the abstract **`Vehicle`** class

### 🔹 Polymorphism
- `FleetManager` manages vehicles as `Vehicle` references.
- At runtime, the correct subclass methods (`displayInfo()`, `calculateFuelConsumption()`, etc.) are executed.

### 🔹 Abstract Classes
- `Vehicle` is abstract with common attributes (`id`, `model`, `fuelCapacity`, etc.) and abstract methods (`calculateFuelConsumption`).
- Subclasses implement their own logic.

### 🔹 Interfaces
- `PassengerCarrier`, `CargoCarrier`, `FuelConsumable`, and `Maintainable` add extra behavior.
- Implemented selectively by vehicle types.

---

## 📂 Project Structure

```plaintext\

src/
 ├── exceptions/
 │     ├── InsufficientFuelException.java
 │     ├── InvalidOperationException.java
 │     └── OverloadException.java
 ├── interfaces/
 │     ├── CargoCarrier.java
 │     ├── FuelConsumable.java
 │     ├── Maintainable.java
 │     └── PassengerCarrier.java
 ├── main/
 │     ├── FleetManager.java
 │     └── Main.java
 └── vehicles/
       ├── Airplane.java
       ├── AirVehicle.java
       ├── Bus.java
       ├── Car.java
       ├── CargoShip.java
       ├── LandVehicle.java
       ├── Truck.java
       ├── Vehicle.java
       └── WaterVehicle.java
```

---


## ⚙️ Compilation & Execution

1️⃣ Compile

```plaintext\
javac exceptions/*.java interfaces/*.java vehicles/*.java main/*.java
```
2️⃣ Run
```plaintext\
java main.Main
```

---

## 📝 File Persistence

The system supports saving and loading fleet data from a CSV file.

Example:
```plaintext\
Save vehicles → fleet_data.csv
Load them back on restart.
```
---

## 💻 CLI Menu

The system provides a menu-driven CLI for user interaction:
```plaintext\
===== Fleet Management System =====
1. Add Vehicle
2. Display All Vehicles
3. Simulate Journey
4. Perform Maintenance
5. Save Fleet to CSV
6. Load Fleet from CSV
7. Exit
Enter your choice:
```

---

## 🚀 Example Run
```plaintext\
javac exceptions/*.java interfaces/*.java vehicles/*.java main/*.java
java main.Main
```
```plaintext\
===== Fleet Management System =====
1. Add Vehicle
2. Display All Vehicles
3. Simulate Journey
4. Perform Maintenance
5. Save Fleet to CSV
6. Load Fleet from CSV
7. Exit
Enter your choice: 1
Enter vehicle type (Car/Bus/Truck/Airplane/CargoShip): Car
Enter ID: C001
Enter Model: Toyota
Enter Fuel Capacity: 50
Enter Passenger Capacity: 4
Vehicle added successfully!

Enter your choice: 2
ID: C001 | Model: Toyota | Fuel: 50 | Passengers: 4
```
---

## 🛠️ Exception Handling

Custom exceptions are implemented to ensure robust behavior:
```plaintext\
InsufficientFuelException → raised when journey fuel > available fuel.

OverloadException → raised when load exceeds vehicle capacity.

InvalidOperationException → raised for invalid menu operations.
```
---

## ✅ Features Demonstrated
```plaintext\
Object-Oriented Design

Robust Exception Handling

CLI-based User Interaction

CSV File Persistence

Extensibility (easy to add new vehicle types)
```
---

## 📌 Notes
```plaintext\
You can add, manage, simulate, and persist vehicles.

Extendable to databases or REST APIs in the future.
```
---









