from utils import timeUtils


class AirportDto:
    def __init__(self, airport):
        self.airportId = airport.airportId
        self.name = airport.name
        self.city = airport.city
        self.country = airport.country
        self.IATA = airport.IATA
        self.ICAO = airport.ICAO
        self.latitude = airport.latitude
        self.longitude = airport.longitude
        self.altitude = airport.altitude
        self.timezone = airport.timezone
        self.DST = airport.DST


class CurrencyDto:
    def __init__(self, currency):
        self.currencyId = currency.currencyId
        self.currencyCode = currency.currencyCode
        self.currencyName = currency.currencyName


class JobDto:
    def __init__(self, job):
        self.jobId = job.jobId
        self.departureStationIATA = job.departureAirport.IATA
        self.arrivalStationIATA = job.arrivalAirport.IATA
        self.departureStationId = job.departureStationId
        self.arrivalStationId = job.arrivalStationId
        self.status = job.status
        self.lastSuccessfull = job.lastSuccessfull.isoformat()
        self.lastFailed = job.lastFailed.isoformat()
        self.totalSuccess = job.totalSuccess
        self.totalFailed = job.totalFailed
        self.failedInRow = job.failedInRow
        self.isActive = job.isActive
        self.isDeleted = job.isDeleted


class FlightDto:
    def __init__(self, flight):
        self.flightId = flight.flightId
        self.departureStationIATA = flight.departureAirport.IATA
        self.arrivalStationIATA = flight.arrivalAirport.IATA
        self.departureDate = flight.departureDate.isoformat()
        self.amount = flight.amount
        self.currencyId = flight.currencyId
        self.priceType = flight.priceType
        self.departureDates = []
        self.departureDates.append(flight.departureDates) #assuming single element array, might change in future
        self.classOfService = flight.classOfService
        self.hasMacFlight = flight.hasMacFLight
        self.airlineName = flight.airline.airlineName
        self.createdDate = flight.createdDate.isoformat()
        self.updatedDate = flight.updatedDate.isoformat()


class FlightDataDto:
    def __init__(self, outbound, inbound):
        self.outboundFlightId = outbound.flightId
        self.inboundFlightId = inbound.flightId
        self.outboundFlightDate = str(outbound.departureDate)
        self.inboundFlightDate = str(inbound.departureDate)
        self.days = timeUtils.get_days_between(str(outbound.departureDate), str(inbound.departureDate))
        self.price = outbound.amount + inbound.amount

class FlightDetailsDto:
    def __init__(self, flight, depAirport, arrAirport, currency):
        self.flightData = flight
        self.departureAirport = depAirport
        self.arrivalAirport = arrAirport
        self.currency = currency


