CS4350 Database Systems - Lab #4
Pomona Transit Management System
=================================

## Project Overview
This project implements a comprehensive transit management system using Java, JDBC, and MySQL. The system manages trips, drivers, buses, stops, and actual trip data through an intuitive GUI interface.

## Database Schema

### Tables
1. **Trip** (TripNumber, StartLocationName, DestinationName)
2. **TripOffering** (TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID)
3. **Bus** (BusID, Model, Year)
4. **Driver** (DriverID, DriverName,DriverTelephoneNumber)
5. **Stop** (StopNumber, StopAddress)
6. **ActualTripStopInfo** (TripNumber, Date, ScheduledStartTime, StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut)
7. **TripStopInfo** (TripNumber, StopNumber, SequenceNumber, DrivingTime)

## Setup Instructions

### Prerequisites
- Java JDK 11 or higher
- MySQL Server 8.0 or higher
- MySQL Connector/J (JDBC Driver) - included in `lib/` folder

### Database Setup
1. Start MySQL server
2. Create the database and tables:
   ```bash
   mysql -u root -p < pomonatransit.sql
   ```
3. Load sample data:
   ```bash
   mysql -u root -p < load_sample_data.sql
   ```

### Database Connection Configuration
Edit `src/db/DBConnection.java` if needed:
- Default URL: `jdbc:mysql://localhost:3306/pomonatransit`
- Default User: `root`
- Default Password: `` (empty)

### Compilation
```bash
javac -d . -cp "lib/*:." src/db/*.java src/dao/*.java src/gui/*.java
```

### Running the Application
```bash
java -cp "lib/*:." gui.TransitApp
```

## Features Implementation

### 1. Display Trip Schedule ✅
**Location:** Trips Panel
- Search by Start Location, Destination, and Date
- Shows: Trip#, Start, Destination, Date, Start Time, Arrival Time, Driver Name, Driver Phone, BusID
- Use "Show All" to display all trip offerings

### 2. Edit Trip Offerings ✅
**Location:** Trips Panel

#### a) Delete Trip Offering
- Select a trip from the table
- Click "Delete Trip" button
- Deletes by Trip#, Date, and ScheduledStartTime (composite key)

#### b) Add Trip Offering
- Fill in: Trip#, Trip Date, Start Time, Arrive Time, Driver Name, BusID
- Click "Add Trip" button
- Can add multiple trips by repeating the process

#### c) Change Driver
- Select a trip from the table
- Click "Change Driver" button
- Enter new Driver Name in the dialog

#### d) Change Bus
- Select a trip from the table
- Click "Change Bus" button
- Enter new Bus ID in the dialog

### 3. Display Stops of a Trip ✅
**Location:** Stops Panel
- Enter Trip# in the "Trip#" field
- Click "Show Trip Stops" button
- Displays: Stop#, Address, Sequence#, DrivingTime

### 4. Display Weekly Schedule of Driver ✅
**Location:** Drivers Panel
- Enter Driver Name in the "Driver Name" field
- Click "Weekly Schedule" button
- Enter start date (YYYY-MM-DD) in the dialog
- Shows all trips for that driver for the next 7 days

### 5. Add a Driver ✅
**Location:** Drivers Panel
- Fill in: Name and Phone fields
- Click "Add Driver" button

### 6. Add a Bus ✅
**Location:** Buses Panel
- Fill in: Bus ID, Model, Year
- Click "Add Bus" button

### 7. Delete a Bus ✅
**Location:** Buses Panel
- Select a bus from the table
- Click "Delete Bus" button

### 8. Record Actual Trip Data ✅
**Location:** Actual Data Panel
- Fill in all fields:
  - Trip#, Date, Stop#
  - Scheduled Start, Scheduled Arrival
  - Actual Start, Actual Arrival
  - Passengers In, Passengers Out
- Click "Add Record" button

## Project Structure
```
Lab 4/
├── src/
│   ├── db/
│   │   ├── DBConnection.java      # Database connection manager
│   │   └── TestConnection.java    # Connection test utility
│   ├── dao/                       # Data Access Objects
│   │   ├── TripDAO.java
│   │   ├── TripOfferingDAO.java
│   │   ├── DriverDAO.java
│   │   ├── BusDAO.java
│   │   ├── StopDAO.java
│   │   ├── TripStopInfoDAO.java
│   │   └── ActualTripStopInfoDAO.java
│   ├── gui/                       # GUI Components
│   │   ├── TransitApp.java        # Main application
│   │   ├── HomePanel.java
│   │   ├── TripPanel.java
│   │   ├── DriverPanel.java
│   │   ├── BusPanel.java
│   │   ├── StopPanel.java
│   │   ├── ActualDataPanel.java
│   │   └── Refreshable.java       # Interface for panel refresh
│   └── Main.java                  # Entry point
├── lib/
│   └── mysql-connector-j-*.jar    # MySQL JDBC Driver
├── pomonatransit.sql              # Database schema
├── load_sample_data.sql           # Sample data
└── README.md                      # This file
```

## Key Design Decisions

1. **DriverName as Primary Key**: Following the assignment specification, DriverName (VARCHAR) is used as the primary key in the Driver table and as a foreign key in TripOffering, rather than using a numeric DriverID.

2. **Composite Primary Key**: TripOffering uses (TripNumber, Date, ScheduledStartTime) as the composite primary key, allowing multiple offerings of the same trip on different dates/times.

3. **GUI Design**: Modern Swing-based interface with:
   - Sidebar navigation
   - Tabbed panels for different functionalities
   - Table-based data display
   - Form-based data entry

4. **DAO Pattern**: Separation of database logic from GUI using Data Access Objects for maintainability and testability.

## Testing

### Test Scenarios
1. **Search Trips**: Search for trips from "Pomona" to "LA" on "2025-10-23"
2. **Add Trip**: Add a new trip offering with valid Trip#, Date, Driver Name, and Bus ID
3. **Delete Trip**: Select and delete a trip offering
4. **Change Driver**: Update driver for an existing trip
5. **Change Bus**: Update bus for an existing trip
6. **View Trip Stops**: Enter Trip# 1 to see all stops
7. **Driver Schedule**: View weekly schedule for driver "Bob" starting from "2025-10-23"
8. **Add Driver**: Add a new driver with name and phone
9. **Add Bus**: Add a new bus with ID, model, and year
10. **Delete Bus**: Select and delete a bus
11. **Record Actual Data**: Add actual trip stop information

## Troubleshooting

### Connection Issues
- Verify MySQL is running: `mysql -u root -p`
- Check database exists: `SHOW DATABASES;`
- Verify credentials in `DBConnection.java`

### Compilation Errors
- Ensure JDBC driver is in `lib/` folder
- Check Java version: `java -version` (should be 11+)

### Foreign Key Constraints
- When deleting, ensure no foreign key references exist
- Delete child records (TripOffering, ActualTripStopInfo) before parent records (Trip, Driver, Bus)

## Notes
- Date format: YYYY-MM-DD (e.g., 2026-01-13)
- Time format: HH:MM:SS (e.g., 11:00:00)
- Driver names must exist in Driver table before adding trip offerings
- Trip numbers must exist in Trip table before adding trip offerings
- Bus IDs must exist in Bus table before adding trip offerings
