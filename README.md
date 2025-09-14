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
## ⚙️ Compilation & Execution

```plaintext\
javac exceptions/*.java interfaces/*.java vehicles/*.java main/*.java
```

