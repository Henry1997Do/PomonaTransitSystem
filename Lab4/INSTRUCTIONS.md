# Pomona Transit Management System - Setup & Usage Instructions

## ✅ What Has Been Created

A complete Java Swing application with the following components:

### Database Layer (`src/db/`)
- **DBConnection.java** - Manages MySQL database connections with your credentials
- **TestConnection.java** - Utility to test database connectivity

### Data Access Layer (`src/dao/`)
- **TripDAO.java** - Trip table operations
- **TripOfferingDAO.java** - Trip offering CRUD operations
- **DriverDAO.java** - Driver management and weekly schedules
- **BusDAO.java** - Bus management
- **StopDAO.java** - Stop information
- **TripStopInfoDAO.java** - Trip stop relationships
- **ActualTripStopInfoDAO.java** - Actual trip data recording

### GUI Layer (`src/gui/`)
- **TransitApp.java** - Main application window
- **HomePanel.java** - Welcome screen
- **TripPanel.java** - Trip schedule management (search, add, delete, change driver/bus)
- **DriverPanel.java** - Driver management and weekly schedules
- **BusPanel.java** - Bus management
- **StopPanel.java** - View trip stops
- **ActualDataPanel.java** - Record actual trip data
- **Refreshable.java** - Interface for panel refresh functionality

### Configuration
- **Database URL**: `jdbc:mysql://127.0.0.1:3306/pomonatransit?useSSL=false&serverTimezone=UTC`
- **Username**: `root`
- **Password**: `NewPassword123!`

## 🚀 Quick Start

### Prerequisites
1. **MySQL Server** must be running
2. **Database** must be created and populated:
   ```bash
   mysql -u root -p < pomonatransit.sql
   mysql -u root -p < load_sample_data.sql
   ```
3. **Java JDK 11+** installed

### Running the Application

#### Option 1: Using Scripts (Recommended)
```bash
# Compile (already done)
./compile.sh

# Run the application
./run.sh
```

#### Option 2: Manual Commands
```bash
# Compile
javac -d bin -cp "lib/*:." src/db/*.java src/dao/*.java src/gui/*.java src/Main.java

# Run
java -cp "bin:lib/*" Main
```

## 📋 Features & How to Use

### 1. Trip Schedule Management (Trips Panel)
- **Search Trips**: Enter start location, destination, and/or date, then click "Search"
- **Show All Trips**: Click "Show All" to display all trip offerings
- **Add Trip Offering**: Fill in all fields (Trip#, Date, Start Time, Arrival Time, Driver Name, Bus ID) and click "Add Trip"
- **Delete Trip**: Select a trip from the table and click "Delete Trip"
- **Change Driver**: Select a trip and click "Change Driver", enter new driver name
- **Change Bus**: Select a trip and click "Change Bus", enter new bus ID

### 2. Driver Management (Drivers Panel)
- **View All Drivers**: All drivers are displayed in the table
- **Add Driver**: Enter name and phone number, click "Add Driver"
- **Weekly Schedule**: Enter driver name, click "Weekly Schedule", then enter start date (YYYY-MM-DD)

### 3. Bus Management (Buses Panel)
- **View All Buses**: All buses are displayed in the table
- **Add Bus**: Enter Bus ID, Model, and Year, click "Add Bus"
- **Delete Bus**: Select a bus from the table and click "Delete Selected Bus"

### 4. Trip Stops (Stops Panel)
- **View Trip Stops**: Enter a Trip# and click "Show Trip Stops"
- Displays: Stop#, Address, Sequence#, and Driving Time

### 5. Actual Trip Data (Actual Data Panel)
- **Record Data**: Fill in all fields:
  - Trip#, Date, Stop#
  - Scheduled Start Time, Scheduled Arrival Time
  - Actual Start Time, Actual Arrival Time
  - Number of Passengers In, Number of Passengers Out
- Click "Add Record" to save

## 📝 Data Format Guidelines

- **Date Format**: `YYYY-MM-DD` (e.g., `2026-01-14`)
- **Time Format**: `HH:MM:SS` (e.g., `11:00:00`)
- **Driver Names**: Must exist in Driver table before adding trip offerings
- **Trip Numbers**: Must exist in Trip table before adding trip offerings
- **Bus IDs**: Must exist in Bus table before adding trip offerings

## 🔧 Troubleshooting

### Database Connection Issues
1. Verify MySQL is running:
   ```bash
   mysql -u root -p
   ```
2. Check database exists:
   ```sql
   SHOW DATABASES;
   USE pomonatransit;
   SHOW TABLES;
   ```
3. Verify credentials in `src/db/DBConnection.java`

### Compilation Errors
- Ensure MySQL JDBC driver is in `lib/` folder (already copied)
- Check Java version: `java -version` (should be 11+)

### Foreign Key Constraint Errors
- When deleting, ensure no foreign key references exist
- Delete child records (TripOffering, ActualTripStopInfo) before parent records (Trip, Driver, Bus)

## 📁 Project Structure
```
Lab 4/
├── src/
│   ├── db/                    # Database connection
│   ├── dao/                   # Data Access Objects
│   ├── gui/                   # GUI components
│   └── Main.java              # Entry point
├── bin/                       # Compiled classes
├── lib/                       # MySQL JDBC driver
│   ├── mysql-connector-j-9.4.0.jar
│   └── protobuf-java-4.31.1.jar
├── compile.sh                 # Compilation script
├── run.sh                     # Run script
├── pomonatransit.sql          # Database schema
├── load_sample_data.sql       # Sample data
└── README.md                  # Project documentation

```

## 🎯 Sample Test Scenarios

1. **Search for trips from Pomona to LA on 2025-10-23**
   - Go to Trips panel
   - Enter "Pomona" in Start Location
   - Enter "LA" in Destination
   - Enter "2025-10-23" in Date
   - Click Search

2. **Add a new driver**
   - Go to Drivers panel
   - Enter name: "John Doe"
   - Enter phone: "909-555-9999"
   - Click Add Driver

3. **View stops for Trip #1**
   - Go to Stops panel
   - Enter "1" in Trip# field
   - Click Show Trip Stops

4. **View weekly schedule for Bob starting 2025-10-23**
   - Go to Drivers panel
   - Enter "Bob" in Driver Name
   - Click Weekly Schedule
   - Enter "2025-10-23" when prompted

## 💡 Tips

- Use the sidebar navigation to switch between different features
- Tables are read-only; use the forms to add/modify data
- The application will show error messages if operations fail
- All panels refresh automatically after successful operations
- Click "Exit" button to close the application properly

## ✨ All Features Implemented

✅ Display Trip Schedule  
✅ Edit Trip Offerings (Add, Delete, Change Driver, Change Bus)  
✅ Display Stops of a Trip  
✅ Display Weekly Schedule of Driver  
✅ Add a Driver  
✅ Add a Bus  
✅ Delete a Bus  
✅ Record Actual Trip Data  

Enjoy using the Pomona Transit Management System! 🚌
