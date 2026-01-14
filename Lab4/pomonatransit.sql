-- MySQL dump 10.13  Distrib 9.5.0, for macos26.0 (arm64)
--
-- Host: localhost    Database: pomonatransit
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '573606bc-b125-11f0-9870-7a10e23e8f13:1-82';

--
-- Table structure for table `ActualTripStopInfo`
--

DROP TABLE IF EXISTS `ActualTripStopInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ActualTripStopInfo` (
  `TripNumber` int NOT NULL,
  `Date` date NOT NULL,
  `ScheduledStartTime` time NOT NULL,
  `StopNumber` int NOT NULL,
  `ScheduledArrivalTime` time DEFAULT NULL,
  `ActualStartTime` time DEFAULT NULL,
  `ActualArrivalTime` time DEFAULT NULL,
  `NumberOfPassengerIn` int DEFAULT NULL,
  `NumberOfPassengerOut` int DEFAULT NULL,
  PRIMARY KEY (`TripNumber`,`Date`,`ScheduledStartTime`,`StopNumber`),
  KEY `StopNumber` (`StopNumber`),
  CONSTRAINT `actualtripstopinfo_ibfk_1` FOREIGN KEY (`TripNumber`, `Date`, `ScheduledStartTime`) REFERENCES `TripOffering` (`TripNumber`, `Date`, `ScheduledStartTime`),
  CONSTRAINT `actualtripstopinfo_ibfk_2` FOREIGN KEY (`StopNumber`) REFERENCES `Stop` (`StopNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ActualTripStopInfo`
--

LOCK TABLES `ActualTripStopInfo` WRITE;
/*!40000 ALTER TABLE `ActualTripStopInfo` DISABLE KEYS */;
INSERT INTO `ActualTripStopInfo` (`TripNumber`, `Date`, `ScheduledStartTime`, `StopNumber`, `ScheduledArrivalTime`, `ActualStartTime`, `ActualArrivalTime`, `NumberOfPassengerIn`, `NumberOfPassengerOut`) VALUES (1,'2025-10-23','13:00:00',1,'13:00:00','13:00:00','13:00:00',0,0),(1,'2025-10-23','13:00:00',2,'13:10:00','13:11:00','13:12:00',3,1),(1,'2025-10-23','13:00:00',3,'13:25:00','13:26:00','13:28:00',4,2),(1,'2025-10-23','13:00:00',4,'13:40:00','13:42:00','13:44:00',5,3),(1,'2025-10-23','13:00:00',10,'14:00:00','14:02:00','14:05:00',2,7),(2,'2025-10-24','10:00:00',1,'10:00:00','10:00:00','10:00:00',0,0),(2,'2025-10-24','10:00:00',5,'10:20:00','10:20:00','10:21:00',2,1),(2,'2025-10-24','10:00:00',6,'10:35:00','10:36:00','10:37:00',3,2),(2,'2025-10-24','10:00:00',9,'10:55:00','10:56:00','10:58:00',4,3),(3,'2025-10-25','06:00:00',1,'06:00:00','06:00:00','06:00:00',0,0),(3,'2025-10-25','06:00:00',7,'06:15:00','06:15:00','06:16:00',1,1),(3,'2025-10-25','06:00:00',8,'06:30:00','06:30:00','06:31:00',3,1),(3,'2025-10-25','06:00:00',9,'06:50:00','06:50:00','06:52:00',2,2),(3,'2025-10-25','06:00:00',10,'07:10:00','07:11:00','07:13:00',1,4),(102,'2025-10-25','09:00:00',1,'11:00:00','09:00:00','10:55:00',30,2),(105,'2025-10-25','12:00:00',2,'14:00:00','12:10:00','14:15:00',18,5);
/*!40000 ALTER TABLE `ActualTripStopInfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Bus`
--

DROP TABLE IF EXISTS `Bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Bus` (
  `BusID` int NOT NULL,
  `Model` varchar(50) DEFAULT NULL,
  `Year` int DEFAULT NULL,
  PRIMARY KEY (`BusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Bus`
--

LOCK TABLES `Bus` WRITE;
/*!40000 ALTER TABLE `Bus` DISABLE KEYS */;
INSERT INTO `Bus` (`BusID`, `Model`, `Year`) VALUES (100,'Nova Bus LFS',2013),(101,'Mercedes CityLine',2022),(102,'Volvo Urban',2023),(103,'Toyota Metro',2021),(105,'Scania X',2022),(106,'MAN Lion',2024),(107,'Isuzu City',2019),(108,'Hyundai Metro',2023),(109,'Kia Express',2021),(110,'DAF Urban',2020),(111,'Volvo X',2022),(112,'Mercedes B',2024),(113,'Tesla E',2025),(114,'BYD Green',2024),(115,'Iveco Urban',2023),(116,'BlueBird',2018),(117,'Neoplan',2022),(118,'Mitsubishi',2019),(119,'Gillig',2023),(120,'Van Hool',2022);
/*!40000 ALTER TABLE `Bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Driver`
--

DROP TABLE IF EXISTS `Driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Driver` (
  `DriverID` int NOT NULL AUTO_INCREMENT,
  `DriverName` varchar(50) NOT NULL,
  `DriverTelephoneNumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`DriverID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Driver`
--

LOCK TABLES `Driver` WRITE;
/*!40000 ALTER TABLE `Driver` DISABLE KEYS */;
INSERT INTO `Driver` (`DriverID`, `DriverName`, `DriverTelephoneNumber`) VALUES (1,'Alice','909-555-1001'),(2,'Amelia','909-555-3012'),(3,'Ava','909-555-3008'),(4,'Benjamin','909-555-3019'),(5,'Bob','909-555-1002'),(6,'Charlotte','909-555-3018'),(7,'Chris','909-555-3002'),(8,'Ella','909-555-3016'),(9,'Emma','909-555-3003'),(10,'Ethan','909-555-3009'),(11,'Harper','909-555-3014'),(12,'Isabella','909-555-3020'),(13,'James','909-555-3017'),(14,'Liam','909-555-3005'),(15,'Logan','909-555-3011'),(16,'Lucas','909-555-3013'),(17,'Mason','909-555-3015'),(18,'Mia','909-555-3010'),(19,'Noah','909-555-3007'),(20,'Olivia','909-555-3006'),(21,'Ryan','909-555-3001'),(22,'Sophia','909-555-3004'),(23,'Khoi','909-555-1234');
/*!40000 ALTER TABLE `Driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stop`
--

DROP TABLE IF EXISTS `Stop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Stop` (
  `StopNumber` int NOT NULL,
  `StopAddress` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`StopNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stop`
--

LOCK TABLES `Stop` WRITE;
/*!40000 ALTER TABLE `Stop` DISABLE KEYS */;
INSERT INTO `Stop` (`StopNumber`, `StopAddress`) VALUES (1,'Pomona Transit Center'),(2,'Claremont Village Station'),(3,'Montclair Plaza Transit'),(4,'Ontario Airport Terminal'),(5,'Chino Hills Crossroads'),(6,'Diamond Bar Blvd Stop'),(7,'Walnut City Center'),(8,'West Covina Mall Stop'),(9,'El Monte Bus Hub'),(10,'Union Station Los Angeles');
/*!40000 ALTER TABLE `Stop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Trip`
--

DROP TABLE IF EXISTS `Trip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Trip` (
  `TripNumber` int NOT NULL,
  `StartLocationName` varchar(50) DEFAULT NULL,
  `DestinationName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`TripNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Trip`
--

LOCK TABLES `Trip` WRITE;
/*!40000 ALTER TABLE `Trip` DISABLE KEYS */;
INSERT INTO `Trip` (`TripNumber`, `StartLocationName`, `DestinationName`) VALUES (1,'Pomona','LA'),(2,'Pomona','Irvine'),(3,'Pomona','Pasadena'),(6,'Pomona','LA'),(7,'Pomona','West Covina'),(8,'Pomona','San Gabriel'),(9,'Pomona','Rowland Height'),(10,'Pomona','Pasdena'),(12,'Pomona','LA'),(13,'Pomona','LA'),(23,'Pomona','LA'),(101,'Pomona','LA'),(102,'Pomona','Irvine'),(103,'Pomona','San Diego'),(104,'Pomona','Pasadena'),(105,'Pomona','Santa Monica'),(106,'Pomona','Ontario'),(107,'Pomona','Riverside'),(108,'Pomona','Long Beach'),(109,'Pomona','Burbank'),(110,'Pomona','Anaheim'),(111,'Pomona','Ventura');
/*!40000 ALTER TABLE `Trip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TripOffering`
--

DROP TABLE IF EXISTS `TripOffering`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TripOffering` (
  `TripNumber` int NOT NULL,
  `Date` date NOT NULL,
  `ScheduledStartTime` time NOT NULL,
  `ScheduledArrivalTime` time DEFAULT NULL,
  `DriverID` int DEFAULT NULL,
  `DriverName` varchar(50) DEFAULT NULL,
  `BusID` int DEFAULT NULL,
  PRIMARY KEY (`TripNumber`,`Date`,`ScheduledStartTime`),
  KEY `DriverName` (`DriverName`),
  KEY `BusID` (`BusID`),
  KEY `fk_tripoffering_driverid` (`DriverID`),
  CONSTRAINT `fk_tripoffering_driverid` FOREIGN KEY (`DriverID`) REFERENCES `Driver` (`DriverID`),
  CONSTRAINT `tripoffering_ibfk_3` FOREIGN KEY (`BusID`) REFERENCES `Bus` (`BusID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TripOffering`
--

LOCK TABLES `TripOffering` WRITE;
/*!40000 ALTER TABLE `TripOffering` DISABLE KEYS */;
INSERT INTO `TripOffering` (`TripNumber`, `Date`, `ScheduledStartTime`, `ScheduledArrivalTime`, `DriverID`, `DriverName`, `BusID`) VALUES (1,'2025-10-23','13:00:00','14:30:00',5,'Bob',102),(2,'2025-10-24','10:00:00','11:30:00',3,'Ava',110),(3,'2025-10-25','06:00:00','07:30:00',11,'Harper',116),(6,'2025-10-19','06:00:00','08:00:00',14,'Liam',112),(8,'2025-10-22','15:00:00','17:00:00',12,'Isabella',115),(9,'2025-10-21','07:00:00','08:00:00',1,'Alice',113),(10,'2025-10-21','12:00:00','14:00:00',15,'Logan',116),(102,'2025-10-25','09:00:00','11:00:00',2,'Amelia',102),(103,'2025-10-25','10:00:00','12:00:00',3,'Ava',103),(104,'2025-10-25','11:00:00','13:00:00',4,'Benjamin',100),(105,'2025-10-25','12:00:00','14:00:00',15,'Logan',105),(106,'2025-10-25','13:00:00','15:00:00',6,'Charlotte',101),(107,'2025-10-25','14:00:00','16:00:00',7,'Chris',102),(108,'2025-10-25','15:00:00','17:00:00',8,'Ella',103),(109,'2025-10-25','16:00:00','18:00:00',9,'Emma',100),(110,'2025-10-25','17:00:00','19:00:00',10,'Ethan',105),(111,'2025-10-25','18:00:00','20:00:00',11,'Harper',118);
/*!40000 ALTER TABLE `TripOffering` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TripStopInfo`
--

DROP TABLE IF EXISTS `TripStopInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TripStopInfo` (
  `TripNumber` int NOT NULL,
  `StopNumber` int NOT NULL,
  `SequenceNumber` int DEFAULT NULL,
  `DrivingTime` int DEFAULT NULL,
  PRIMARY KEY (`TripNumber`,`StopNumber`),
  KEY `StopNumber` (`StopNumber`),
  CONSTRAINT `tripstopinfo_ibfk_1` FOREIGN KEY (`TripNumber`) REFERENCES `Trip` (`TripNumber`),
  CONSTRAINT `tripstopinfo_ibfk_2` FOREIGN KEY (`StopNumber`) REFERENCES `Stop` (`StopNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TripStopInfo`
--

LOCK TABLES `TripStopInfo` WRITE;
/*!40000 ALTER TABLE `TripStopInfo` DISABLE KEYS */;
INSERT INTO `TripStopInfo` (`TripNumber`, `StopNumber`, `SequenceNumber`, `DrivingTime`) VALUES (1,1,1,0),(1,2,2,10),(1,3,3,25),(1,4,4,40),(1,10,5,60),(2,1,1,0),(2,5,2,20),(2,6,3,35),(2,9,4,55),(2,10,5,75),(3,1,1,0),(3,7,2,15),(3,8,3,30),(3,9,4,50),(3,10,5,70);
/*!40000 ALTER TABLE `TripStopInfo` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-25 18:56:39
