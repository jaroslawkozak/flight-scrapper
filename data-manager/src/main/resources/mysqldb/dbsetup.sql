-- --------------------------------------------------------
-- Host:                         192.168.7.2
-- Wersja serwera:               5.5.58-0+deb7u1 - (Debian)
-- Serwer OS:                    debian-linux-gnu
-- HeidiSQL Wersja:              9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Zrzut struktury bazy danych flight-scrapper
CREATE DATABASE IF NOT EXISTS `flight-scrapper` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `flight-scrapper`;

-- Zrzut struktury tabela flight-scrapper.airlines
CREATE TABLE IF NOT EXISTS `airlines` (
  `airlineId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `airlineName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`airlineId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Zrzut struktury tabela flight-scrapper.airports
CREATE TABLE IF NOT EXISTS `airports` (
  `airportId` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'Unique OpenFlights identifier for this airport.',
  `name` varchar(100) NOT NULL COMMENT 'Name of airport. May or may not contain the City name',
  `city` varchar(50) NOT NULL COMMENT 'Main city served by airport. May be spelled differently from Name.',
  `country` varchar(50) NOT NULL COMMENT 'Country or territory where airport is located. See countries.dat to cross-reference to ISO 3166-1 codes.',
  `IATA` varchar(3) DEFAULT NULL COMMENT '3-letter IATA code. Null if not assigned/unknown',
  `ICAO` varchar(4) DEFAULT NULL COMMENT '4-letter ICAO code.',
  `latitude` double DEFAULT NULL COMMENT 'Decimal degrees, usually to six significant digits. Negative is South, positive is North.',
  `longitude` double DEFAULT NULL COMMENT 'Decimal degrees, usually to six significant digits. Negative is West, positive is East.',
  `altitude` int(11) DEFAULT NULL COMMENT 'In feet.',
  `timezone` double DEFAULT NULL COMMENT 'Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.',
  `DST` varchar(4) DEFAULT NULL COMMENT 'Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown).',
  PRIMARY KEY (`airportId`)
) ENGINE=InnoDB AUTO_INCREMENT=12058 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.
-- Zrzut struktury tabela flight-scrapper.currencyCodes
CREATE TABLE IF NOT EXISTS `currencyCodes` (
  `currencyId` int(10) unsigned NOT NULL,
  `currencyCode` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currencyName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`currencyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Zrzut struktury tabela flight-scrapper.flights
CREATE TABLE IF NOT EXISTS `flights` (
  `flightId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `departureStationId` int(11) unsigned NOT NULL,
  `arrivalStationId` int(11) unsigned NOT NULL,
  `departureDate` datetime NOT NULL,
  `amount` double unsigned NOT NULL,
  `currencyId` int(10) unsigned NOT NULL,
  `priceType` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `departureDates` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `classOfService` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hasMacFlight` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  `airlineId` int(10) unsigned DEFAULT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  PRIMARY KEY (`flightId`),
  KEY `FK_flights_station_dep` (`departureStationId`),
  KEY `FK_flights_station_arr` (`arrivalStationId`),
  KEY `FK_flights_currencyCodes` (`currencyId`),
  KEY `FK_flights_airlines` (`airlineId`),
  CONSTRAINT `FK_flights_airlines` FOREIGN KEY (`airlineId`) REFERENCES `airlines` (`airlineId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_flights_currencyCodes` FOREIGN KEY (`currencyId`) REFERENCES `currencyCodes` (`currencyId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_flights_station_arr` FOREIGN KEY (`arrivalStationId`) REFERENCES `airports` (`airportId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_flights_station_dep` FOREIGN KEY (`departureStationId`) REFERENCES `airports` (`airportId`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Data exporting was unselected.
-- Zrzut struktury tabela flight-scrapper.jobs
CREATE TABLE IF NOT EXISTS `jobs` (
  `jobId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `departureStationId` int(10) unsigned NOT NULL,
  `arrivalStationId` int(10) unsigned NOT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'new',
  `lastSuccessfull` datetime DEFAULT NULL,
  `lastFailed` datetime DEFAULT NULL,
  `totalSuccess` int(10) unsigned NOT NULL DEFAULT '0',
  `totalFailed` int(10) unsigned NOT NULL DEFAULT '0',
  `failedInRow` int(10) unsigned NOT NULL DEFAULT '0',
  `isActive` int(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`jobId`),
  KEY `FK_jobs_airports` (`departureStationId`),
  KEY `FK_jobs_airports_2` (`arrivalStationId`),
  CONSTRAINT `FK_jobs_airports` FOREIGN KEY (`departureStationId`) REFERENCES `airports` (`airportId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_jobs_airports_2` FOREIGN KEY (`arrivalStationId`) REFERENCES `airports` (`airportId`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Zrzut struktury tabela flight-scrapper.params
CREATE TABLE IF NOT EXISTS `params` (
  `parameter⁯Id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parameterName` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '0',
  `parameterValue` varchar(100) COLLATE utf8_unicode_ci DEFAULT '0',
  PRIMARY KEY (`parameter⁯Id`),
  UNIQUE KEY `parameterName` (`parameterName`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- Zrzucanie danych dla tabeli flight-scrapper.params: ~0 rows (około)
/*!40000 ALTER TABLE `params` DISABLE KEYS */;
INSERT INTO `params` (`parameter⁯Id`, `parameterName`, `parameterValue`) VALUES
	(1, 'firstRun', 'false');
/*!40000 ALTER TABLE `params` ENABLE KEYS */;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
