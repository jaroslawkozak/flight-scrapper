from dao import Job, Flight, Airport, Currency
from utils import timeUtils
from Responses import ErrorResponse
from dto import FlightDataDto, FlightDetailsDto, FlightDto, AirportDto, CurrencyDto
import json

MAXIMUM_FLIGHT_DURATION = 30


def get_one_month_data(job_id, from_date):
    job = Job.get_single_job(job_id)
    airports = [job.departureStationId, job.arrivalStationId]
    to_date = timeUtils.add_one_month(from_date)
    flights = Flight.get_flights(airports, airports, from_date, to_date)

    out_flights_dto = []
    in_flights_dto = []

    for flight in flights:
        if ((job.departureAirport.IATA == flight.departureAirport.IATA) and (
                job.arrivalAirport.IATA == flight.arrivalAirport.IATA)):
            out_flights_dto.append(flight)
        elif ((job.departureAirport.IATA == flight.arrivalAirport.IATA) and (
                job.arrivalAirport.IATA == flight.departureAirport.IATA)):
            in_flights_dto.append(flight)
        else:
            print("Error in flight: departure " + flight.departureAirport.IATA
                  + " arrival " + flight.arrivalAirport.IATA)

    flights_dto_response = []
    for outbound in out_flights_dto:
        for inbound in in_flights_dto:
            daysbetween = timeUtils.get_days_between(str(outbound.departureDate), str(inbound.departureDate))
            if (daysbetween > 0) and (daysbetween <= MAXIMUM_FLIGHT_DURATION):
                flights_dto_response.append(FlightDataDto(outbound, inbound).__dict__)

    return flights_dto_response


def get_flight_details(flight_id):
    flight = Flight.get(flight_id)
    if flight is None:
        return json.dumps(ErrorResponse("Error: No flight.", "Requested flight doesn't exist").__dict__)
    departure_airport = flight.departureAirport
    arrival_airport = flight.arrivalAirport
    currency = Currency.get(flight.currencyId)

    return json.dumps(FlightDetailsDto(FlightDto(flight).__dict__,
                                       AirportDto(departure_airport).__dict__,
                                       AirportDto(arrival_airport).__dict__,
                                       CurrencyDto(currency).__dict__).__dict__)


def add_flight_records(timetable):
    try:
        for inboundFlight in timetable['inboundFlights']:
            Flight.add_or_update(inboundFlight)
    except KeyError:
        print "INFO: No inbound flights received."
    try:
        for outboundFlights in timetable['outboundFlights']:
            Flight.add_or_update(outboundFlights)
    except KeyError:
        print "INFO: No outbound flights received."

