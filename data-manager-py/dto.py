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
        self.departureDate = flight.departureDate
        self.amount = flight.amount
        self.currencyId = flight.currencyId
        self.priceType = flight.priceType
        self.departureDates = flight.departureDates
        self.classOfService = flight.classOfService
        self.hasMacFlight = flight.hasMacFLight
        self.airlineName = flight.airline.airlineName
        self.createdDate = flight.createdDate
        self.updatedDate = flight.updatedDate

